package faketorio;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL33.*;

public class World {
	int vao = 0;
	int vbo = 0;
	int shaderProgram = 0;
	int vertCount = 0;

	Matrix4f model = new Matrix4f();

	Vector2i size = new Vector2i(100, 100);

	Tile[][] tiles;
	
	ArrayList<Entity> ghost;
	ArrayList<Entity> entities;

	public void init() {
		model = new Matrix4f();
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		shaderProgram = App.shaderProgram;
		tiles = new Tile[size.x][size.y];
		for (int x=0;x<size.x;x++) {
			for (int y=0;y<size.y;y++) {
				tiles[x][y] = new Tile();
			}
		}
		ghost = new ArrayList<Entity>();
		entities = new ArrayList<Entity>();
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

	public void update() {
		Vector3f worldPos = App.camera.getCursorWorldPos();
		ghost.clear();
		if (worldPos != null) {
			Vector3f tilePos = new Vector3f(worldPos).floor();
			if (tiles[(int)tilePos.x+size.x/2][(int)tilePos.y+size.y/2].free) {
				if (App.player.item == 1) {
					Cube ghostCube = new Cube();
					ghostCube.position = new Vector3f(tilePos.x, tilePos.y, 0f);
					ghostCube.color = new Vector3f(1f, 1f, 1f);
					ghostCube.init();
					ghost.add(ghostCube);
				} else if (App.player.item == 2) {
					Triangle ghostTriangle = new Triangle();
					ghostTriangle.position = new Vector3f(tilePos.x, tilePos.y, 0f);
					ghostTriangle.color = new Vector3f(1f, 1f, 1f);
					ghostTriangle.init();
					ghost.add(ghostTriangle);
				} else if (App.player.item == 3) {
					Sphere ghostSphere = new Sphere();
					ghostSphere.position = new Vector3f(tilePos.x, tilePos.y, 0f);
					ghostSphere.color = new Vector3f(1f, 1f, 1f);
					ghostSphere.init();
					ghost.add(ghostSphere);
				}
			}
		}
		
		for (Entity g : ghost) {
			g.update();
		}
		for (Entity e : entities) {
			e.update();
		}
	}

	public FloatBuffer generateMesh(MemoryStack stack) {
		vertCount = 6 * size.x * size.y;
		Vector3f color = new Vector3f(0.1f, 0.1f, 0.1f);
		FloatBuffer mesh = stack.mallocFloat(9 * vertCount);
		for (int x=0;x<size.x;x++) {
			for (int y=0;y<size.y;y++) {
				if ((x + y % 2) % 2 == 0) {
					color = new Vector3f(0.1f, 0.1f, 0.1f);
				} else {
					color = new Vector3f(0.12f, 0.12f, 0.12f);
				}
				mesh.put(x  -size.x/2).put(y+1-size.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
				mesh.put(x  -size.x/2).put(y  -size.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
				mesh.put(x+1-size.x/2).put(y  -size.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
				mesh.put(x+1-size.x/2).put(y  -size.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
				mesh.put(x+1-size.x/2).put(y+1-size.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
				mesh.put(x  -size.x/2).put(y+1-size.y/2).put(0f).put(0f).put(0f).put(1f).put(color.x).put(color.y).put(color.z);
			}
		}
		mesh.flip();
		return mesh;
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
		
		for (Entity g : ghost) {
			g.draw();
		}
		for (Entity e : entities) {
			e.draw();
		}
	}

}
