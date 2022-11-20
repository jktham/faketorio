package faketorio;

import org.lwjgl.system.MemoryStack;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;

public class Entity {
	int vao = 0;
	int vbo = 0;
	
	int vertCount = 0;

	Matrix4f model = new Matrix4f();

	public Entity() {
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
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
		int aPos = glGetAttribLocation(App.shaderProgram, "position");
		glEnableVertexAttribArray(aPos);
		glVertexAttribPointer(aPos, 3, GL_FLOAT, false, 6 * floatSize, 0);

		int aCol = glGetAttribLocation(App.shaderProgram, "color");
		glEnableVertexAttribArray(aCol);
		glVertexAttribPointer(aCol, 3, GL_FLOAT, false, 6 * floatSize, 3 * floatSize);

		glBindVertexArray(0);
	}

	public FloatBuffer generateMesh(MemoryStack stack) {
		FloatBuffer mesh = stack.mallocFloat(3 * vertCount);
		return mesh;
	}

	public void update() {

	}

	public void draw() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniformMatrix4fv(glGetUniformLocation(App.shaderProgram, "model"), false, model.get(stack.mallocFloat(16)));
		}
		
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, vertCount);
		glBindVertexArray(0);
	}

}
