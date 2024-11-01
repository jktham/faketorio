package faketorio.entities.buildings;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.engine.App;
import faketorio.inventory.ItemStack;

public class Extractor extends Building {
	Building input;

	public Extractor(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.extractorModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.05f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
		model.meshColors.set(6, new Vector3f(0.4f, 0.4f, 0.4f));
		name = "extractor";
		inventory.stackSize = 0;
		inventory.inventorySize = 0;
		type = 5;
	}

	public void update() {
		input = App.world.getBuilding(getInputTilePos());
		output = App.world.getBuilding(getOutputTilePos());
		updateBorders();

		if (ghost) {
			return;
		}

		if (colorResetTicks > 0) {
			colorResetTicks -= 1;
		} else {
			model.meshColors.set(5, new Vector3f(0.4f, 0.4f, 0.4f));
			model.meshColors.set(6, new Vector3f(0.4f, 0.4f, 0.4f));
		}
		
		if (sleepTicks > 0) {
			sleepTicks -= 1;
			return;
		}

		if (input != null && output != null) {
			for (ItemStack stack : input.inventory.stacks) {
				if (stack.amount > 0) {
					if (output.canBeAdded(stack.item.id, 1, this)) {
						input.removeItem(stack.item.id, 1);
						output.addItem(stack.item.id, 1, this);
						sleepTicks = 6-1;
						model.meshColors.set(5, stack.item.color);
						model.meshColors.set(6, stack.item.color);
						colorResetTicks = 6-1;
						break;
					}
				}
			}
		}
	}
	
	public Vector2i getOutputTilePos() {
		Vector2i outputTilePos = null;
		if (rotation == 0) {
			outputTilePos = new Vector2i(tilePos).add(0, -1);
		} else if (rotation == 1) {
			outputTilePos = new Vector2i(tilePos).add(1, 0);
		} else if (rotation == 2) {
			outputTilePos = new Vector2i(tilePos).add(0, 1);
		} else if (rotation == 3) {
			outputTilePos = new Vector2i(tilePos).add(-1, 0);
		}
		return outputTilePos;
	}
	public Vector2i getInputTilePos() {
		Vector2i outputTilePos = null;
		if (rotation == 0) {
			outputTilePos = new Vector2i(tilePos).add(0, 1);
		} else if (rotation == 1) {
			outputTilePos = new Vector2i(tilePos).add(-1, 0);
		} else if (rotation == 2) {
			outputTilePos = new Vector2i(tilePos).add(0, -1);
		} else if (rotation == 3) {
			outputTilePos = new Vector2i(tilePos).add(1, 0);
		}
		return outputTilePos;
	}
}
