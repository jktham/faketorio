package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Thrower extends Entity {
	int range = 5;

	public Thrower(Vector3f position, int rotation) {
		super(position, rotation);
		model = App.resources.throwerModel.copy();
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.05f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
		model.meshColors.set(6, new Vector3f(0.4f, 0.4f, 0.4f));
		name = "thrower";
		stackSize = 1;
		inventorySize = 1;
		type = 4;
	}

	public void update() {
		updateBorders();

		if (ghost) {
			return;
		}
		
		if (sleepTicks > 0) {
			sleepTicks -= 1;
			return;
		}

		boolean empty = true;
		for (ItemStack itemStack : inventory) {
			if (itemStack.amount > 0) {
				empty = false;
			}
		}
		if (empty) {
			model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
			model.meshColors.set(6, new Vector3f(0.4f, 0.4f, 0.4f));
		} else {
			for (ItemStack itemStack : inventory) {
				model.meshColors.set(5, itemStack.item.color);
				model.meshColors.set(6, itemStack.item.color);
			}
		}

		if (App.tick % 12 == 0) {
			Entity outputEntity = App.world.getEntity(getOutputTilePos());
			if (outputEntity != null) {
				for (ItemStack itemStack : inventory) {
					if (itemStack.amount > 0) {
						if (outputEntity.canBeAdded(itemStack.item.id, 1, this)) {
							removeItem(itemStack.item.id, 1);
							outputEntity.addItem(itemStack.item.id, 1, this);
							break;
						}
					}
				}
			}
		}
	}
	
	public Vector2i getOutputTilePos() {
		Vector2i outputTilePos = null;
		if (rotation == 0) {
			outputTilePos = App.world.worldToTilePos(position).add(0, -1 * range);
		} else if (rotation == 1) {
			outputTilePos = App.world.worldToTilePos(position).add(1 * range, 0);
		} else if (rotation == 2) {
			outputTilePos = App.world.worldToTilePos(position).add(0, 1 * range);
		} else if (rotation == 3) {
			outputTilePos = App.world.worldToTilePos(position).add(-1 * range, 0);
		}
		return outputTilePos;
	}

}
