package faketorio;

import java.util.ArrayList;

import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL33.*;

public class Ui {
	ArrayList<Element> elements;
	
	Matrix4f view = new Matrix4f();
	Matrix4f projection = new Matrix4f().ortho(0f, App.width, App.height, 0, 1f, -1f);

	public void init() {
		elements = new ArrayList<Element>();
	}

	public void update() {
		projection = new Matrix4f().ortho(0f, App.width, App.height, 0, 1f, -1f);

		for (Element element : elements) {
			element.update();
		}
	}

	public void draw() {
		glDisable(GL_DEPTH_TEST);
		for (Element element : elements) {
			element.draw();
		}
		glEnable(GL_DEPTH_TEST);
	}

	public void addElement(Element element) {
		elements.add(element);
	}

}
