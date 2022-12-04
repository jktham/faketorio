package faketorio;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Scanner;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL33.*;

public class Label extends Element {
	String text = "";
	int fontAtlas;

	ArrayList<Glyph> glyphs;
	
	public void init() {
		model = new Matrix4f().translate(position).scale(size);
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		shader = App.uiTexturedShader;

		glyphs = new ArrayList<Glyph>();
		for (int i=0;i<256;i++) {
			Glyph glyph = new Glyph();
			glyph.id = i;
			float w = 1f / 16f;
			glyph.uvW = w;
			glyph.uvX = w * (i % 16);
			glyph.uvY = w * (i / 16) - 2*w;
			glyphs.add(glyph);
		}

		try {
			Scanner scanner = new Scanner(new File("app/src/main/resources/fonts/arial.csv"));
			int i = 0;
			while (scanner.hasNextLine()) {
				i++;
				String line = scanner.nextLine();
				if (i > 8 && i <= 264) {
					glyphs.get(i-9).width = Float.parseFloat(line.split(",")[1]) / 128f;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

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
		vertCount = 6 * text.length();
		FloatBuffer mesh = stack.mallocFloat(5 * vertCount);
		float offsetX = 0f;
		float offsetY = 0f;
		for (int i=0;i<text.length();i++) {
			int id = text.charAt(i);
			if (id == 10) {
				offsetX = 0f;
				offsetY += 1f;
				continue;
			}
			float width = glyphs.get(id).width;
			float uvW = glyphs.get(id).uvW;
			float uvX = glyphs.get(id).uvX;
			float uvY = glyphs.get(id).uvY;

			mesh.put(offsetX + 0f).put(offsetY + 1f).put(0f).put(uvX).put(uvY + uvW);
			mesh.put(offsetX + 0f).put(offsetY + 0f).put(0f).put(uvX).put(uvY);
			mesh.put(offsetX + 1f).put(offsetY + 0f).put(0f).put(uvX + uvW).put(uvY);
			mesh.put(offsetX + 1f).put(offsetY + 0f).put(0f).put(uvX + uvW).put(uvY);
			mesh.put(offsetX + 1f).put(offsetY + 1f).put(0f).put(uvX + uvW).put(uvY + uvW);
			mesh.put(offsetX + 0f).put(offsetY + 1f).put(0f).put(uvX).put(uvY + uvW);
			offsetX += width;
		}
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
		
		glBindTexture(GL_TEXTURE_2D, fontAtlas);
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, vertCount);
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
