package faketorio;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL33.*;

public class World {
	int vao;
	int vbo;
	int ibo;
	
	int shader;
	int vertCount;

	Matrix4f model = new Matrix4f();

	Vector2i size = new Vector2i(100, 100);

	ArrayList<Tile> tiles;

	Entity ghost = null;
	ArrayList<Entity> entities;

	Vector2i prevGhostPosition;
	Vector3f prevGhostTint;
	Vector2i prevTilePosition;
	Vector3f prevTileTint;

	public void init() {
		model = new Matrix4f();

		shader = App.resources.worldInstancedShader;

		tiles = new ArrayList<Tile>();
		for (int x=0;x<size.x;x++) {
			for (int y=0;y<size.y;y++) {
				Tile tile = new Tile();
				tile.position = new Vector2i(x-size.x/2, y-size.y/2);
				if ((x + y % 2) % 2 == 0) {
					tile.type = -1;
					tile.name = "empty tile";
					tile.color = new Vector3f(0.10f);
				} else {
					tile.type = -1;
					tile.name = "empty tile";
					tile.color = new Vector3f(0.12f);
				}
				if ((x > size.x/2 && x < size.x/2 + 10 && y > size.y/2 && y < size.y/2 + 10)) {
					tile.type = 0;
					tile.name = "iron tile";
					ItemStack itemStack = new ItemStack();
					itemStack.item = App.items.get(0);
					itemStack.amount = 1000;
					tile.inventory.add(itemStack);
					tile.color = new Vector3f(0.2f, 0.2f, 0.5f);
				}
				if ((x > size.x/2 + 10 && x < size.x/2 + 20 && y > size.y/2 && y < size.y/2 + 10)) {
					tile.type = 1;
					tile.name = "copper tile";
					ItemStack itemStack = new ItemStack();
					itemStack.item = App.items.get(1);
					itemStack.amount = 1000;
					tile.inventory.add(itemStack);
					tile.color = new Vector3f(0.5f, 0.2f, 0.2f);
				}
				if ((x > size.x/2 + 20 && x < size.x/2 + 30 && y > size.y/2 && y < size.y/2 + 10)) {
					tile.type = 2;
					tile.name = "coal tile";
					ItemStack itemStack = new ItemStack();
					itemStack.item = App.items.get(2);
					itemStack.amount = 1000;
					tile.inventory.add(itemStack);
					tile.color = new Vector3f(0.05f, 0.05f, 0.05f);
				}
				tiles.add(tile);
			}
		}

		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		ibo = glGenBuffers();

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
		int aPosition = glGetAttribLocation(shader, "aPosition");
		glEnableVertexAttribArray(aPosition);
		glVertexAttribPointer(aPosition, 3, GL_FLOAT, false, 9 * floatSize, 0);

		int aNormal = glGetAttribLocation(shader, "aNormal");
		glEnableVertexAttribArray(aNormal);
		glVertexAttribPointer(aNormal, 3, GL_FLOAT, false, 9 * floatSize, 3 * floatSize);

		int aColor = glGetAttribLocation(shader, "aColor");
		glEnableVertexAttribArray(aColor);
		glVertexAttribPointer(aColor, 3, GL_FLOAT, false, 9 * floatSize, 6 * floatSize);

		glBindBuffer(GL_ARRAY_BUFFER, 0); 

		glBindBuffer(GL_ARRAY_BUFFER, ibo);
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer data = stack.mallocFloat(9 * tiles.size());
			for (Tile tile : tiles) {
				data.put((float)tile.position.x).put((float)tile.position.y).put(0f);
				data.put(tile.color.x).put(tile.color.y).put(tile.color.z);
				data.put(tile.tint.x).put(tile.tint.y).put(tile.tint.z);
			}
			data.flip();
			glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW);
		}

		int iOffset = glGetAttribLocation(shader, "iOffset");
		glEnableVertexAttribArray(iOffset);
		glVertexAttribPointer(iOffset, 3, GL_FLOAT, false, 9 * floatSize, 0 * floatSize);
		glVertexAttribDivisor(iOffset, 1);

		int iColor = glGetAttribLocation(shader, "iColor");
		glEnableVertexAttribArray(iColor);
		glVertexAttribPointer(iColor, 3, GL_FLOAT, false, 9 * floatSize, 3 * floatSize);
		glVertexAttribDivisor(iColor, 1);

		int iTint = glGetAttribLocation(shader, "iTint");
		glEnableVertexAttribArray(iTint);
		glVertexAttribPointer(iTint, 3, GL_FLOAT, false, 9 * floatSize, 6 * floatSize);
		glVertexAttribDivisor(iTint, 1);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindVertexArray(0);
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

	public void update() {
		Vector3f worldPos = App.camera.screenToWorldPos(App.cursorPos);
		Vector2i tilePos = worldToTilePos(worldPos);
		moveGhost(tilePos, App.player.selectedItem);
		
		if (ghost != null) {
			ghost.update();
		}
		for (Entity entity : entities) {
			entity.update();
		}
	}
	
	public void draw() {
		if (glGetInteger(GL_CURRENT_PROGRAM) != shader) {
			glUseProgram(shader);
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniformMatrix4fv(glGetUniformLocation(shader, "uModel"), false, model.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "uView"), false, App.camera.view.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "uProjection"), false, App.camera.projection.get(stack.mallocFloat(16)));
		}
		
		glBindVertexArray(vao);
		glDrawArraysInstanced(GL_TRIANGLES, 0, vertCount, tiles.size());
		glBindVertexArray(0);
		
		if (ghost != null) {
			ghost.draw();
		}
		for (Entity entity : entities) {
			entity.draw();
		}
	}

	public void placeNew(Vector2i tilePos, int type) {
		Tile tile = getTile(tilePos);
		if (tile.free) {
			Entity entity = newEntity(tilePos, type);
			if (entity != null) {
				entity.init();
				entities.add(entity);
				tile.free = false;
			}
		}
	}

	public Entity newEntity(Vector2i tilePos, int type) {
		Entity entity = null;
		if (type == 1) {
			entity = new Miner(new Vector3f(tilePos.x, tilePos.y, 0f), App.player.itemRotation);
		} else if (type == 2) {
			entity = new Chest(new Vector3f(tilePos.x, tilePos.y, 0f), App.player.itemRotation);
		} else if (type == 3) {
			entity = new Belt(new Vector3f(tilePos.x, tilePos.y, 0f), App.player.itemRotation);
		} else if (type == 4) {

		} else if (type == 5) {
			
		} else if (type == 6) {
			entity = new Cube(new Vector3f(tilePos.x, tilePos.y, 0f), App.player.itemRotation);
		} else if (type == 7) {
			entity = new Triangle(new Vector3f(tilePos.x, tilePos.y, 0f), App.player.itemRotation);
		} else if (type == 8) {
			entity = new Sphere(new Vector3f(tilePos.x, tilePos.y, 0f), App.player.itemRotation);
		} else if (type == 9) {
			entity = new Monke(new Vector3f(tilePos.x, tilePos.y, 0f), App.player.itemRotation);
		}
		return entity;
	}

	public void placeEntity(Vector2i tilePos, Entity entity) {
		Tile tile = getTile(tilePos);
		if (tile.free) {
			entities.add(entity);
			tile.free = false;
		}
	}

	public void destroy(Vector2i tilePos) {
		Tile tile = getTile(tilePos);
		if (!tile.free) {
			for (Entity entity : entities) {
				if (worldToTilePos(entity.position).equals(tilePos)) {
					entities.remove(entity);
					tile.free = true;
					prevGhostPosition = null;
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
		Tile tile = getTile(tilePos);
		if (!tile.free) {
			for (Entity entity : entities) {
				if (worldToTilePos(entity.position).equals(tilePos)) {
					entity.label.hidden = !entity.label.hidden;	
				}
			}
		}
	}

	public void moveGhost(Vector2i tilePos, int type) {
		for (Element element : App.ui.elements) {
			if (element.tether == ghost && ghost != null) {
				App.ui.elements.remove(element);
				break;
			}
		}
		ghost = null;
		if (prevGhostPosition != null) {
			for (Entity entity : entities) {
				if (worldToTilePos(entity.position).equals(prevGhostPosition)) {
					entity.model.tint = new Vector3f(prevGhostTint);
				}
			}
		}
		if (tilePos != null) {
			Tile tile = getTile(tilePos);
			if (tile.free) {
				Entity entity = newEntity(tilePos, type);
				if (entity != null) {
					entity.name = "ghost " + entity.name;
					entity.model.color = new Vector3f(1f, 1f, 1f);
					entity.init();
					ghost = entity;
				}
			} else {
				for (Entity entity : entities) {
					if (worldToTilePos(entity.position).equals(tilePos)) {
						prevGhostPosition = worldToTilePos(entity.position);
						prevGhostTint = new Vector3f(entity.model.tint);
						entity.model.tint = new Vector3f(1f, 1f, 1f);
					}
				}
			}	
		}

		if (prevTilePosition != null) {
			Tile tile = getTile(prevTilePosition);
			tile.tint = new Vector3f(prevTileTint);
			int floatSize = 4;
			glBindBuffer(GL_ARRAY_BUFFER, ibo);
			try (MemoryStack stack = MemoryStack.stackPush()) {
				FloatBuffer data = stack.mallocFloat(3);
				data.put(tile.tint.x).put(tile.tint.y).put(tile.tint.z);
				data.flip();
				glBufferSubData(GL_ARRAY_BUFFER, tiles.indexOf(tile) * 9 * floatSize + 6 * floatSize, data);
			}
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
		if (tilePos != null) {
			Tile tile = getTile(tilePos);
			prevTilePosition = new Vector2i(tile.position);
			prevTileTint = new Vector3f(tile.tint);
			tile.tint = new Vector3f(1f, 1f, 1f);
	
			int floatSize = 4;
			glBindBuffer(GL_ARRAY_BUFFER, ibo);
			try (MemoryStack stack = MemoryStack.stackPush()) {
				FloatBuffer data = stack.mallocFloat(3);
				data.put(tile.tint.x).put(tile.tint.y).put(tile.tint.z);
				data.flip();
				glBufferSubData(GL_ARRAY_BUFFER, tiles.indexOf(tile) * 9 * floatSize + 6 * floatSize, data);
			}
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
	}

	public Tile getTile(Vector2i tilePos) {
		for (Tile tile : tiles) {
			if (tile.position.equals(tilePos)) {
				return tile;
			}
		}
		return null;
	}

	public Vector2i worldToTilePos(Vector3f worldPos) {
		if (worldPos == null) {
			return null;
		}
		Vector2i tilePos = new Vector2i((int)Math.floor(worldPos.x), (int)Math.floor(worldPos.y));
		return tilePos;
	}

	public Entity getEntity(Vector2i tilePos) {
		for (Entity entity : entities) {
			if (worldToTilePos(entity.position).equals(tilePos)) {
				return entity;
			}
		}
		return null;
	}
}
