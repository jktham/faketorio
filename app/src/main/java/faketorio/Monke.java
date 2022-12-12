package faketorio;

import org.joml.Matrix4f;

public class Monke extends Entity {

	public Monke() {
		model = App.resources.monkeModel.copy();
	}

	public void update() {
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.5f);
		model.transform.rotate((float)Math.PI/2f, 0f, 0f, 1f);
	}
}
