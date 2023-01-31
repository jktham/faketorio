package faketorio.entities.buildings;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.engine.App;
import faketorio.entities.Entity;

public class Turret extends Building {
	public Entity target;
	public float range;
	public float damage;
	public float rotation;
	
	public Turret(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.turretModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.5f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(0, new Vector3f(0.2f, 0.2f, 0.5f));
		name = "turret";
		inventory.stackSize = 1000;
		inventory.inventorySize = 100;
		type = 12;
		label.hidden = false;
		maxHealth = 1000;
		health = maxHealth;
		range = 8f;
		damage = 1f;
	}

	public void update() {
		if (ghost) {
			return;
		}

		if (App.tick % 12 == 0) {
			float min = 9999f;
			target = null;
			for (Entity e : App.world.entities) {
				if (e.type >= 100 && new Vector3f(e.worldPos).sub(worldPos).length() < min) {
					min = new Vector3f(e.worldPos).sub(worldPos).length();
					target = e;
				}
			}
		}

		if (target == null) {
			return;
		}

		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.5f).rotate(rotation, 0f, 0f, 1f);
		if (new Vector3f(target.worldPos).sub(worldPos).length() <= range) {
			target.health -= damage;
			if (target.health <= 0) {
				App.world.kill(target);
				target = null;
			}
			rotation += 0.1f;
		}
	}

	public Vector2i getOutputTilePos() {
		return new Vector2i(-99999);
	}
}
