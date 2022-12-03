package faketorio;

import java.util.ArrayList;

import org.joml.Matrix4f;

public class Ui {
	ArrayList<Element> elements;
	
	Matrix4f view = new Matrix4f();
	Matrix4f projection = new Matrix4f().ortho(0f, App.width, App.height, 0, 1f, -1f);

	public void init() {
		elements = new ArrayList<Element>();
	}

	public void update() {
		projection = new Matrix4f().ortho(0f, App.width, App.height, 0, 1f, -1f);

		for (Element e : elements) {
			e.update();
		}
	}

	public void draw() {
		for (Element e : elements) {
			e.draw();
		}
	}


}
