package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Splitter extends Entity {
	boolean lastOutputLeft = false;

	public Splitter(Vector3f position, int rotation) {
		super(position, rotation);
		model = App.resources.splitterModel.copy();
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.05f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
		model.meshColors.set(6, new Vector3f(0.4f, 0.4f, 0.4f));
		name = "splitter";
		stackSize = 1;
		inventorySize = 1;
		type = 5;
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
				if (lastOutputLeft) {
					model.meshColors.set(5, itemStack.item.color);
					model.meshColors.set(6, new Vector3f(0.4f, 0.4f, 0.4f));
				} else {
					model.meshColors.set(6, itemStack.item.color);
					model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
				}
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
							lastOutputLeft = !lastOutputLeft;
							break;
						}
					}
				}
			}
		}
	}

	public Vector2i getOutputTilePos() {
		if (lastOutputLeft) {
			return getRightOutputTilePos();
		} else {
			return getLeftOutputTilePos();
		}
	}

	public Vector2i getLeftOutputTilePos() {
		Vector2i outputTilePos = null;
		if (rotation == 0) {
			outputTilePos = App.world.worldToTilePos(position).add(-1, 0);
		} else if (rotation == 1) {
			outputTilePos = App.world.worldToTilePos(position).add(0, -1);
		} else if (rotation == 2) {
			outputTilePos = App.world.worldToTilePos(position).add(1, 0);
		} else if (rotation == 3) {
			outputTilePos = App.world.worldToTilePos(position).add(0, 1);
		}
		return outputTilePos;
	}

	public Vector2i getRightOutputTilePos() {
		Vector2i outputTilePos = null;
		if (rotation == 0) {
			outputTilePos = App.world.worldToTilePos(position).add(1, 0);
		} else if (rotation == 1) {
			outputTilePos = App.world.worldToTilePos(position).add(0, 1);
		} else if (rotation == 2) {
			outputTilePos = App.world.worldToTilePos(position).add(-1, 0);
		} else if (rotation == 3) {
			outputTilePos = App.world.worldToTilePos(position).add(0, -1);
		}
		return outputTilePos;
	}
	public void updateBorders() {
		model.meshHidden.set(1, true);
		model.meshHidden.set(2, true);
		model.meshHidden.set(3, true);
		model.meshHidden.set(4, true);

		if (App.world.getEntity(getOutputTilePos()) != null && App.world.getEntity(getOutputTilePos()).canBeAdded(-1, 0, this)) {
			if (lastOutputLeft) {
				model.meshHidden.set(3, false);
			} else {
				model.meshHidden.set(1, false);
			}
		}
		if (App.world.ghost != null && getOutputTilePos().equals(App.world.worldToTilePos(App.world.ghost.position)) && App.world.ghost.canBeAdded(-1, 0, this)) {
			if (lastOutputLeft) {
				model.meshHidden.set(3, false);
			} else {
				model.meshHidden.set(1, false);
			}
		}

		if (App.world.getEntity(App.world.worldToTilePos(position).add(0, 1)) != null && App.world.getEntity(App.world.worldToTilePos(position).add(0, 1)).getOutputTilePos().equals(App.world.worldToTilePos(position))) {
			model.meshHidden.set(getBorderIndex(0), false);
		}
		if (App.world.getEntity(App.world.worldToTilePos(position).add(1, 0)) != null && App.world.getEntity(App.world.worldToTilePos(position).add(1, 0)).getOutputTilePos().equals(App.world.worldToTilePos(position))) {
			model.meshHidden.set(getBorderIndex(1), false);
		}
		if (App.world.getEntity(App.world.worldToTilePos(position).add(0, -1)) != null && App.world.getEntity(App.world.worldToTilePos(position).add(0, -1)).getOutputTilePos().equals(App.world.worldToTilePos(position))) {
			model.meshHidden.set(getBorderIndex(2), false);
		}
		if (App.world.getEntity(App.world.worldToTilePos(position).add(-1, 0)) != null && App.world.getEntity(App.world.worldToTilePos(position).add(-1, 0)).getOutputTilePos().equals(App.world.worldToTilePos(position))) {
			model.meshHidden.set(getBorderIndex(3), false);
		}
		if (App.world.ghost != null && App.world.worldToTilePos(App.world.ghost.position).equals(App.world.worldToTilePos(position).add(0, 1)) && App.world.ghost.getOutputTilePos().equals(App.world.worldToTilePos(position))) {
			model.meshHidden.set(getBorderIndex(0), false);
		}
		if (App.world.ghost != null && App.world.worldToTilePos(App.world.ghost.position).equals(App.world.worldToTilePos(position).add(1, 0)) && App.world.ghost.getOutputTilePos().equals(App.world.worldToTilePos(position))) {
			model.meshHidden.set(getBorderIndex(1), false);
		}
		if (App.world.ghost != null && App.world.worldToTilePos(App.world.ghost.position).equals(App.world.worldToTilePos(position).add(0, -1)) && App.world.ghost.getOutputTilePos().equals(App.world.worldToTilePos(position))) {
			model.meshHidden.set(getBorderIndex(2), false);
		}
		if (App.world.ghost != null && App.world.worldToTilePos(App.world.ghost.position).equals(App.world.worldToTilePos(position).add(-1, 0)) && App.world.ghost.getOutputTilePos().equals(App.world.worldToTilePos(position))) {
			model.meshHidden.set(getBorderIndex(3), false);
		}
	}
}
