package faketorio;

import java.nio.FloatBuffer;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class World extends Entity {

	public void update() {

	}

	public FloatBuffer generateMesh(MemoryStack stack) {
		Vector2i worldSize = new Vector2i(100, 100);
		vertCount = 6 * worldSize.x * worldSize.y;
		Vector3f color = new Vector3f(0.1f, 0.1f, 0.1f);
		FloatBuffer mesh = stack.mallocFloat(9 * vertCount);
		for (int x=0;x<worldSize.x;x++) {
			for (int y=0;y<worldSize.y;y++) {
				if ((x + y % 2) % 2 == 0) {
					color = new Vector3f(0.1f, 0.1f, 0.1f);
				} else {
					color = new Vector3f(0.12f, 0.12f, 0.12f);
				}
				mesh.put(x  -worldSize.x/2).put(y+1-worldSize.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
				mesh.put(x  -worldSize.x/2).put(y  -worldSize.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
				mesh.put(x+1-worldSize.x/2).put(y  -worldSize.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
				mesh.put(x+1-worldSize.x/2).put(y  -worldSize.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
				mesh.put(x+1-worldSize.x/2).put(y+1-worldSize.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
				mesh.put(x  -worldSize.x/2).put(y+1-worldSize.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
			}
		}
		mesh.flip();
		return mesh;
	}
}
