package faketorio.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera {
	public Matrix4f view = new Matrix4f();
	public Matrix4f projection = new Matrix4f().perspective(1.2f, (float)App.width/(float)App.height, 0.1f, 1000f);

	public float radius = 10f;
	public float rotation = -(float)Math.PI / 2f;
	public float angle = (float)Math.PI / 4f;

	public Vector3f position = new Vector3f(0f, 0f, 0f);

	public void update() {
		projection = new Matrix4f().perspective(1.2f, (float)App.width/(float)App.height, 0.1f, 1000f);

		Vector3f offset = new Vector3f();
		offset.add((float)Math.cos(rotation), (float)Math.sin(rotation), 1f);
		offset.mul((float)Math.sin(angle), (float)Math.sin(angle), (float)Math.cos(angle));
		offset.mul(radius);

		position = new Vector3f(App.player.position).add(offset).add(0f, 0f, 1f);

		view = new Matrix4f();
		view.lookAlong(new Vector3f(offset).negate(), new Vector3f(0f, 0f, 1f));
		view.translate(new Vector3f(position).negate());
	}

	public Vector3f screenToWorldPos(Vector2i screenPos) {
		if (screenPos == null) {
			return null;
		}
		Vector4f clickPos = new Vector4f(((float)screenPos.x / (float)App.width) * 2f - 1f, 1f - ((float)screenPos.y / (float)App.height) * 2f, -1f, 1f);
		clickPos.mul(new Matrix4f(projection).invert());
		clickPos.z = -1f;
		clickPos.w = 0f;
		clickPos.mul(new Matrix4f(view).invert());
		Vector3f ray = new Vector3f(clickPos.x, clickPos.y, clickPos.z).normalize();
		Vector3f planeNormal = new Vector3f(0f, 0f, 1f).normalize();
		float planeDistance = 0f;
		if (new Vector3f(ray).dot(new Vector3f(planeNormal)) == 0f) {
			return null;
		}
		float t = -1 * (new Vector3f(position).dot(new Vector3f(planeNormal)) + planeDistance) / (new Vector3f(ray).dot(new Vector3f(planeNormal)));
		if (t < 0) {
			return null;
		}
		Vector3f worldPos = new Vector3f(position).add(new Vector3f(ray).mul(t));
		if (!(worldPos.x < App.world.size.x / 2f && worldPos.x > -App.world.size.x / 2f && worldPos.y < App.world.size.y / 2f && worldPos.y > -App.world.size.y / 2f)) {
			return null;
		}
		return worldPos;
	}

	public Vector2f worldToScreenPos(Vector3f worldPos) {
		Vector4f clipPos = new Vector4f(worldPos.x, worldPos.y, worldPos.z, 1.0f).mul(view).mul(projection);
		Vector3f ndcPos = new Vector3f(clipPos.x, clipPos.y, clipPos.z).div(clipPos.w);
		Vector2f screenPos = new Vector2f((ndcPos.x + 1f) / 2f * App.width, App.height - (ndcPos.y + 1f) / 2f * App.height);
		return screenPos;
	}
}
