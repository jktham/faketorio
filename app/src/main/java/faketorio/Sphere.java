package faketorio;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class Sphere extends Entity {

	public void update() {
		model = new Matrix4f().translate(position).translate(0.5f, 0.5f, 1f);
		model.translate(0f, 0f, 0.25f * (float)Math.sin(App.time));
		model.rotate(-0.75f * App.time, new Vector3f(0f, 0f, 1f));
		model.scale(0.5f);
		instanceUpdate();
	}
	
	public FloatBuffer generateMesh(MemoryStack stack) {
		vertCount = 60;
		FloatBuffer mesh = stack.mallocFloat(9 * vertCount);
		mesh.put( 0.000000f).put( 0.000000f).put(-1.000000f).put( 0.5774f).put( 0.1876f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.525720f).put( 0.723600f).put(-0.447215f).put( 0.5774f).put( 0.1876f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.850640f).put(-0.276385f).put(-0.447215f).put( 0.5774f).put( 0.1876f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.525720f).put( 0.723600f).put(-0.447215f).put( 0.0000f).put( 0.6071f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.000000f).put(-1.000000f).put( 0.0000f).put( 0.6071f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put(-0.525720f).put( 0.723600f).put(-0.447215f).put( 0.0000f).put( 0.6071f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.000000f).put(-1.000000f).put( 0.3568f).put(-0.4911f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.850640f).put(-0.276385f).put(-0.447215f).put( 0.3568f).put(-0.4911f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put(-0.894425f).put(-0.447215f).put( 0.3568f).put(-0.4911f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.000000f).put(-1.000000f).put(-0.3568f).put(-0.4911f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put(-0.894425f).put(-0.447215f).put(-0.3568f).put(-0.4911f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put(-0.850640f).put(-0.276385f).put(-0.447215f).put(-0.3568f).put(-0.4911f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.000000f).put(-1.000000f).put(-0.5774f).put( 0.1876f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put(-0.850640f).put(-0.276385f).put(-0.447215f).put(-0.5774f).put( 0.1876f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put(-0.525720f).put( 0.723600f).put(-0.447215f).put(-0.5774f).put( 0.1876f).put(-0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.525720f).put( 0.723600f).put(-0.447215f).put( 0.0000f).put( 0.9822f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.525720f).put( 0.723600f).put(-0.447215f).put( 0.0000f).put( 0.9822f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.894425f).put( 0.447215f).put( 0.0000f).put( 0.9822f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.850640f).put(-0.276385f).put(-0.447215f).put( 0.9342f).put( 0.3035f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.525720f).put( 0.723600f).put(-0.447215f).put( 0.9342f).put( 0.3035f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.850640f).put( 0.276385f).put( 0.447215f).put( 0.9342f).put( 0.3035f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put(-0.894425f).put(-0.447215f).put( 0.5774f).put(-0.7946f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.850640f).put(-0.276385f).put(-0.447215f).put( 0.5774f).put(-0.7946f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.525720f).put(-0.723600f).put( 0.447215f).put( 0.5774f).put(-0.7946f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.850640f).put(-0.276385f).put(-0.447215f).put(-0.5774f).put(-0.7946f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put(-0.894425f).put(-0.447215f).put(-0.5774f).put(-0.7946f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.525720f).put(-0.723600f).put( 0.447215f).put(-0.5774f).put(-0.7946f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.525720f).put( 0.723600f).put(-0.447215f).put(-0.9342f).put( 0.3035f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.850640f).put(-0.276385f).put(-0.447215f).put(-0.9342f).put( 0.3035f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.850640f).put( 0.276385f).put( 0.447215f).put(-0.9342f).put( 0.3035f).put(-0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.525720f).put( 0.723600f).put(-0.447215f).put( 0.5774f).put( 0.7946f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.894425f).put( 0.447215f).put( 0.5774f).put( 0.7946f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.850640f).put( 0.276385f).put( 0.447215f).put( 0.5774f).put( 0.7946f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.850640f).put(-0.276385f).put(-0.447215f).put( 0.9342f).put(-0.3035f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.850640f).put( 0.276385f).put( 0.447215f).put( 0.9342f).put(-0.3035f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.525720f).put(-0.723600f).put( 0.447215f).put( 0.9342f).put(-0.3035f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put(-0.894425f).put(-0.447215f).put( 0.0000f).put(-0.9822f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.525720f).put(-0.723600f).put( 0.447215f).put( 0.0000f).put(-0.9822f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.525720f).put(-0.723600f).put( 0.447215f).put( 0.0000f).put(-0.9822f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.850640f).put(-0.276385f).put(-0.447215f).put(-0.9342f).put(-0.3035f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.525720f).put(-0.723600f).put( 0.447215f).put(-0.9342f).put(-0.3035f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.850640f).put( 0.276385f).put( 0.447215f).put(-0.9342f).put(-0.3035f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.525720f).put( 0.723600f).put(-0.447215f).put(-0.5774f).put( 0.7946f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put(-0.850640f).put( 0.276385f).put( 0.447215f).put(-0.5774f).put( 0.7946f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.894425f).put( 0.447215f).put(-0.5774f).put( 0.7946f).put( 0.1876f).put(1f).put(1f).put(1f);
		mesh.put( 0.850640f).put( 0.276385f).put( 0.447215f).put( 0.3568f).put( 0.4911f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.894425f).put( 0.447215f).put( 0.3568f).put( 0.4911f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.000000f).put( 1.000000f).put( 0.3568f).put( 0.4911f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.525720f).put(-0.723600f).put( 0.447215f).put( 0.5774f).put(-0.1876f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.850640f).put( 0.276385f).put( 0.447215f).put( 0.5774f).put(-0.1876f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.000000f).put( 1.000000f).put( 0.5774f).put(-0.1876f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put(-0.525720f).put(-0.723600f).put( 0.447215f).put( 0.0000f).put(-0.6071f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.525720f).put(-0.723600f).put( 0.447215f).put( 0.0000f).put(-0.6071f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.000000f).put( 1.000000f).put( 0.0000f).put(-0.6071f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put(-0.850640f).put( 0.276385f).put( 0.447215f).put(-0.5774f).put(-0.1876f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put(-0.525720f).put(-0.723600f).put( 0.447215f).put(-0.5774f).put(-0.1876f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.000000f).put( 1.000000f).put(-0.5774f).put(-0.1876f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.894425f).put( 0.447215f).put(-0.3568f).put( 0.4911f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put(-0.850640f).put( 0.276385f).put( 0.447215f).put(-0.3568f).put( 0.4911f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.put( 0.000000f).put( 0.000000f).put( 1.000000f).put(-0.3568f).put( 0.4911f).put( 0.7947f).put(1f).put(1f).put(1f);
		mesh.flip();
		return mesh;
	}
}
