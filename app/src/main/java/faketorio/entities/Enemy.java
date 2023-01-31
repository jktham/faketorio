package faketorio.entities;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import faketorio.engine.App;
import faketorio.entities.buildings.Building;

public class Enemy extends Entity {
	public Building target;
	public float speed;
	public float range;
	public float damage;
	public float rotation;
	
	public Enemy(Vector3f worldPos) {
		super(worldPos);
		model = App.resources.enemyModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.5f);
		model.color = new Vector3f(0.2f, 0.2f, 0.2f);
		model.meshColors.set(0, new Vector3f(0.6f, 0.2f, 0.2f));
		name = "enemy";
		inventory.stackSize = 1000;
		inventory.inventorySize = 100;
		type = 100;
		label.hidden = false;
		speed = 0.02f;
		range = 1f;
		damage = 1f;
	}

	public void update() {
		if (App.tick % 12 == 0) {
			float min = 9999f;
			target = null;
			for (Building b : App.world.buildings) {
				if (b.type != 13 && new Vector3f(b.worldPos).sub(worldPos).length() < min) {
					min = new Vector3f(b.worldPos).sub(worldPos).length();
					target = b;
				}
			}
		}

		if (target == null) {
			return;
		}

		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.5f).rotate(rotation, 0f, 0f, 1f);
		Vector3f dir = new Vector3f(target.worldPos).sub(worldPos).normalize().mul(speed);
		if (new Vector3f(target.worldPos).sub(worldPos).length() > range) {
			worldPos.add(dir);
		}

		if (new Vector3f(target.worldPos).sub(worldPos).length() <= range) {
			target.health -= damage;
			if (target.health <= 0) {
				App.world.destroy(target);
				target = null;
			}
			rotation += 0.1f;
		}
	}
}
