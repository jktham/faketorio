package faketorio;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class Miner extends Entity {
	int lastTick = 0;
	
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

	public void update() {
		instanceUpdate();
		model = new Matrix4f().translate(position).scale(new Vector3f(1f, 1f, 0.5f));
		if (App.tick != lastTick) {
			Tile tile = App.world.getTile(App.world.worldToTilePos(position));
			if (!tile.free && tile.type >= 0) {
				for (ItemStack itemStack : tile.inventory) {
					if (itemStack.amount > 0) {
						itemStack.amount -= 1;
						addItem(itemStack.item.id, 1);
					}
				}
			}
			lastTick = App.tick;
		}
	}
}
