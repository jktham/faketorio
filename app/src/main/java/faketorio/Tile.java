package faketorio;

import java.util.ArrayList;

import org.joml.Vector2i;
import org.joml.Vector3f;

public class Tile {
	int type = 0;
	String name = "";
	boolean free = true;
	ArrayList<ItemStack> inventory = new ArrayList<ItemStack>();

	Vector2i position = new Vector2i();
	Vector3f color = new Vector3f(-1f);
	Vector3f tint = new Vector3f(-1f);
}
