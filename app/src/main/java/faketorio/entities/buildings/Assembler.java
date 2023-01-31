package faketorio.entities.buildings;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import faketorio.engine.App;
import faketorio.inventory.Inventory;
import faketorio.inventory.ItemStack;
import faketorio.inventory.Recipe;
import faketorio.ui.Label;
import faketorio.world.Tile;

public class Assembler extends Building {
	public Recipe recipe;
	public Inventory inputInventory;
	
	public Assembler(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.assemblerModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.05f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(5, new Vector3f(0.6f, 0.6f, 0.6f));
		name = "assembler";
		inventory.stackSize = 2;
		inventory.inventorySize = 100;
		type = 7;

		recipe = App.recipes.get(0);
		inputInventory = new Inventory();
		inputInventory.stackSize = recipe.maxInputAmount*2;
		inputInventory.inventorySize = 100;

		App.ui.elements.remove(label);
		label = new Label() {
			public void update() {
				if (tether != null) {
					Vector4f tetherPos = new Vector4f(0f, 0f, 0f, 1f).mul(new Matrix4f().translate(tether.worldPos));
					Vector2f screenTetherPos = App.camera.worldToScreenPos(new Vector3f(tetherPos.x, tetherPos.y, tetherPos.z).add(tetherWorldOffset)).add(tetherScreenOffset);
					position = new Vector2f(screenTetherPos.x, screenTetherPos.y);
				}
				model = new Matrix4f().translate(new Vector3f(position.x, position.y, 1f)).scale(new Vector3f(size.x, size.y, 1f));
				text = "" + name + " " + health + "/" + maxHealth + "\n";
				text += recipe.output.get(0).item.name + "\n";
				for (ItemStack stack : inputInventory.stacks) {
					text += "i: " + stack.item.id + ": " + stack.item.name + ": " + stack.amount + "\n";
				}
				for (ItemStack stack : inventory.stacks) {
					text += "o: " + stack.item.id + ": " + stack.item.name + ": " + stack.amount + "\n";
				}
				Tile tile = App.world.getTile(App.world.worldToTilePos(Assembler.this.worldPos));
				if (tile != null) {
					text += tile.name + "\n";
					for (ItemStack stack : tile.inventory.stacks) {
						text += stack.item.id + ": " + stack.item.name + ": " + stack.amount + "\n";
					}
				}
				updateMesh();
			}
		};
		label.size = new Vector2f(16f);
		label.tether = this;
		label.color = new Vector3f(1f);
		label.fontAtlas = App.resources.arialTexture;
		label.hidden = false;
		label.init();
		App.ui.elements.add(label);
	}

	public void update() {
		output = App.world.getBuilding(getOutputTilePos());
		updateBorders();

		if (ghost) {
			return;
		}
		
		if (App.tick % 6 == 0) {
			model.meshColors.set(5, new Vector3f(0.6f, 0.6f, 0.6f));
		}
		
		if (sleepTicks > 0) {
			sleepTicks -= 1;
			return;
		}

		boolean enoughInput = true;
		if (App.tick % 12 == 0) {
			for (ItemStack rstack : recipe.input) {
				boolean found = false;
				for (ItemStack istack : inputInventory.stacks) {
					if (rstack.item.id == istack.item.id) {
						if (istack.amount >= rstack.amount) {
							found = true;
						}
					}
				}
				if (!found) {
					enoughInput = false;
				}
			}
			boolean outputFull = false;
			for (ItemStack stack : recipe.output) {
				if (!inventory.canBeAdded(stack.item.id, stack.amount)) {
					outputFull = true;
				}
			}
			if (enoughInput && !outputFull) {
				for (ItemStack stack : recipe.input) {
					inputInventory.removeItem(stack.item.id, stack.amount);
				}
				for (ItemStack stack : recipe.output) {
					inventory.addItem(stack.item.id, stack.amount);
					model.meshColors.set(5, stack.item.color);
				}
				sleepTicks = recipe.time-1;
			}
		}

	}

	public boolean canBeAdded(int id, int amount, Building source) {
		if (id == -1) {
			return true;
		}
		for (ItemStack stack : recipe.input) {
			if (stack.item.id == id) {
				return true && inputInventory.canBeAdded(id, amount);
			}
		}
		return false;
	}
	
	public void addItem(int id, int amount, Building source) {
		inputInventory.addItem(id, amount);
	}

	public Vector2i getOutputTilePos() {
		return new Vector2i(-99999);
	}

	public void interact() {
		recipe = App.recipes.get((App.recipes.indexOf(recipe)+1)%App.recipes.size());
		inputInventory.stackSize = recipe.maxInputAmount*2;
	}
}
