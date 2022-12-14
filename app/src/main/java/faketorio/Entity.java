package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Entity {
	Vector3f position;
	int rotation;

	Model model;
	Label label;

	String name;
	ArrayList<ItemStack> inventory;

	public Entity(Vector3f position, int rotation) {
		this.position = new Vector3f(position);
		this.rotation = rotation;
		model = App.resources.emptyModel.copy();
		model.transform = new Matrix4f().translate(position).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0f, 0f, 1f);
		name = "entity";
		inventory = new ArrayList<ItemStack>();
		label = new Label() {
			public void instanceUpdate() {
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
		instanceUpdate();
	}
	
	public void instanceUpdate() {

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

	public void draw() {
		model.draw();
	}

}
