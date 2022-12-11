package faketorio;

import org.lwjgl.system.MemoryStack;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Entity {
	int vao;
	int vbo;
	int shader;
	int vertCount;

	Vector3f position = new Vector3f(0f, 0f, 0f);
	Matrix4f model = new Matrix4f();

	Vector3f color = new Vector3f(-1f);
	Vector3f tint = new Vector3f(-1f);

	String name = "entity";
	ArrayList<ItemStack> inventory = new ArrayList<ItemStack>();
	Label entityLabel = new Label();

	public void init() {
		model = new Matrix4f().translate(position);
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		shader = App.baseShader;
		entityLabel = new Label() {
			public void instanceUpdate() {
				text = "" + name + "\n";
				for (ItemStack itemStack : inventory) {
					text += itemStack.item.id + ": " + itemStack.item.name + ": " + itemStack.amount + "\n";
				}
				Tile tile = App.world.getTile(App.world.worldToTilePos(Entity.this.position));
				if (tile != null) {
					text += tile.name + "\n";
					for (ItemStack itemStack : tile.inventory) {
						text += itemStack.item.id + ": " + itemStack.item.name + ": " + itemStack.amount + "\n";
					}
				}
				updateMesh();
			}
		};
		entityLabel.text = "test";
		entityLabel.size = new Vector2f(24f);
		entityLabel.tether = this;
		entityLabel.color = new Vector3f(1f);
		entityLabel.fontAtlas = App.arialAtlas;
		entityLabel.hidden = false;
		entityLabel.init();
		App.ui.elements.add(entityLabel);
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
		instanceUpdate();
	}
	
	public void instanceUpdate() {

	}

	public void addItem(int id, int amount) {
		boolean found = false;
		for (ItemStack itemStack : inventory) {
			if (itemStack.item.id == id) {
				itemStack.amount += amount;
				found = true;
			}
		}
		if (!found) {
			ItemStack itemStack = new ItemStack();
			itemStack.item = App.items.get(id);
			itemStack.amount = amount;
			inventory.add(itemStack);
		}
	}

	public void draw() {
		if (glGetInteger(GL_CURRENT_PROGRAM) != shader) {
			glUseProgram(shader);
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniform3f(glGetUniformLocation(shader, "uColor"), color.x, color.y, color.z);
			glUniform3f(glGetUniformLocation(shader, "uTint"), tint.x, tint.y, tint.z);
			glUniformMatrix4fv(glGetUniformLocation(shader, "uModel"), false, model.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "uView"), false, App.camera.view.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shader, "uProjection"), false, App.camera.projection.get(stack.mallocFloat(16)));
		}
		
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, vertCount);
		glBindVertexArray(0);
	}

}
