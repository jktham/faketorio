package faketorio.inventory;

import org.joml.Vector3f;

public class Item {
	public int id;
	public String name;
	public Vector3f color;

	public Item() {
		id = 0;
		name = "";
		color = new Vector3f();
	}

	public Item(int id, String name, Vector3f color) {
		this.id = id;
		this.name = name;
		this.color = color;
	}
}
