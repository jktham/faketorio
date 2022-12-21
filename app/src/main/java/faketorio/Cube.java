package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Cube extends Entity {

	public Cube(Vector3f position, int rotation) {
		super(position, rotation);
		model = App.resources.cubeModel.copy();
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.5f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0f, 0f, 1f);
		name = "cube";
		type = 99;
	}

}
