package faketorio;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL33.*;
import org.lwjgl.system.MemoryStack;

public class Model {
	int shader;
	int vao;
	int vbo;
	int vertCount;

	ArrayList<Integer> meshOffsets = new ArrayList<Integer>();
	ArrayList<Integer> meshSizes = new ArrayList<Integer>();
	ArrayList<Matrix4f> meshTransforms = new ArrayList<Matrix4f>();

	Matrix4f transform = new Matrix4f();
	Vector3f color = new Vector3f(-1f);
	Vector3f tint = new Vector3f(-1f);

	public Model copy() {
		Model model = new Model();
		model.shader = shader;
		model.vao = vao;
		model.vbo = vbo;
		model.vertCount = vertCount;
		model.meshOffsets = meshOffsets;
		model.meshSizes = meshSizes;
		model.meshTransforms = new ArrayList<Matrix4f>();
		model.transform = new Matrix4f();
		model.color = new Vector3f(-1f);
		model.tint = new Vector3f(-1f);
		return model;
	}

	public void draw() {
		if (glGetInteger(GL_CURRENT_PROGRAM) != shader) {
			glUseProgram(shader);
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniform3f(glGetUniformLocation(shader, "uColor"), color.x, color.y, color.z);
			glUniform3f(glGetUniformLocation(shader, "uTint"), tint.x, tint.y, tint.z);
			glUniformMatrix4fv(glGetUniformLocation(shader, "uModel"), false, transform.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "uView"), false, App.camera.view.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "uProjection"), false, App.camera.projection.get(stack.mallocFloat(16)));
		}
		
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, vertCount);
		glBindVertexArray(0);
	}
}