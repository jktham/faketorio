package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Monke extends Entity {

	public Monke(Vector3f position, int rotation) {
		super(position, rotation);
		model = App.resources.monkeModel.copy();
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.5f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0f, 1f, 0f);
		name = "monke";
		type = 99;
	}

}
