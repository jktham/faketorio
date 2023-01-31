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
		inventory.stackSize = 1000;
		inventory.inventorySize = 100;
		type = 11;
		label.hidden = false;
		maxHealth = 10000;
		health = maxHealth;
	}

	public void update() {

	}

	public Vector2i getOutputTilePos() {
		return new Vector2i(-99999);
	}
}
