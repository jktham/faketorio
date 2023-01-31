package faketorio.entities.buildings;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.engine.App;
import faketorio.entities.Enemy;

public class Spawner extends Building {
	
	public Spawner(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.spawnerModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(0, new Vector3f(0.5f, 0.2f, 0.2f));
		name = "spawner";
		inventory.stackSize = 1000;
		inventory.inventorySize = 100;
		type = 13;
		label.hidden = false;
	}

	public void update() {
		if (ghost) {
			return;
		}

		if (sleepTicks > 0) {
			sleepTicks -= 1;
			return;
		}

		App.world.entities.add(new Enemy(worldPos));
		sleepTicks += 299;
	}

	public Vector2i getOutputTilePos() {
		return new Vector2i(-99999);
	}
}
