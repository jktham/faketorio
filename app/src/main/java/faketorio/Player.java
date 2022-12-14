package faketorio;

import org.joml.Vector3f;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
	float speed = 5.0f;
	int selectedItem = 1;
	int itemRotation = 0;

	public Player(Vector3f position, int rotation) {
		super(position, rotation);
		model = App.resources.playerModel.copy();
		model.transform = new Matrix4f().translate(position).translate(0f, 0f, 0.5f);
		model.color = new Vector3f(1f, 0f, 0f);
		name = "player";
	}

	public void update() {
		float deltaMove = speed * App.deltaTime;
		Vector3f front = new Vector3f((float)Math.cos(App.camera.rotation), (float)Math.sin(App.camera.rotation), 0f).negate().normalize();
		Vector3f right = new Vector3f(front).cross(0f, 0f, 1f).normalize();
		if (glfwGetKey(App.window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
			deltaMove *= 4f;
		}
		if (glfwGetKey(App.window, GLFW_KEY_W) == GLFW_PRESS) {
			position.add(new Vector3f(front).mul(deltaMove));
		}
		if (glfwGetKey(App.window, GLFW_KEY_S) == GLFW_PRESS) {
			position.add(new Vector3f(front).mul(deltaMove).negate());
		}
		if (glfwGetKey(App.window, GLFW_KEY_A) == GLFW_PRESS) {
			position.add(new Vector3f(right).mul(deltaMove).negate());
		}
		if (glfwGetKey(App.window, GLFW_KEY_D) == GLFW_PRESS) {
			position.add(new Vector3f(right).mul(deltaMove));
		}

		model.transform = new Matrix4f().translate(position).translate(0f, 0f, 0.5f);
		instanceUpdate();
	}
}
