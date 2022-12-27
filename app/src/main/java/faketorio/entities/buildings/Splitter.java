package faketorio.entities.buildings;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.engine.App;
import faketorio.inventory.ItemStack;

public class Splitter extends Building {
	boolean lastOutputLeft = false;

	public Splitter(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.splitterModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.05f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
		model.meshColors.set(6, new Vector3f(0.4f, 0.4f, 0.4f));
		name = "splitter";
		inventory.stackSize = 1;
		inventory.inventorySize = 1;
		type = 5;
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
			model.meshColors.set(6, new Vector3f(0.4f, 0.4f, 0.4f));
		} else {
			for (ItemStack stack : inventory.stacks) {
				if (lastOutputLeft) {
					model.meshColors.set(5, stack.item.color);
					model.meshColors.set(6, new Vector3f(0.4f, 0.4f, 0.4f));
				} else {
					model.meshColors.set(6, stack.item.color);
					model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
				}
			}
		}
		
		if (App.tick % 12 == 0) {
			if (output != null) {
				for (ItemStack stack : inventory.stacks) {
					if (stack.amount > 0) {
						if (output.canBeAdded(stack.item.id, 1, this)) {
							removeItem(stack.item.id, 1);
							output.addItem(stack.item.id, 1, this);
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
			Vector2i outputTilePos = null;
			if (rotation == 0) {
				outputTilePos = new Vector2i(tilePos).add(1, 0);
			} else if (rotation == 1) {
				outputTilePos = new Vector2i(tilePos).add(0, 1);
			} else if (rotation == 2) {
				outputTilePos = new Vector2i(tilePos).add(-1, 0);
			} else if (rotation == 3) {
				outputTilePos = new Vector2i(tilePos).add(0, -1);
			}
			return outputTilePos;
		} else {
			Vector2i outputTilePos = null;
			if (rotation == 0) {
				outputTilePos = new Vector2i(tilePos).add(-1, 0);
			} else if (rotation == 1) {
				outputTilePos = new Vector2i(tilePos).add(0, -1);
			} else if (rotation == 2) {
				outputTilePos = new Vector2i(tilePos).add(1, 0);
			} else if (rotation == 3) {
				outputTilePos = new Vector2i(tilePos).add(0, 1);
			}
			return outputTilePos;
		}
	}

	public void updateBorders() {
		model.meshHidden.set(1, true);
		model.meshHidden.set(2, true);
		model.meshHidden.set(3, true);
		model.meshHidden.set(4, true);

		if (output != null && output.canBeAdded(-1, 0, this)) {
			if (lastOutputLeft) {
				model.meshHidden.set(3, false);
			} else {
				model.meshHidden.set(1, false);
			}
		}
		if (App.world.ghost != null && getOutputTilePos().equals(App.world.ghost.tilePos) && App.world.ghost.canBeAdded(-1, 0, this)) {
			if (lastOutputLeft) {
				model.meshHidden.set(3, false);
			} else {
				model.meshHidden.set(1, false);
			}
		}

		if (App.world.getBuilding(new Vector2i(tilePos).add(0, 1)) != null && App.world.getBuilding(new Vector2i(tilePos).add(0, 1)).getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(0), false);
		}
		if (App.world.getBuilding(new Vector2i(tilePos).add(1, 0)) != null && App.world.getBuilding(new Vector2i(tilePos).add(1, 0)).getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(1), false);
		}
		if (App.world.getBuilding(new Vector2i(tilePos).add(0, -1)) != null && App.world.getBuilding(new Vector2i(tilePos).add(0, -1)).getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(2), false);
		}
		if (App.world.getBuilding(new Vector2i(tilePos).add(-1, 0)) != null && App.world.getBuilding(new Vector2i(tilePos).add(-1, 0)).getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(3), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(0, 1)) && App.world.ghost.getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(0), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(1, 0)) && App.world.ghost.getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(1), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(0, -1)) && App.world.ghost.getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(2), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(-1, 0)) && App.world.ghost.getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(3), false);
		}
	}
}
