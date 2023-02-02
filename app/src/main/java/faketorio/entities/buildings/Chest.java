package faketorio.entities.buildings;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.engine.App;

public class Chest extends Building {
	
	public Chest(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.chestModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.05f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
		name = "chest";
		inventory.stackSize = 1000;
		inventory.inventorySize = 100;
		type = 6;
		label.hidden = false;
	}
	
	public void update() {
		output = App.world.getBuilding(getOutputTilePos());
		updateBorders();
		
		if (colorResetTicks > 0) {
			colorResetTicks -= 1;
		} else {
			model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
		}
	}

	public void addItem(int id, int amount, Building source) {
		inventory.addItem(id, amount);
		model.meshColors.set(5, App.items.get(id).color);
		colorResetTicks = 6-1;
	}

	public Vector2i getOutputTilePos() {
		return new Vector2i(-99999);
	}
}
