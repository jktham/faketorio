package faketorio.ui;

import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;

import org.joml.Matrix4f;

import faketorio.engine.App;

public class Ui {
	public ArrayList<Element> elements;
	
	public Matrix4f view = new Matrix4f();
	public Matrix4f projection = new Matrix4f().ortho(0f, App.width, App.height, 0, 1f, -1f);

	public boolean hidden = false;

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
		if (hidden) {
			return;
		}
		
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
