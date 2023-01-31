package faketorio.entities;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import faketorio.engine.App;
import faketorio.engine.Model;
import faketorio.entities.buildings.Building;
import faketorio.inventory.Inventory;
import faketorio.inventory.ItemStack;
import faketorio.ui.Label;
import faketorio.world.Tile;

public class Entity {
	public Vector3f worldPos;

	public Model model;
	public Label label;

	public String name;
	public Inventory inventory;

	public int type;
	public int sleepTicks;

	public int maxHealth;
	public int health;

	public Entity(Vector3f worldPos) {
		this.worldPos = new Vector3f(worldPos);
		model = App.resources.emptyModel.copy();
		model.transform = new Matrix4f().translate(worldPos);
		model.color = new Vector3f(-1f, -1f, -1f);
		name = "entity";
		inventory = new Inventory();
		inventory.stackSize = 100;
		inventory.inventorySize = 10;
		type = 0;
		sleepTicks = 0;
		maxHealth = 100;
		health = maxHealth;
		label = new Label() {
			public void update() {
				if (tether != null) {
					Vector4f tetherPos = new Vector4f(0f, 0f, 0f, 1f).mul(new Matrix4f().translate(tether.worldPos));
					Vector2f screenTetherPos = App.camera.worldToScreenPos(new Vector3f(tetherPos.x, tetherPos.y, tetherPos.z).add(tetherWorldOffset)).add(tetherScreenOffset);
					position = new Vector2f(screenTetherPos.x, screenTetherPos.y);
				}
				model = new Matrix4f().translate(new Vector3f(position.x, position.y, 1f)).scale(new Vector3f(size.x, size.y, 1f));
				text = "" + name + " " + health + "/" + maxHealth + " " + sleepTicks + "\n";
				for (ItemStack stack : inventory.stacks) {
					text += stack.item.id + ": " + stack.item.name + ": " + stack.amount + "\n";
				}
				Tile tile = App.world.getTile(App.world.worldToTilePos(Entity.this.worldPos));
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
		label.hidden = true;
		label.init();
		App.ui.elements.add(label);
	}

	public void init() {
		
	}

	public void update() {

	}

	public void draw() {
		model.draw();
	}

	public boolean canBeAdded(int id, int amount, Building source) {
		return inventory.canBeAdded(id, amount);
	}

	public void addItem(int id, int amount, Building source) {
		inventory.addItem(id, amount);
		sleepTicks = 1;
	}

	public void removeItem(int id, int amount) {
		inventory.removeItem(id, amount);
	}

}
