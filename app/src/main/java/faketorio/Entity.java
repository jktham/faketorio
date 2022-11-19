package faketorio;

import org.lwjgl.system.MemoryStack;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;

public class Entity {
	int vao;
	int vbo;
	Matrix4f model = new Matrix4f();

	public Entity(int shaderProgram) {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer vertices = stack.mallocFloat(3 * 6);
			vertices.put(-0.5f).put(-0.43f).put(0f).put(1f).put(0f).put(0f);
			vertices.put(0.5f).put(-0.43f).put(0f).put(0f).put(1f).put(0f);
			vertices.put(0f).put(0.43f).put(0f).put(0f).put(0f).put(1f);
			vertices.flip();

			glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		}

		int floatSize = 4;
		int aPos = glGetAttribLocation(shaderProgram, "position");
		glEnableVertexAttribArray(aPos);
		glVertexAttribPointer(aPos, 3, GL_FLOAT, false, 6 * floatSize, 0);

		int aCol = glGetAttribLocation(shaderProgram, "color");
		glEnableVertexAttribArray(aCol);
		glVertexAttribPointer(aCol, 3, GL_FLOAT, false, 6 * floatSize, 3 * floatSize);

		glBindVertexArray(0);
	}

	public void update(double deltaTime) {
		model.rotate((float)(0.5f * deltaTime), 0f, 0f, 1f);
	}

}
