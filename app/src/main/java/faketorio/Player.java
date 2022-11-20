package faketorio;

import org.joml.Vector3f;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.FloatBuffer;

public class Player extends Entity {
	float speed = 5.0f;
	Vector3f position = new Vector3f(0f, 0f, 0f);

	public void update() {
		float deltaMove = speed * App.deltaTime;
		if (glfwGetKey(App.window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
			deltaMove *= 4f;
		}
		if (glfwGetKey(App.window, GLFW_KEY_W) == GLFW_PRESS) {
			position.add(0f, deltaMove, 0f);
		}
		if (glfwGetKey(App.window, GLFW_KEY_S) == GLFW_PRESS) {
			position.add(0f, -deltaMove, 0f);
		}
		if (glfwGetKey(App.window, GLFW_KEY_A) == GLFW_PRESS) {
			position.add(-deltaMove, 0f, 0f);
		}
		if (glfwGetKey(App.window, GLFW_KEY_D) == GLFW_PRESS) {
			position.add(deltaMove, 0f, 0f);
		}
		model = new Matrix4f().translate(new Vector3f(position));
	}

	public FloatBuffer generateMesh(MemoryStack stack) {
		vertCount = 36;
		Vector3f color = new Vector3f(1f, 0f, 0f);
		FloatBuffer mesh = stack.mallocFloat(6 * vertCount);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put(-0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put( 0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put(-0.5f).put( 0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.put( 0.5f).put(-0.5f).put(2f).put(color.x).put(color.y).put(color.z);
		mesh.flip();
		return mesh;
	}
}