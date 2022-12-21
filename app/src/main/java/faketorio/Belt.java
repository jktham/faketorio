package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Belt extends Entity {

	public Belt(Vector3f position, int rotation) {
		super(position, rotation);
		model = App.resources.beltModel.copy();
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.05f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshHidden.set(1, true);
		model.meshHidden.set(2, true);
		model.meshHidden.set(3, true);
		model.meshHidden.set(4, true);
		model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
		name = "belt";
		stackSize = 1;
		inventorySize = 1;
		type = 3;
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
		} else {
			for (ItemStack itemStack : inventory) {
				model.meshColors.set(5, itemStack.item.color);
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

}
