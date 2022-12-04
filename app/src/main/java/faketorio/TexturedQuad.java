package faketorio;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;

public class TexturedQuad extends Element {
	int texture = 0;
	
	public void init() {
		model = new Matrix4f().translate(position).scale(size);
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		shader = App.uiTexturedShader;
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
		int aPosition = glGetAttribLocation(shader, "position");
		glEnableVertexAttribArray(aPosition);
		glVertexAttribPointer(aPosition, 3, GL_FLOAT, false, 5 * floatSize, 0);

		int aUv = glGetAttribLocation(shader, "uv");
		glEnableVertexAttribArray(aUv);
		glVertexAttribPointer(aUv, 2, GL_FLOAT, false, 5 * floatSize, 3 * floatSize);

		glBindVertexArray(0);
	}

	public FloatBuffer generateMesh(MemoryStack stack) {
		vertCount = 6;
		FloatBuffer mesh = stack.mallocFloat(5 * vertCount);
		mesh.put(0f).put(1f).put(0f).put(0f).put(1f);
		mesh.put(0f).put(0f).put(0f).put(0f).put(0f);
		mesh.put(1f).put(0f).put(0f).put(1f).put(0f);
		mesh.put(1f).put(0f).put(0f).put(1f).put(0f);
		mesh.put(1f).put(1f).put(0f).put(1f).put(1f);
		mesh.put(0f).put(1f).put(0f).put(0f).put(1f);
		mesh.flip();
		return mesh;
	}

	public void draw() {
		if (glGetInteger(GL_CURRENT_PROGRAM) != shader) {
			glUseProgram(shader);
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniform3f(glGetUniformLocation(shader, "tint"), tint.x, tint.y, tint.z);
			glUniformMatrix4fv(glGetUniformLocation(shader, "model"), false, model.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "view"), false, App.ui.view.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "projection"), false, App.ui.projection.get(stack.mallocFloat(16)));
		}
		
		glBindTexture(GL_TEXTURE_2D, texture);
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, vertCount);
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
