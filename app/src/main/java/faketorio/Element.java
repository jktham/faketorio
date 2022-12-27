package faketorio;

import org.lwjgl.system.MemoryStack;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;

public class Element {
	int vao;
	int vbo;
	int shader;
	int vertCount;

	Entity tether = null;
	Vector3f tetherWorldOffset = new Vector3f();
	Vector2f tetherScreenOffset = new Vector2f();;

	Vector2f position = new Vector2f(0f);
	Vector2f size = new Vector2f(0f);

	Matrix4f model = new Matrix4f();

	Vector3f color = new Vector3f(-1f);
	Vector3f tint = new Vector3f(-1f);
	boolean hidden = false;

	public void init() {
		model = new Matrix4f().translate(new Vector3f(position.x, position.y, 1f)).scale(new Vector3f(size.x, size.y, 1f));
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		shader = App.resources.uiBaseShader;
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
		int aPosition = glGetAttribLocation(shader, "aPosition");
		glEnableVertexAttribArray(aPosition);
		glVertexAttribPointer(aPosition, 3, GL_FLOAT, false, 9 * floatSize, 0);

		int aNormal = glGetAttribLocation(shader, "aNormal");
		glEnableVertexAttribArray(aNormal);
		glVertexAttribPointer(aNormal, 3, GL_FLOAT, false, 9 * floatSize, 3 * floatSize);

		int aColor = glGetAttribLocation(shader, "aColor");
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
		if (tether != null) {
			Vector4f tetherPos = new Vector4f(0f, 0f, 0f, 1f).mul(new Matrix4f().translate(tether.worldPos));
			Vector2f screenTetherPos = App.camera.worldToScreenPos(new Vector3f(tetherPos.x, tetherPos.y, tetherPos.z).add(tetherWorldOffset)).add(tetherScreenOffset);
			position = new Vector2f(screenTetherPos.x, screenTetherPos.y);
		}
		model = new Matrix4f().translate(new Vector3f(position.x, position.y, 1f)).scale(new Vector3f(size.x, size.y, 1f));
	}

	public void draw() {
		if (hidden) {
			return;
		}
		
		if (glGetInteger(GL_CURRENT_PROGRAM) != shader) {
			glUseProgram(shader);
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniform3f(glGetUniformLocation(shader, "uColor"), color.x, color.y, color.z);
			glUniform3f(glGetUniformLocation(shader, "uTint"), tint.x, tint.y, tint.z);
			glUniformMatrix4fv(glGetUniformLocation(shader, "uModel"), false, model.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "uView"), false, App.ui.view.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "uProjection"), false, App.ui.projection.get(stack.mallocFloat(16)));
		}
		
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, vertCount);
		glBindVertexArray(0);
	}
}
