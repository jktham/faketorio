package faketorio;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class Cube extends Entity {
	Vector3f color = new Vector3f();

	public FloatBuffer generateMesh(MemoryStack stack) {
		vertCount = 36;
		FloatBuffer mesh = stack.mallocFloat(9 * vertCount);
		mesh.put(0f).put(0f).put(0f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(0f).put(1f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(1f).put(1f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(1f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(0f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(1f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(0f).put(1f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(0f).put(0f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(0f).put(0f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(1f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(0f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(0f).put(0f).put(0f).put(0f).put(-1f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(0f).put(0f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(1f).put(1f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(1f).put(0f).put(-1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(0f).put(1f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(0f).put(1f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(0f).put(0f).put(0f).put(-1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(1f).put(1f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(0f).put(1f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(0f).put(1f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(1f).put(1f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(0f).put(0f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(1f).put(0f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(0f).put(0f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(1f).put(1f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(0f).put(1f).put(1f).put(0f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(1f).put(1f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(1f).put(0f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(1f).put(0f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(1f).put(1f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(1f).put(0f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(1f).put(1f).put(0f).put(1f).put(0f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(1f).put(1f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.put(0f).put(1f).put(1f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.put(1f).put(0f).put(1f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
		mesh.flip();
		return mesh;
	}
}
