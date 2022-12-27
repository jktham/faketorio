package faketorio.world;

import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.inventory.Inventory;

public class Tile {
	public int type = 0;
	public String name = "tile";
	public boolean free = true;
	public Inventory inventory = new Inventory();

	public Vector2i tilePos = new Vector2i();
	public Vector3f color = new Vector3f(-1f);
	public Vector3f tint = new Vector3f(-1f);
}
