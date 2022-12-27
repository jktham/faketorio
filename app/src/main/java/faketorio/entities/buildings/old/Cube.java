package faketorio.entities.buildings.old;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.engine.App;
import faketorio.entities.buildings.Building;

public class Cube extends Building {

	public Cube(Vector2i tilePos, int rotation) {
		super(tilePos, rotation);
		model = App.resources.cubeModel.copy();
		model.transform = new Matrix4f().translate(worldPos).translate(0.5f, 0.5f, 0.5f).rotate((float)Math.PI / 2f * rotation, 0f, 0f, 1f);
		model.color = new Vector3f(0f, 0f, 1f);
		name = "cube";
		type = 99;
	}

}
