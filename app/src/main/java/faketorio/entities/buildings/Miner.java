package faketorio.entities.buildings;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.engine.App;
import faketorio.inventory.ItemStack;
import faketorio.world.Tile;

public class Miner extends Building {
	
	public Miner(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.minerModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.05f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(4, new Vector3f(0.8f, 0.8f, 0.8f));
		name = "miner";
		inventory.stackSize = 1;
		inventory.inventorySize = 10;
		type = 8;
		label.hidden = false;
	}

	public void update() {
		output = App.world.getBuilding(getOutputTilePos());

		model.meshTransforms.set(4, new Matrix4f().rotate(1.5f * App.time, 0f, 0f, 1f));

		if (ghost) {
			return;
		}

		if (App.tick % 6 == 0) {
			model.meshColors.set(4, new Vector3f(0.8f, 0.8f, 0.8f));
		}

		if (App.tick % 120 == 0) {
			Tile tile = App.world.getTile(tilePos);
			if (tile.type >= 0) {
				for (ItemStack stack : tile.inventory.stacks) {
					if (stack.amount > 0) {
						if (canBeAdded(stack.item.id, 1, this)) {
							stack.amount -= 1;
							addItem(stack.item.id, 1, this);
							sleepTicks = 0;
							model.meshColors.set(4, stack.item.color);
						}
					}
				}
			}
		}

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
