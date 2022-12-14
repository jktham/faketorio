package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Entity {
	Vector3f position;
	int rotation;

	Model model;
	Label label;

	String name;
	ArrayList<ItemStack> inventory;
	int stackSize;
	int type;

	public Entity(Vector3f position, int rotation) {
		this.position = new Vector3f(position);
		this.rotation = rotation;
		model = App.resources.emptyModel.copy();
		model.transform = new Matrix4f().translate(position).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(-1f, -1f, -1f);
		name = "entity";
		inventory = new ArrayList<ItemStack>();
		stackSize = 100;
		type = 0;
		label = new Label() {
			public void update() {
				if (tether != null) {
					Vector4f tetherPos = new Vector4f(0f, 0f, 0f, 1f).mul(new Matrix4f().translate(tether.position));
					Vector2f screenTetherPos = App.camera.worldToScreenPos(new Vector3f(tetherPos.x, tetherPos.y, tetherPos.z).add(tetherWorldOffset)).add(tetherScreenOffset);
					position = new Vector2f(screenTetherPos.x, screenTetherPos.y);
				}
				model = new Matrix4f().translate(new Vector3f(position.x, position.y, 1f)).scale(new Vector3f(size.x, size.y, 1f));
				text = "" + name + "\n";
				for (ItemStack itemStack : inventory) {
					text += itemStack.item.id + ": " + itemStack.item.name + ": " + itemStack.amount + "\n";
				}
				Tile tile = App.world.getTile(App.world.worldToTilePos(Entity.this.position));
				if (tile != null) {
					text += tile.name + "\n";
					for (ItemStack itemStack : tile.inventory) {
						text += itemStack.item.id + ": " + itemStack.item.name + ": " + itemStack.amount + "\n";
					}
				}
				updateMesh();
			}
		};
		label.size = new Vector2f(24f);
		label.tether = this;
		label.color = new Vector3f(1f);
		label.fontAtlas = App.resources.arialTexture;
		label.hidden = false;
		label.init();
		App.ui.elements.add(label);
	}

	public void init() {
		
	}

	public void update() {

	}

	public boolean canBeAdded(int id, int amount) {
		// only amount 1 supported
		boolean found = false;
		for (ItemStack itemStack : inventory) {
			if (itemStack.item.id == id) {
				found = true;
				if (itemStack.amount < stackSize) {
					return true;
				}
			}
		}
		return !found;
	}

	public void addItem(int id, int amount) {
		boolean found = false;
		for (ItemStack itemStack : inventory) {
			if (itemStack.item.id == id) {
				itemStack.amount += amount;
				found = true;
			}
		}
		if (!found) {
			ItemStack itemStack = new ItemStack();
			itemStack.item = App.items.get(id);
			itemStack.amount = amount;
			inventory.add(itemStack);
		}
	}
	
	public Vector2i getOutputTilePos() {
		Vector2i outputTilePos = null;
		if (rotation == 0) {
			outputTilePos = App.world.worldToTilePos(position).add(0, -1);
		} else if (rotation == 1) {
			outputTilePos = App.world.worldToTilePos(position).add(1, 0);
		} else if (rotation == 2) {
			outputTilePos = App.world.worldToTilePos(position).add(0, 1);
		} else if (rotation == 3) {
			outputTilePos = App.world.worldToTilePos(position).add(-1, 0);
		}
		return outputTilePos;
	}

	public void draw() {
		model.draw();
	}

}
