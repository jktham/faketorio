package faketorio;

import org.joml.Matrix4f;

public class Miner extends Entity {
	int lastTick = 0;
	
	public Miner() {
		model = App.resources.minerModel.copy();
	}

	public void update() {
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.5f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.meshTransforms.set(0, new Matrix4f().rotate(1.5f * App.time, 0f, 0f, 1f));

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
