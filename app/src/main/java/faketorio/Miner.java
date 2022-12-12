package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Miner extends Entity {
	int lastTick = 0;
	
	public Miner() {
		model = App.resources.minerModel.copy();
	}

	public void update() {
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.5f);
		model.transform.rotate(1.5f * App.time, new Vector3f(0f, 0f, 1f));

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
		instanceUpdate();
	}
}
