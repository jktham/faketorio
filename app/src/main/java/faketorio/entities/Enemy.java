package faketorio.entities;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import faketorio.engine.App;
import faketorio.engine.Line;
import faketorio.entities.buildings.Building;

public class Enemy extends Entity {
	public Building target;
	public float speed;
	public float range;
	public float damage;
	public float rotation;
	public Line laser;
	
	public Enemy(Vector3f worldPos) {
		super(worldPos);
		model = App.resources.enemyModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.5f);
		model.color = new Vector3f(0.6f, 0.2f, 0.2f);
		name = "enemy";
		inventory.stackSize = 1000;
		inventory.inventorySize = 100;
		type = 100;
		label.hidden = false;
		speed = 0.03f;
		range = 1f;
		damage = 1f;

		ArrayList<Vector3f> verts = new ArrayList<Vector3f>();
		verts.add(new Vector3f(0.5f, 0.5f, 1.0f));
		verts.add(new Vector3f(0.5f, 0.5f, 0.5f));
		ArrayList<Vector3f> colors = new ArrayList<Vector3f>();
		colors.add(new Vector3f(0.9f, 0.3f, 0.3f));
		colors.add(new Vector3f(0.9f, 0.3f, 0.3f));
		laser = new Line(verts, colors);
		laser.transform.translate(worldPos);
	}

	public void update() {
		float min = 9999f;
		target = null;
		for (Building b : App.world.buildings) {
			if (b.type != 13 && new Vector3f(b.worldPos).sub(worldPos).length() < min) {
				min = new Vector3f(b.worldPos).sub(worldPos).length();
				target = b;
			}
		}

		if (target != null) {
			model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.5f).rotate(rotation, 0f, 0f, 1f).scale((float)health/(float)maxHealth);
			laser.transform = new Matrix4f().translate(worldPos);

			Vector3f dir = new Vector3f(target.worldPos).sub(worldPos).normalize().mul(speed);
			if (new Vector3f(target.worldPos).sub(worldPos).length() > range) {
				worldPos.add(dir);
			}

			if (new Vector3f(target.worldPos).sub(worldPos).length() <= range) {
				laser.verts.set(1, new Vector3f(target.worldPos).sub(worldPos).add(0.5f, 0.5f, 0.5f));
				laser.update();

				target.health -= damage;
				if (target.health <= 0) {
					App.world.destroy(target);
					target = null;
				}
				rotation += 0.1f;
			} else {
				laser.verts.set(1, new Vector3f(0.5f, 0.5f, 0.5f));
				laser.update();
			}
		} else {
			laser.verts.set(1, new Vector3f(0.5f, 0.5f, 0.5f));
			laser.update();
		}
	}

	public void draw() {
		model.draw();
		laser.draw();
	}
}
