package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Belt extends Building {

	public Belt(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.beltModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.05f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshHidden.set(1, true);
		model.meshHidden.set(2, true);
		model.meshHidden.set(3, true);
		model.meshHidden.set(4, true);
		model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
		name = "belt";
		inventory.stackSize = 1;
		inventory.inventorySize = 1;
		type = 3;
	}

	public void update() {
		output = App.world.getBuilding(getOutputTilePos());
		updateBorders();

		if (ghost) {
			return;
		}
		
		if (sleepTicks > 0) {
			sleepTicks -= 1;
			return;
		}

		boolean empty = true;
		for (ItemStack stack : inventory.stacks) {
			if (stack.amount > 0) {
				empty = false;
			}
		}
		if (empty) {
			model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
		} else {
			for (ItemStack stack : inventory.stacks) {
				model.meshColors.set(5, stack.item.color);
			}
		}

		if (App.tick % 12 == 0) {
			if (output != null) {
				for (ItemStack stack : inventory.stacks) {
					if (stack.amount > 0) {
						if (output.canBeAdded(stack.item.id, 1, this)) {
							removeItem(stack.item.id, 1);
							output.addItem(stack.item.id, 1, this);
							break;
						}
					}
				}
			}
		}
	}

}
