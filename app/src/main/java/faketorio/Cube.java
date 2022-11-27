package faketorio;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;

public class Cube extends Entity {

	public FloatBuffer generateMesh(MemoryStack stack) {
		vertCount = 36;
		FloatBuffer mesh = stack.mallocFloat(9 * vertCount);
		mesh.put(0f).put(0f).put(0f).put(-1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(0f).put(1f).put(-1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(1f).put(1f).put(-1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(1f).put(0f).put(0f).put(0f).put(-1f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(0f).put(0f).put(0f).put(0f).put(-1f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(1f).put(0f).put(0f).put(0f).put(-1f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(0f).put(1f).put(0f).put(-1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(0f).put(0f).put(0f).put(-1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(0f).put(0f).put(0f).put(-1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(1f).put(0f).put(0f).put(0f).put(-1f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(0f).put(0f).put(0f).put(0f).put(-1f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(0f).put(0f).put(0f).put(0f).put(-1f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(0f).put(0f).put(-1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(1f).put(1f).put(-1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(1f).put(0f).put(-1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(0f).put(1f).put(0f).put(-1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(0f).put(1f).put(0f).put(-1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(0f).put(0f).put(0f).put(-1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(1f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(0f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(0f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(1f).put(1f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(0f).put(0f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(1f).put(0f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(0f).put(0f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(1f).put(1f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(0f).put(1f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(1f).put(1f).put(0f).put(1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(1f).put(0f).put(0f).put(1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(1f).put(0f).put(0f).put(1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(1f).put(1f).put(0f).put(1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(1f).put(0f).put(0f).put(1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(1f).put(1f).put(0f).put(1f).put(0f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(1f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(1f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(0f).put(1f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.flip();
		return mesh;
	}
}
