package faketorio;

import org.joml.Matrix4f;

public class Cube extends Entity {

	public Cube() {
		model = App.resources.cubeModel.copy();
	}

	public void update() {
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.5f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		instanceUpdate();
	}
}
