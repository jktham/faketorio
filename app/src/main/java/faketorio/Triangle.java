package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Triangle extends Building {
	
	public Triangle(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.triangleModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 1.0f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(-1f, -1f, -1f);
		name = "triangle";
		type = 99;
	}

	public void update() {
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 1.0f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.transform.rotate(0.5f * App.time, new Vector3f(0f, 0f, 1f));
	}

}
