package faketorio.entities.buildings;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.engine.App;
import faketorio.engine.Line;
import faketorio.entities.Entity;

public class Turret extends Building {
	public Entity target;
	public float range;
	public float damage;
	public float rotation;
	public Line laser;
	
	public Turret(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.turretModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.5f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0.4f, 0.4f, 0.4f);
		model.meshColors.set(0, new Vector3f(0.2f, 0.2f, 0.5f));
		model.meshColors.set(1, new Vector3f(0.2f, 0.2f, 0.6f));
		name = "turret";
		inventory.stackSize = 1000;
		inventory.inventorySize = 100;
		type = 12;
		label.hidden = false;
		maxHealth = 1000;
		health = maxHealth;
		range = 8f;
		damage = 1f;

		ArrayList<Vector3f> verts = new ArrayList<Vector3f>();
		verts.add(new Vector3f(0.5f, 0.5f, 1.0f));
		verts.add(new Vector3f(0.5f, 0.5f, 0.5f));
		ArrayList<Vector3f> colors = new ArrayList<Vector3f>();
		colors.add(new Vector3f(0.3f, 0.3f, 0.9f));
		colors.add(new Vector3f(0.3f, 0.3f, 0.9f));
		laser = new Line(verts, colors);
		laser.transform.translate(worldPos);
	}

	public void update() {
		if (ghost) {
			return;
		}

		float min = 9999f;
		target = null;
		for (Entity e : App.world.entities) {
			if (e.type >= 100 && new Vector3f(e.worldPos).sub(worldPos).length() < min) {
				min = new Vector3f(e.worldPos).sub(worldPos).length();
				target = e;
			}
		}

		if (target != null) {
			model.meshTransforms.set(1, new Matrix4f().rotate(rotation, 0f, 0f, 1f));

			if (new Vector3f(target.worldPos).sub(worldPos).length() <= range) {
				laser.verts.set(1, new Vector3f(target.worldPos).sub(worldPos).add(0.5f, 0.5f, 0.5f));
				laser.update();

				target.health -= damage;
				if (target.health <= 0) {
					App.world.kill(target);
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

	public Vector2i getOutputTilePos() {
		return new Vector2i(-99999);
	}
	
	public boolean canBeAdded(int id, int amount, Building source) {
		return false;
	}
}
