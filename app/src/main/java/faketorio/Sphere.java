package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Sphere extends Entity {

	public Sphere() {
		model = App.resources.sphereModel.copy();
	}

	public void update() {
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 1.0f);
		model.transform.translate(0f, 0f, 0.25f * (float)Math.sin(App.time));
		model.transform.rotate(-0.75f * App.time, new Vector3f(0f, 0f, 1f));
		instanceUpdate();
	}
	
}
