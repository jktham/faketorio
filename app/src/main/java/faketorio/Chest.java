package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Chest extends Entity {
	
	public Chest(Vector3f position, int rotation) {
		super(position, rotation);
		model = App.resources.chestModel.copy();
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.05f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(5, new Vector3f(0.6f, 0.6f, 0.6f));
		name = "chest";
		stackSize = 1000;
		inventorySize = 100;
		type = 2;
	}

	public void update() {
		updateBorders();
	}

	public Vector2i getOutputTilePos() {
		return new Vector2i(-99999);
	}
}
