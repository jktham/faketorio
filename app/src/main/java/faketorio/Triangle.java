package faketorio;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class Triangle extends Entity {
	public FloatBuffer generateMesh(MemoryStack stack) {
		vertCount = 6;
		FloatBuffer mesh = stack.mallocFloat(3 * vertCount);
		mesh.put(-0.25f).put(0.433f).put(0f).put(1f).put(0f).put(0f);
		mesh.put(-0.25f).put(-0.433f).put(0f).put(0f).put(1f).put(0f);
		mesh.put(0.5f).put(0f).put(0f).put(0f).put(0f).put(1f);
		mesh.flip();
		return mesh;
	}

	public void update() {
		model.rotate(0.5f * App.deltaTime, new Vector3f(0f, 0f, 1f));
	}
}
