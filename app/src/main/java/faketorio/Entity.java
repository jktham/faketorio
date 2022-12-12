package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Entity {
	Model model;

	Vector3f position = new Vector3f(0f, 0f, 0f);

	String name = "entity";
	ArrayList<ItemStack> inventory = new ArrayList<ItemStack>();
	Label entityLabel = new Label();

	public Entity() {
		model = App.resources.emptyModel.copy();
	}

	public void init() {
		model.transform = new Matrix4f().translate(position);
		entityLabel = new Label() {
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
		entityLabel.text = "test";
		entityLabel.size = new Vector2f(24f);
		entityLabel.tether = this;
		entityLabel.color = new Vector3f(1f);
		entityLabel.fontAtlas = App.resources.arialTexture;
		entityLabel.hidden = false;
		entityLabel.init();
		App.ui.elements.add(entityLabel);
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
