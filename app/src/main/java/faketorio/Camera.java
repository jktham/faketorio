package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector3f;


public class Camera {
	Matrix4f view = new Matrix4f();
	Matrix4f projection = new Matrix4f().perspective(1.2f, (float)App.width/(float)App.height, 0.1f, 1000f);

	float radius = 10f;
	float rotation = -(float)Math.PI / 2f;
	float angle = (float)Math.PI / 4f;

	Vector3f position = new Vector3f(0f, 0f, 0f);

	public void update() {
		Vector3f offset = new Vector3f();
		offset.add((float)Math.cos(rotation), (float)Math.sin(rotation), 1f);
		offset.mul((float)Math.sin(angle), (float)Math.sin(angle), (float)Math.cos(angle));
		offset.mul(radius);

		position = new Vector3f(App.player.position).add(offset).add(0f, 0f, 1f);

		view = new Matrix4f();
		view.lookAlong(new Vector3f(offset).negate(), new Vector3f(0f, 0f, 1f));
		view.translate(new Vector3f(position).negate());
	}
}
