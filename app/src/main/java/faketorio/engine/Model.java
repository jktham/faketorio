package faketorio.engine;

import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class Model {
	public int shader;
	public int vao;
	public int vbo;

	public ArrayList<Integer> meshOffsets;
	public ArrayList<Integer> meshSizes;
	public ArrayList<Matrix4f> meshTransforms;
	public ArrayList<Vector3f> meshColors;
	public ArrayList<Boolean> meshHidden;

	public Matrix4f transform;
	public Vector3f color;
	public Vector3f tint;
	
	public Model() {
		meshOffsets = new ArrayList<Integer>();
		meshSizes = new ArrayList<Integer>();
		meshTransforms = new ArrayList<Matrix4f>();
		meshColors = new ArrayList<Vector3f>();
		meshHidden = new ArrayList<Boolean>();

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
		model.meshColors = new ArrayList<Vector3f>();
		for (int i=0;i<meshColors.size();i++) {
			model.meshColors.add(meshColors.get(i));
		}
		model.meshHidden = new ArrayList<Boolean>();
		for (int i=0;i<meshHidden.size();i++) {
			model.meshHidden.add(meshHidden.get(i));
		}
		model.transform = new Matrix4f();
		model.color = new Vector3f(-1f);
		model.tint = new Vector3f(-1f);
		return model;
	}

	public void draw() {
		for (int i=0;i<meshOffsets.size();i++) {
			if (meshHidden.get(i)) {
				continue;
			}

			if (glGetInteger(GL_CURRENT_PROGRAM) != shader) {
				glUseProgram(shader);
			}

			try (MemoryStack stack = MemoryStack.stackPush()) {
				if (meshColors.get(i).x >= 0f) {
					glUniform3f(glGetUniformLocation(shader, "uColor"), meshColors.get(i).x, meshColors.get(i).y, meshColors.get(i).z);
				} else {
					glUniform3f(glGetUniformLocation(shader, "uColor"), color.x, color.y, color.z);
				}
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
