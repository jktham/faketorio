package faketorio;

import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
	Matrix4f view = new Matrix4f().translate(0f, 0f, -1f);
	Matrix4f projection = new Matrix4f().perspective(1.2f, 1920f/1080f, 0f, 100f);
	double speed = 1.0;

	public void update(long window, double deltaTime) {
		float deltaMove = (float)(speed * deltaTime);
		if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
			view.translate(0f, 0f, deltaMove);
		}
		if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
			view.translate(0f, 0f, -deltaMove);
		}
		if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
			view.translate(deltaMove, 0f, 0f);
		}
		if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
			view.translate(-deltaMove, 0f, 0f);
		}
	}
}
