package faketorio;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL33.*;

public class World {
	int[] vao;
	int[] vbo;
	int[] ibo;
	
	int shader;
	int vertCount;

	Matrix4f model = new Matrix4f();

	Vector2i size = new Vector2i(100, 100);

	Tile[][] tiles;
	
	int tileTypes;
	Vector3f[] tileTints;
	ArrayList<Vector3f>[] tileOffsets;

	ArrayList<Entity> ghost;
	ArrayList<Entity> entities;

	Vector2i prevGhostPosition;
	Vector3f prevGhostTint;

	@SuppressWarnings("unchecked")
	public void init() {
		model = new Matrix4f();

		shader = App.instanceShader;

		tiles = new Tile[size.x][size.y];
		for (int x=0;x<size.x;x++) {
			for (int y=0;y<size.y;y++) {
				tiles[x][y] = new Tile();
				if ((x + y % 2) % 2 == 0) {
					tiles[x][y].type = 0;
				} else {
					tiles[x][y].type = 1;
				}
			}
		}

		tileTypes = 2;

		vao = new int[tileTypes];
		vbo = new int[tileTypes];
		ibo = new int[tileTypes];
		for (int i=0;i<tileTypes;i++) {
			vao[i] = glGenVertexArrays();
			vbo[i] = glGenBuffers();
			ibo[i] = glGenBuffers();
		}

		tileTints = new Vector3f[tileTypes];
		tileTints[0] = new Vector3f(0.1f);
		tileTints[1] = new Vector3f(0.12f);

		tileOffsets = (ArrayList<Vector3f>[]) new ArrayList[tileTypes];
		tileOffsets[0] = new ArrayList<Vector3f>();
		tileOffsets[1] = new ArrayList<Vector3f>();

		for (int x=0;x<size.x;x++) {
			for (int y=0;y<size.y;y++) {
				tileOffsets[tiles[x][y].type].add(new Vector3f(x-size.x/2, y-size.y/2, 0f));
			}
		}

		ghost = new ArrayList<Entity>();
		entities = new ArrayList<Entity>();
		updateMesh();
	}

	public void updateMesh() {
		for (int i=0;i<tileTypes;i++) {
			glBindVertexArray(vao[i]);
			glBindBuffer(GL_ARRAY_BUFFER, vbo[i]);

			try (MemoryStack stack = MemoryStack.stackPush()) {
				FloatBuffer mesh = generateMesh(stack);
				glBufferData(GL_ARRAY_BUFFER, mesh, GL_STATIC_DRAW);
			}

			int floatSize = 4;
			int aPosition = glGetAttribLocation(shader, "position");
			glEnableVertexAttribArray(aPosition);
			glVertexAttribPointer(aPosition, 3, GL_FLOAT, false, 9 * floatSize, 0);

			int aNormal = glGetAttribLocation(shader, "normal");
			glEnableVertexAttribArray(aNormal);
			glVertexAttribPointer(aNormal, 3, GL_FLOAT, false, 9 * floatSize, 3 * floatSize);

			int aColor = glGetAttribLocation(shader, "color");
			glEnableVertexAttribArray(aColor);
			glVertexAttribPointer(aColor, 3, GL_FLOAT, false, 9 * floatSize, 6 * floatSize);

			glBindBuffer(GL_ARRAY_BUFFER, 0); 

			glBindBuffer(GL_ARRAY_BUFFER, ibo[i]);
			
			try (MemoryStack stack = MemoryStack.stackPush()) {
				FloatBuffer data = stack.mallocFloat(3 * tileOffsets[i].size());
				for (int j=0;j<tileOffsets[0].size();j++) {
					data.put(tileOffsets[i].get(j).x).put(tileOffsets[i].get(j).y).put(tileOffsets[i].get(j).z);
				}
				data.flip();
				glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
			}

			int aOffset = glGetAttribLocation(shader, "offset");
			glEnableVertexAttribArray(aOffset);
			glVertexAttribPointer(aOffset, 3, GL_FLOAT, false, 3 * floatSize, 0);

			glBindBuffer(GL_ARRAY_BUFFER, 0);

			glVertexAttribDivisor(aOffset, 1);

			glBindVertexArray(0);
		}
	}

	public void update() {
		Vector3f worldPos = App.camera.screenToWorldPos(App.cursorPos);
		Vector2i tilePos = worldToTilePos(worldPos);
		moveGhost(tilePos, App.player.item);
		
		for (Entity g : ghost) {
			g.update();
		}
		for (Entity e : entities) {
			e.update();
		}
	}

	public FloatBuffer generateMesh(MemoryStack stack) {
		vertCount = 6;
		FloatBuffer mesh = stack.mallocFloat(9 * vertCount);
		mesh.put(0f).put(1f).put(0f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(0f).put(0f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(0f).put(0f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(0f).put(0f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.put(1f).put(1f).put(0f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.put(0f).put(1f).put(0f).put(0f).put(0f).put(1f).put(1f).put(1f).put(1f);
		mesh.flip();
		return mesh;
	}
	
	public void draw() {
		if (glGetInteger(GL_CURRENT_PROGRAM) != shader) {
			glUseProgram(shader);
		}

		for (int i=0;i<tileTypes;i++) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				glUniform3f(glGetUniformLocation(shader, "tint"), tileTints[i].x, tileTints[i].y, tileTints[i].z);
				glUniformMatrix4fv(glGetUniformLocation(shader, "model"), false, model.get(stack.mallocFloat(16)));
				glUniformMatrix4fv(glGetUniformLocation(shader, "view"), false, App.camera.view.get(stack.mallocFloat(16)));
				glUniformMatrix4fv(glGetUniformLocation(shader, "projection"), false, App.camera.projection.get(stack.mallocFloat(16)));
			}
			
			glBindVertexArray(vao[i]);
			glDrawArraysInstanced(GL_TRIANGLES, 0, vertCount, tileOffsets[i].size());
			glBindVertexArray(0);
		}
		
		
		for (Entity g : ghost) {
			g.draw();
		}
		for (Entity e : entities) {
			e.draw();
		}
	}

	public void placeNew(Vector2i tilePos, int type) {
		Tile tile = getTile(tilePos);
		if (tile.free) {
			if (type == 1) {
				Cube cube = new Cube();
				cube.position = new Vector3f(tilePos.x, tilePos.y, 0f);
				cube.tint = new Vector3f(0f, 0f, 1f);
				cube.init();
				entities.add(cube);
				tile.free = false;
			} else if (type == 2) {
				Triangle triangle = new Triangle();
				triangle.position = new Vector3f(tilePos.x, tilePos.y, 0f);
				triangle.tint = new Vector3f(-1f, -1f, -1f);
				triangle.init();
				entities.add(triangle);
				tile.free = false;
			} else if (type == 3) {
				Sphere sphere = new Sphere();
				sphere.position = new Vector3f(tilePos.x, tilePos.y, 0f);
				sphere.tint = new Vector3f(1f, 0f, 1f);
				sphere.init();
				entities.add(sphere);
				tile.free = false;
			}
		}
	}

	public void placeEntity(Vector2i tilePos, Entity entity) {
		Tile tile = getTile(tilePos);
		entities.add(entity);
		tile.free = false;
	}

	public void destroy(Vector2i tilePos) {
		Tile tile = getTile(tilePos);
		if (!tile.free) {
			for (Entity entity : entities) {
				if (worldToTilePos(entity.position).equals(tilePos)) {
					entities.remove(entity);
					tile.free = true;
					for (Element element : App.ui.elements) {
						if (element.tether == entity) {
							App.ui.elements.remove(element);
							break;
						}
					}
					break;
				}
			}
		}
	}

	public void interact(Vector2i tilePos) {
		
	}

	public void moveGhost(Vector2i tilePos, int type) {
		ghost.clear();
		if (prevGhostPosition != null) {
			for (Entity e : entities) {
				if (worldToTilePos(e.position).equals(prevGhostPosition)) {
					e.tint = prevGhostTint;
				}
			}
		}
		if (tilePos != null) {
			Tile tile = getTile(tilePos);
			if (tile.free) {
				if (App.player.item == 1) {
					Cube ghostCube = new Cube();
					ghostCube.position = new Vector3f(tilePos.x, tilePos.y, 0f);
					ghostCube.tint = new Vector3f(0f, 1f, 0f);
					ghostCube.init();
					ghost.add(ghostCube);
				} else if (App.player.item == 2) {
					Triangle ghostTriangle = new Triangle();
					ghostTriangle.position = new Vector3f(tilePos.x, tilePos.y, 0f);
					ghostTriangle.tint = new Vector3f(0f, 1f, 0f);
					ghostTriangle.init();
					ghost.add(ghostTriangle);
				} else if (App.player.item == 3) {
					Sphere ghostSphere = new Sphere();
					ghostSphere.position = new Vector3f(tilePos.x, tilePos.y, 0f);
					ghostSphere.tint = new Vector3f(0f, 1f, 0f);
					ghostSphere.init();
					ghost.add(ghostSphere);
				}
			} else {
				for (Entity entity : entities) {
					if (worldToTilePos(entity.position).equals(tilePos)) {
						prevGhostPosition = worldToTilePos(entity.position);
						prevGhostTint = new Vector3f(entity.tint);
						entity.tint = new Vector3f(1f, 0f, 0f);
					}
				}
			}
		}
	}

	public Tile getTile(Vector2i tilePos) {
		return tiles[(int)tilePos.x+size.x/2][(int)tilePos.y+size.y/2];
	}

	public Vector2i worldToTilePos(Vector3f worldPos) {
		if (worldPos == null) {
			return null;
		}
		return new Vector2i((int)worldPos.floor().x, (int)worldPos.floor().y);
	}
}
