package faketorio;

import org.lwjgl.system.MemoryStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;

public class Entity {
	int vao = 0;
	int vbo = 0;
	int shaderProgram = 0;
	int vertCount = 0;

	Vector3f position = new Vector3f(0f, 0f, 0f);
	Matrix4f model = new Matrix4f();

	public void init() {
		model = new Matrix4f().translate(position);
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		shaderProgram = App.shaderProgram;
		updateMesh();
	}

	public void updateMesh() {
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer mesh = generateMesh(stack);
			glBufferData(GL_ARRAY_BUFFER, mesh, GL_STATIC_DRAW);
		}

		int floatSize = 4;
		int aPosition = glGetAttribLocation(App.shaderProgram, "position");
		glEnableVertexAttribArray(aPosition);
		glVertexAttribPointer(aPosition, 3, GL_FLOAT, false, 9 * floatSize, 0);

		int aNormal = glGetAttribLocation(App.shaderProgram, "normal");
		glEnableVertexAttribArray(aNormal);
		glVertexAttribPointer(aNormal, 3, GL_FLOAT, false, 9 * floatSize, 3 * floatSize);

		int aColor = glGetAttribLocation(App.shaderProgram, "color");
		glEnableVertexAttribArray(aColor);
		glVertexAttribPointer(aColor, 3, GL_FLOAT, false, 9 * floatSize, 6 * floatSize);

		glBindVertexArray(0);
	}

	public FloatBuffer generateMesh(MemoryStack stack) {
		vertCount = 0;
		FloatBuffer mesh = stack.mallocFloat(9 * vertCount);
		return mesh;
	}

	public void update() {

	}

	public void draw() {
		if (glGetInteger(GL_CURRENT_PROGRAM) != shaderProgram) {
			glUseProgram(shaderProgram);
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "model"), false, model.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "view"), false, App.camera.view.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "projection"), false, App.camera.projection.get(stack.mallocFloat(16)));
		}
		
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, vertCount);
		glBindVertexArray(0);
	}

}
