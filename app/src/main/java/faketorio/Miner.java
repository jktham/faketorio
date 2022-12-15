package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Miner extends Entity {
	
	public Miner(Vector3f position, int rotation) {
		super(position, rotation);
		model = App.resources.minerModel.copy();
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.5f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.3f, 0.3f, 0.3f);
		model.meshColors.set(0, new Vector3f(0.8f, 0.8f, 0.8f));
		name = "miner";
		stackSize = 10;
		inventorySize = 10;
		type = 1;
	}

	public void update() {
		model.meshTransforms.set(0, new Matrix4f().rotate(1.5f * App.time, 0f, 0f, 1f));

		if (ghost) {
			return;
		}

		model.meshColors.set(0, new Vector3f(0.8f, 0.8f, 0.8f));

		if (App.tick % 60 == 0) {
			Tile tile = App.world.getTile(App.world.worldToTilePos(position));
			if (tile.type >= 0) {
				for (ItemStack itemStack : tile.inventory) {
					if (itemStack.amount > 0) {
						if (canBeAdded(itemStack.item.id, 1)) {
							itemStack.amount -= 1;
							addItem(itemStack.item.id, 1);
							model.meshColors.set(0, itemStack.item.color);
						}
					}
				}
			}
		}

		Entity outputEntity = App.world.getEntity(getOutputTilePos());
		if (outputEntity != null) {
			for (ItemStack itemStack : inventory) {
				if (itemStack.amount > 0) {
					if (outputEntity.canBeAdded(itemStack.item.id, 1)) {
						removeItem(itemStack.item.id, 1);
						outputEntity.addItem(itemStack.item.id, 1);
						break;
					}
				}
			}
		}
	}

}
