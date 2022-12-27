package faketorio;

import org.joml.Vector2i;
import org.joml.Vector3f;

public class Tile {
	int type = 0;
	String name = "tile";
	boolean free = true;
	Inventory inventory = new Inventory();

	Vector2i tilePos = new Vector2i();
	Vector3f color = new Vector3f(-1f);
	Vector3f tint = new Vector3f(-1f);
}
