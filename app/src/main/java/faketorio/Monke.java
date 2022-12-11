package faketorio;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class Monke extends Entity {
	public FloatBuffer generateMesh(MemoryStack stack) {
		ArrayList<ArrayList<Float>> verts = App.monkeVerts;
		
		vertCount = verts.size();
		FloatBuffer mesh = stack.mallocFloat(9 * verts.size());
		for (int i=0;i<verts.size();i++) {
			for (int j=0;j<9;j++) {
				mesh.put(verts.get(i).get(j));
			}
		}
		mesh.flip();
		return mesh;
	}
	
	public void update() {
		model = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.5f);
		model.rotate((float)Math.PI / 2f, new Vector3f(1f, 0f, 0f));
		instanceUpdate();
	}
}
