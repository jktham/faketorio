package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Belt extends Entity {
	int tickCounter = 0;

	public Belt(Vector3f position, int rotation) {
		super(position, rotation);
		model = App.resources.beltModel.copy();
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.1f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		name = "belt";
		stackSize = 1;
		type = 3;
	}

	public void update() {
		boolean empty = true;
		for (ItemStack itemStack : inventory) {
			if (itemStack.amount > 0) {
				empty = false;
			}
		}
		if (empty) {
			tickCounter = 15;
			model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		} else {
			model.color = new Vector3f(0.4f, 0.4f, 0.4f);
		}

		tickCounter += 1;
		if (tickCounter >= 15) {
			tickCounter = 0;

			Entity outputEntity = App.world.getEntity(getOutputTilePos());
			if (outputEntity != null) {
				for (ItemStack itemStack : inventory) {
					if (itemStack.amount > 0) {
						if (outputEntity.canBeAdded(itemStack.item.id, 1)) {
							itemStack.amount -= 1;
							outputEntity.addItem(itemStack.item.id, 1);
						}
					}
				}
			}
		}
	}
}
