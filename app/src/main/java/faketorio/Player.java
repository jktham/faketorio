package faketorio;

import org.joml.Vector3f;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.FloatBuffer;

public class Player extends Entity {
	float speed = 5.0f;
	Vector3f color = new Vector3f();
	int item = 1;

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
		model = new Matrix4f().translate(new Vector3f(position));
	}

	public FloatBuffer generateMesh(MemoryStack stack) {
		vertCount = 36;
		FloatBuffer mesh = stack.mallocFloat(9 * vertCount);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(2f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(2f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(2f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(0f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(2f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(0f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(2f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(2f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(2f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(2f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(2f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(2f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(0f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(0f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(0f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(2f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(2f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(2f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(0f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(0f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(2f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(0f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(2f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(2f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(2f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(2f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.flip();
		return mesh;
	}
}
