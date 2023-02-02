package faketorio.engine;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class Line {
	public int shader;
	public int vao;
	public int vbo;

	public ArrayList<Vector3f> verts;
	public ArrayList<Vector3f> colors;
	public Matrix4f transform;
	
	public Line() {
		verts = new ArrayList<Vector3f>();
		colors = new ArrayList<Vector3f>();
		transform = new Matrix4f();
	}
	
	public Line(ArrayList<Vector3f> verts, ArrayList<Vector3f> colors) {
		this();

		this.verts = verts;
		this.colors = colors;

		this.shader = App.resources.worldLineShader;
		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			
			FloatBuffer mesh = stack.mallocFloat(9 * verts.size());
			for (int i=0;i<verts.size();i++) {
				mesh.put(verts.get(i).x).put(verts.get(i).y).put(verts.get(i).z).put(colors.get(i).x).put(colors.get(i).y).put(colors.get(i).z);
			}
			mesh.flip();

			glBufferData(GL_ARRAY_BUFFER, mesh, GL_STATIC_DRAW);
		}

		int floatSize = 4;
		int aPosition = glGetAttribLocation(shader, "aPosition");
		glEnableVertexAttribArray(aPosition);
		glVertexAttribPointer(aPosition, 3, GL_FLOAT, false, 6 * floatSize, 0);

		int aColor = glGetAttribLocation(shader, "aColor");
		glEnableVertexAttribArray(aColor);
		glVertexAttribPointer(aColor, 3, GL_FLOAT, false, 6 * floatSize, 3 * floatSize);

		glBindVertexArray(0);
	}

	public void update() {
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			
			FloatBuffer mesh = stack.mallocFloat(9 * verts.size());
			for (int i=0;i<verts.size();i++) {
				mesh.put(verts.get(i).x).put(verts.get(i).y).put(verts.get(i).z).put(colors.get(i).x).put(colors.get(i).y).put(colors.get(i).z);
			}
			mesh.flip();

			glBufferData(GL_ARRAY_BUFFER, mesh, GL_STATIC_DRAW);
		}
		
		glBindVertexArray(0);
	}

	public void draw() {
		if (glGetInteger(GL_CURRENT_PROGRAM) != shader) {
			glUseProgram(shader);
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniformMatrix4fv(glGetUniformLocation(shader, "uModel"), false, new Matrix4f(transform).get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "uView"), false, App.camera.view.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "uProjection"), false, App.camera.projection.get(stack.mallocFloat(16)));
		}
		
		glBindVertexArray(vao);
		glDrawArrays(GL_LINES, 0, verts.size());
		glBindVertexArray(0);
	}
}
