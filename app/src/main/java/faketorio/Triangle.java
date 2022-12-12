package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Triangle extends Entity {
	
	public Triangle() {
		model = App.resources.triangleModel.copy();
	}

	public void update() {
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 1.0f);
		model.transform.rotate(0.5f * App.time, new Vector3f(0f, 0f, 1f));
		instanceUpdate();
	}
}
