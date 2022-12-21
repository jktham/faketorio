package faketorio;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Merger extends Entity {
	boolean lastInputLeft = false;
	boolean firstInput = true;

	public Merger(Vector3f position, int rotation) {
		super(position, rotation);
		model = App.resources.mergerModel.copy();
		model.transform = new Matrix4f().translate(position).translate(0.5f, 0.5f, 0.05f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
		model.meshColors.set(6, new Vector3f(0.4f, 0.4f, 0.4f));
		model.meshColors.set(7, new Vector3f(0.4f, 0.4f, 0.4f));
		name = "merger";
		stackSize = 1;
		inventorySize = 1;
		type = 6;
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
			model.meshColors.set(7, new Vector3f(0.4f, 0.4f, 0.4f));
		} else {
			for (ItemStack itemStack : inventory) {
				model.meshColors.set(7, itemStack.item.color);
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

	public boolean canBeAdded(int id, int amount, Entity source) {
		if (!firstInput) {
			if (lastInputLeft) {
				if (!isRightInputDirection(source)) {
					return false;
				}
			} else {
				if (!isLeftInputDirection(source)) {
					return false;
				}
			}
		}
		// only amount 1 supported
		if (id == -1) {
			return true;
		}
		int stackCount = 0;
		for (ItemStack itemStack : inventory) {
			if (itemStack.item.id != id || itemStack.amount >= stackSize) {
				stackCount += 1;
			}
		}
		if (stackCount >= inventorySize) {
			return false;
		}

		boolean found = false;
		for (ItemStack itemStack : inventory) {
			if (itemStack.item.id == id) {
				found = true;
				if (itemStack.amount < stackSize) {
					return true;
				}
			}
		}
		return !found;
	}

	public void addItem(int id, int amount, Entity source) {
		if (firstInput) {
			firstInput = false;
		}
		if (isLeftInputDirection(source)) {
			lastInputLeft = true;
		} else {
			lastInputLeft = false;
		}
		boolean found = false;
		for (ItemStack itemStack : inventory) {
			if (itemStack.item.id == id) {
				itemStack.amount += amount;
				found = true;
			}
		}
		if (!found) {
			ItemStack itemStack = new ItemStack();
			itemStack.item = App.items.get(id);
			itemStack.amount = amount;
			inventory.add(itemStack);
		}
		sleepTicks = 1;
	}

	public void updateBorders() {
		model.meshHidden.set(1, true);
		model.meshHidden.set(2, true);
		model.meshHidden.set(3, true);
		model.meshHidden.set(4, true);

		if (App.world.getEntity(getOutputTilePos()) != null && App.world.getEntity(getOutputTilePos()).canBeAdded(-1, 0, this)) {
			model.meshHidden.set(4, false);
		}
		if (App.world.ghost != null && getOutputTilePos().equals(App.world.worldToTilePos(App.world.ghost.position)) && App.world.ghost.canBeAdded(-1, 0, this)) {
			model.meshHidden.set(4, false);
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

		if (!firstInput) {
			if (lastInputLeft) {
				model.meshHidden.set(3, true);
			} else {
				model.meshHidden.set(1, true);
			}
		}
		model.meshHidden.set(2, true);
	}

	public boolean isLeftInputDirection(Entity source) {
		Vector2i inputDir = App.world.worldToTilePos(position).sub(App.world.worldToTilePos(source.position));
		if (rotation == 0) {
			if (inputDir.x < 0) {
				return true;
			} else {
				return false;
			}
		} else if (rotation == 1) {
			if (inputDir.y < 0) {
				return true;
			} else {
				return false;
			}
		} else if (rotation == 2) {
			if (inputDir.x > 0) {
				return true;
			} else {
				return false;
			}
		} else if (rotation == 3) {
			if (inputDir.y > 0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public boolean isRightInputDirection(Entity source) {
		Vector2i inputDir = App.world.worldToTilePos(position).sub(App.world.worldToTilePos(source.position));
		if (rotation == 0) {
			if (inputDir.x > 0) {
				return true;
			} else {
				return false;
			}
		} else if (rotation == 1) {
			if (inputDir.y > 0) {
				return true;
			} else {
				return false;
			}
		} else if (rotation == 2) {
			if (inputDir.x < 0) {
				return true;
			} else {
				return false;
			}
		} else if (rotation == 3) {
			if (inputDir.y < 0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
