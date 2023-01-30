package faketorio.entities.buildings;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.engine.App;
import faketorio.inventory.Inventory;
import faketorio.inventory.ItemStack;
import faketorio.inventory.Recipe;

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
		inventory.stackSize = 100;
		inventory.inventorySize = 100;
		type = 8;

		recipe = App.recipes.get(0);
		inputInventory = new Inventory();
		inputInventory.stackSize = 2;
		inputInventory.inventorySize = 100;
	}

	public void update() {
		output = App.world.getBuilding(getOutputTilePos());
		updateBorders();

		if (ghost) {
			return;
		}
		
		model.meshColors.set(5, new Vector3f(0.6f, 0.6f, 0.6f));
		if (sleepTicks > 0) {
			sleepTicks -= 1;
			return;
		}

		boolean enough = true;
		if (App.tick % 120 == 0) {
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
					enough = false;
				}
			}
			if (enough) {
				for (ItemStack stack : recipe.input) {
					inputInventory.removeItem(stack.item.id, stack.amount);
				}
				for (ItemStack stack : recipe.output) {
					inventory.addItem(stack.item.id, stack.amount);
					model.meshColors.set(5, stack.item.color);
				}
			}
		}

	}

	public boolean canBeAdded(int id, int amount, Building source) {
		for (ItemStack stack : recipe.input) {
			if (stack.item.id == id) {
				return true && inputInventory.canBeAdded(id, amount);
			}
		}
		return false;
	}
	
	public void addItem(int id, int amount, Building source) {
		inputInventory.addItem(id, amount);
		sleepTicks = 1;
	}

	public Vector2i getOutputTilePos() {
		return new Vector2i(-99999);
	}
}
