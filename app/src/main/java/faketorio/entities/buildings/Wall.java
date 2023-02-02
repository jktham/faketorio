package faketorio.entities.buildings;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.engine.App;

public class Wall extends Building {
	
	public Wall(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.wallModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.2f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(0, new Vector3f(0.3f, 0.3f, 0.3f));
		name = "wall";
		inventory.stackSize = 0;
		inventory.inventorySize = 0;
		type = 11;
		label.hidden = false;
		maxHealth = 10000;
		health = maxHealth;
	}

	public void update() {
		updateBorders();
	}

	public Vector2i getOutputTilePos() {
		return new Vector2i(-99999);
	}

	public void updateBorders() {
		model.meshHidden.set(1, true);
		model.meshHidden.set(2, true);
		model.meshHidden.set(3, true);
		model.meshHidden.set(4, true);

		if (App.world.getBuilding(new Vector2i(tilePos).add(0, 1)) != null && App.world.getBuilding(new Vector2i(tilePos).add(0, 1)).type == type) {
			model.meshHidden.set(getBorderIndex(0), false);
		}
		if (App.world.getBuilding(new Vector2i(tilePos).add(1, 0)) != null && App.world.getBuilding(new Vector2i(tilePos).add(1, 0)).type == type) {
			model.meshHidden.set(getBorderIndex(1), false);
		}
		if (App.world.getBuilding(new Vector2i(tilePos).add(0, -1)) != null && App.world.getBuilding(new Vector2i(tilePos).add(0, -1)).type == type) {
			model.meshHidden.set(getBorderIndex(2), false);
		}
		if (App.world.getBuilding(new Vector2i(tilePos).add(-1, 0)) != null && App.world.getBuilding(new Vector2i(tilePos).add(-1, 0)).type == type) {
			model.meshHidden.set(getBorderIndex(3), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(0, 1)) && App.world.ghost.type == type) {
			model.meshHidden.set(getBorderIndex(0), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(1, 0)) && App.world.ghost.type == type) {
			model.meshHidden.set(getBorderIndex(1), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(0, -1)) && App.world.ghost.type == type) {
			model.meshHidden.set(getBorderIndex(2), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(-1, 0)) && App.world.ghost.type == type) {
			model.meshHidden.set(getBorderIndex(3), false);
		}
	}
	
	public boolean canBeAdded(int id, int amount, Building source) {
		return false;
	}
}
