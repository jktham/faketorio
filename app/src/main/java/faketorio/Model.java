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

	ArrayList<Integer> meshOffsets;
	ArrayList<Integer> meshSizes;
	ArrayList<Matrix4f> meshTransforms;

	Matrix4f transform;
	Vector3f color;
	Vector3f tint;
	
	public Model() {
		meshOffsets = new ArrayList<Integer>();
		meshSizes = new ArrayList<Integer>();
		meshTransforms = new ArrayList<Matrix4f>();

		transform = new Matrix4f();
		color = new Vector3f(-1f);
		tint = new Vector3f(-1f);
	}

	public Model copy() {
		Model model = new Model();
		model.shader = shader;
		model.vao = vao;
		model.vbo = vbo;
		model.meshOffsets = meshOffsets;
		model.meshSizes = meshSizes;
		model.meshTransforms = new ArrayList<Matrix4f>();
		for (int i=0;i<meshTransforms.size();i++) {
			model.meshTransforms.add(meshTransforms.get(i));
		}
		model.transform = new Matrix4f();
		model.color = new Vector3f(-1f);
		model.tint = new Vector3f(-1f);
		return model;
	}

	public void draw() {
		for (int i=0;i<meshOffsets.size();i++) {
			if (glGetInteger(GL_CURRENT_PROGRAM) != shader) {
				glUseProgram(shader);
			}

			try (MemoryStack stack = MemoryStack.stackPush()) {
				glUniform3f(glGetUniformLocation(shader, "uColor"), color.x, color.y, color.z);
				glUniform3f(glGetUniformLocation(shader, "uTint"), tint.x, tint.y, tint.z);
				glUniformMatrix4fv(glGetUniformLocation(shader, "uModel"), false, new Matrix4f(transform).mul(meshTransforms.get(i)).get(stack.mallocFloat(16)));
				glUniformMatrix4fv(glGetUniformLocation(shader, "uView"), false, App.camera.view.get(stack.mallocFloat(16)));
				glUniformMatrix4fv(glGetUniformLocation(shader, "uProjection"), false, App.camera.projection.get(stack.mallocFloat(16)));
			}
			
			glBindVertexArray(vao);
			glDrawArrays(GL_TRIANGLES, meshOffsets.get(i), meshSizes.get(i));
			glBindVertexArray(0);
		}
	}
}
