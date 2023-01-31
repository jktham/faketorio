package faketorio.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.Configuration;

import faketorio.entities.Camera;
import faketorio.entities.Player;
import faketorio.entities.buildings.Building;
import faketorio.inventory.Item;
import faketorio.inventory.ItemStack;
import faketorio.inventory.Recipe;
import faketorio.ui.Label;
import faketorio.ui.Ui;
import faketorio.world.Tile;
import faketorio.world.World;

public class App {
	public static long window = 0;
	public static int width = 1920;
	public static int height = 1080;

	public static float time = 0.0f;

	public static int frame;
	public static float targetFrameRate = 60.0f;
	public static float actualFrameRate;
	public static float lastFrame;
	public static float deltaFrame;

	public static int tick;
	public static float targetTickRate = 60.0f;
	public static float actualTickRate;
	public static float lastTick;
	public static float deltaTick;

	public static Vector2i cursorPos = new Vector2i(0);

	public static Resources resources;
	public static ArrayList<Item> items;
	public static ArrayList<Recipe> recipes;
	public static World world;
	public static Player player;
	public static Camera camera;
	public static Ui ui;

	public static void main(String[] args) {
		new App().run();
	}

	public void run() {
		init();
		loop();
		cleanup();
	}
	
	private void init() {
		Configuration.STACK_SIZE.set(10000);

		GLFWErrorCallback.createPrint(System.err).set();

		glfwInit();

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(width, height, "faketorio", NULL, NULL);

		glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			App.width = width;
			App.height = height;
			glViewport(0, 0, width, height);
		});

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
				glfwSetWindowShouldClose(window, true);
			}
			if (key >= 48 && key < 58 && action == GLFW_PRESS) {
				player.selectedItem = key - 48;
				if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
					player.selectedItem += 10;
				}
			}
			if (key == GLFW_KEY_R && action == GLFW_PRESS) {
				player.itemRotation = player.itemRotation - 1;
				if (player.itemRotation < 0) {
					player.itemRotation = 3;
				}
			}
			if (key == GLFW_KEY_Q && action == GLFW_PRESS) {
				Building building = world.getBuilding(world.worldToTilePos(camera.screenToWorldPos(cursorPos)));
				if (building != null) {
					player.selectedItem = building.type;
					player.itemRotation = building.rotation;
				} else {
					player.selectedItem = 0;
				}
			}
			if (key == GLFW_KEY_LEFT_ALT && action == GLFW_PRESS) {
				ui.hidden = !ui.hidden;
			}
			if (key == GLFW_KEY_L && action == GLFW_PRESS) {
				

				world.placeNew(new Vector2i(2, 5), 8, 1);
				world.placeNew(new Vector2i(2, 4), 8, 1);
				world.placeNew(new Vector2i(2, 3), 8, 1);
				world.placeNew(new Vector2i(2, 2), 8, 1);
				world.placeNew(new Vector2i(12, 5), 8, 1);
				world.placeNew(new Vector2i(12, 4), 8, 1);
				world.placeNew(new Vector2i(12, 3), 8, 1);
				world.placeNew(new Vector2i(12, 2), 8, 1);
				world.placeNew(new Vector2i(22, 5), 8, 1);
				world.placeNew(new Vector2i(22, 4), 8, 1);
				world.placeNew(new Vector2i(22, 3), 8, 1);
				world.placeNew(new Vector2i(22, 2), 8, 1);

				world.placeNew(new Vector2i(23, 5), 1, 0);
				world.placeNew(new Vector2i(23, 4), 1, 0);
				world.placeNew(new Vector2i(23, 3), 1, 0);
				world.placeNew(new Vector2i(23, 2), 1, 0);
				world.placeNew(new Vector2i(23, 1), 1, 0);
				world.placeNew(new Vector2i(23, 0), 1, 0);
				world.placeNew(new Vector2i(23, -1), 1, 0);
				world.placeNew(new Vector2i(23, -2), 1, 3);
				world.placeNew(new Vector2i(22, -2), 1, 3);
				world.placeNew(new Vector2i(21, -2), 1, 3);
				world.placeNew(new Vector2i(20, -2), 1, 3);
				world.placeNew(new Vector2i(19, -2), 1, 3);
				world.placeNew(new Vector2i(18, -2), 1, 3);
				world.placeNew(new Vector2i(17, -2), 1, 3);
				world.placeNew(new Vector2i(16, -2), 3, 3);
				world.placeNew(new Vector2i(16, -1), 1, 3);
				world.placeNew(new Vector2i(16, -3), 1, 3);
				world.placeNew(new Vector2i(15, -1), 2, 3);
				world.placeNew(new Vector2i(11, -1), 1, 3);
				world.placeNew(new Vector2i(10, -1), 1, 3);
				world.placeNew(new Vector2i(9, -1), 1, 3);
				world.placeNew(new Vector2i(8, -1), 1, 3);
				world.placeNew(new Vector2i(7, -1), 1, 3);
				world.placeNew(new Vector2i(6, -1), 1, 3);
				world.placeNew(new Vector2i(5, -1), 1, 3);

				world.placeNew(new Vector2i(13, 5), 1, 0);
				world.placeNew(new Vector2i(13, 4), 1, 0);
				world.placeNew(new Vector2i(13, 3), 1, 0);
				world.placeNew(new Vector2i(13, 2), 1, 0);
				world.placeNew(new Vector2i(13, 1), 1, 0);
				world.placeNew(new Vector2i(13, 0), 1, 0);
				world.placeNew(new Vector2i(13, -1), 1, 0);
				world.placeNew(new Vector2i(13, -2), 1, 0);
				world.placeNew(new Vector2i(13, -3), 1, 1);
				world.placeNew(new Vector2i(15, -3), 1, 3);

				world.placeNew(new Vector2i(3, 5), 1, 0);
				world.placeNew(new Vector2i(3, 4), 1, 0);
				world.placeNew(new Vector2i(3, 3), 1, 0);
				world.placeNew(new Vector2i(3, 2), 1, 0);
				world.placeNew(new Vector2i(3, 1), 1, 0);
				world.placeNew(new Vector2i(3, 0), 1, 0);
				world.placeNew(new Vector2i(3, -1), 1, 1);

				world.placeNew(new Vector2i(14, -3), 4, 0);
				world.placeNew(new Vector2i(14, -4), 1, 0);
				world.placeNew(new Vector2i(14, -5), 1, 0);
				world.placeNew(new Vector2i(14, -6), 1, 0);
				world.placeNew(new Vector2i(14, -7), 1, 0);
				world.placeNew(new Vector2i(14, -8), 1, 0);
				world.placeNew(new Vector2i(14, -9), 6, 0);
				
				world.placeNew(new Vector2i(4, -1), 4, 0);
				world.placeNew(new Vector2i(4, -2), 1, 0);
				world.placeNew(new Vector2i(4, -3), 1, 0);
				world.placeNew(new Vector2i(4, -4), 1, 0);
				world.placeNew(new Vector2i(4, -5), 1, 0);
				world.placeNew(new Vector2i(4, -6), 1, 0);
				world.placeNew(new Vector2i(4, -7), 1, 0);
				world.placeNew(new Vector2i(4, -8), 1, 0);
				world.placeNew(new Vector2i(4, -9), 6, 0);

				world.placeNew(new Vector2i(13, -6), 5, 3);
				world.placeNew(new Vector2i(12, -6), 7, 0);
				world.interact(new Vector2i(12, -6));
				world.placeNew(new Vector2i(11, -6), 5, 3);
				world.placeNew(new Vector2i(10, -6), 1, 0);
				world.placeNew(new Vector2i(10, -7), 1, 0);
				world.placeNew(new Vector2i(10, -8), 1, 3);
				
				world.placeNew(new Vector2i(5, -6), 5, 1);
				world.placeNew(new Vector2i(6, -6), 7, 0);
				world.placeNew(new Vector2i(7, -6), 5, 1);
				world.placeNew(new Vector2i(8, -6), 1, 0);
				world.placeNew(new Vector2i(8, -7), 1, 0);
				world.placeNew(new Vector2i(8, -8), 1, 1);

				world.placeNew(new Vector2i(9, -8), 7, 0);
				world.interact(new Vector2i(9, -8));
				world.interact(new Vector2i(9, -8));
				world.placeNew(new Vector2i(9, -9), 5, 0);
				world.placeNew(new Vector2i(9, -10), 1, 0);
				world.placeNew(new Vector2i(9, -11), 1, 0);
				world.placeNew(new Vector2i(9, -12), 6, 0);

				world.placeNew(new Vector2i(7, -16), 12, 0);
				world.placeNew(new Vector2i(9, -16), 12, 0);
				world.placeNew(new Vector2i(11, -16), 12, 0);
				world.placeNew(new Vector2i(4, -18), 11, 0);
				world.placeNew(new Vector2i(5, -18), 11, 0);
				world.placeNew(new Vector2i(6, -18), 11, 0);
				world.placeNew(new Vector2i(7, -18), 11, 0);
				world.placeNew(new Vector2i(8, -18), 11, 0);
				world.placeNew(new Vector2i(9, -18), 11, 0);
				world.placeNew(new Vector2i(10, -18), 11, 0);
				world.placeNew(new Vector2i(11, -18), 11, 0);
				world.placeNew(new Vector2i(12, -18), 11, 0);
				world.placeNew(new Vector2i(13, -18), 11, 0);
				world.placeNew(new Vector2i(14, -18), 11, 0);
				world.placeNew(new Vector2i(8, -24), 13, 0);
				world.placeNew(new Vector2i(10, -24), 13, 0);
				world.placeNew(new Vector2i(7, -25), 13, 0);
				world.placeNew(new Vector2i(9, -25), 13, 0);
				world.placeNew(new Vector2i(11, -25), 13, 0);
				world.placeNew(new Vector2i(6, -26), 13, 0);
				world.placeNew(new Vector2i(8, -26), 13, 0);
				world.placeNew(new Vector2i(10, -26), 13, 0);
				world.placeNew(new Vector2i(12, -26), 13, 0);


			}
		});

		glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
			if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_3) == GLFW_PRESS) {
				camera.rotation -= ((float)xpos - cursorPos.x) * 0.005f;
				camera.angle -= ((float)ypos - cursorPos.y) * 0.005f;
				if (camera.angle > (float)Math.PI / 2f - 0.1f) {
					camera.angle = (float)Math.PI / 2f - 0.1f;
				} else if (camera.angle < 0.1f) {
					camera.angle = 0.1f;
				}
			}

			cursorPos = new Vector2i((int)xpos, (int)ypos);
		});

		glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
				Vector3f worldPos = camera.screenToWorldPos(cursorPos);
				Vector2i tilePos = world.worldToTilePos(worldPos);
				if (tilePos != null) {
					if (!world.getTile(tilePos).free) {
						world.interact(tilePos);
					} else {
						world.placeNew(tilePos, App.player.selectedItem, App.player.itemRotation);
					}
				}
			}
			if (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS) {
				Vector3f worldPos = camera.screenToWorldPos(cursorPos);
				Vector2i tilePos = world.worldToTilePos(worldPos);
				if (tilePos != null) {
					world.deconstruct(tilePos);
				}
			}
		});
		
		glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
			if (yoffset > 0) {
				camera.radius *= 0.8f;
			} else if (yoffset < 0) {
				camera.radius *= 1.25f;
			}
		});

		glfwSetCursorEnterCallback(window, (window, entered) -> {
			if (!entered) {
				cursorPos = null;
			}
		});

		glfwMakeContextCurrent(window);
		glfwSwapInterval(0);
		glfwShowWindow(window);

		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glViewport(0, 0, width, height);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		resources = new Resources();
		resources.init();

		items = new ArrayList<Item>();
		Item item = new Item();
		item.id = 0;
		item.name = "iron ore";
		item.color = new Vector3f(0.2f, 0.2f, 0.5f);
		items.add(item);
		item = new Item();
		item.id = 1;
		item.name = "copper ore";
		item.color = new Vector3f(0.5f, 0.2f, 0.2f);
		items.add(item);
		item = new Item();
		item.id = 2;
		item.name = "coal";
		item.color = new Vector3f(0.05f, 0.05f, 0.05f);
		items.add(item);
		item = new Item();
		item.id = 3;
		item.name = "iron plate";
		item.color = new Vector3f(0.2f, 0.2f, 0.8f);
		items.add(item);
		item = new Item();
		item.id = 4;
		item.name = "copper plate";
		item.color = new Vector3f(0.8f, 0.2f, 0.2f);
		items.add(item);
		item = new Item();
		item.id = 5;
		item.name = "science stuff";
		item.color = new Vector3f(0.2f, 0.8f, 0.2f);
		items.add(item);
		
		recipes = new ArrayList<Recipe>();
		ArrayList<ItemStack> recipeInput = new ArrayList<ItemStack>();
		ArrayList<ItemStack> recipeOutput = new ArrayList<ItemStack>();
		ItemStack stack = new ItemStack();
		stack.item = App.items.get(0);
		stack.amount = 1;
		recipeInput.add(stack);
		stack = new ItemStack();
		stack.item = App.items.get(2);
		stack.amount = 1;
		recipeInput.add(stack);
		stack = new ItemStack();
		stack.item = App.items.get(3);
		stack.amount = 1;
		recipeOutput.add(stack);
		recipes.add(new Recipe(recipeInput, recipeOutput, 1, 60));
		
		recipeInput = new ArrayList<ItemStack>();
		recipeOutput = new ArrayList<ItemStack>();
		stack = new ItemStack();
		stack.item = App.items.get(1);
		stack.amount = 1;
		recipeInput.add(stack);
		stack = new ItemStack();
		stack.item = App.items.get(2);
		stack.amount = 1;
		recipeInput.add(stack);
		stack = new ItemStack();
		stack.item = App.items.get(4);
		stack.amount = 1;
		recipeOutput.add(stack);
		recipes.add(new Recipe(recipeInput, recipeOutput, 1, 60));
		
		recipeInput = new ArrayList<ItemStack>();
		recipeOutput = new ArrayList<ItemStack>();
		stack = new ItemStack();
		stack.item = App.items.get(3);
		stack.amount = 2;
		recipeInput.add(stack);
		stack = new ItemStack();
		stack.item = App.items.get(4);
		stack.amount = 2;
		recipeInput.add(stack);
		stack = new ItemStack();
		stack.item = App.items.get(5);
		stack.amount = 1;
		recipeOutput.add(stack);
		recipes.add(new Recipe(recipeInput, recipeOutput, 2, 120));

		ui = new Ui();
		ui.init();
		world = new World();
		world.size = new Vector2i(100, 100);
		world.init();
		player = new Player(new Vector3f(0f, 0f, 0f));
		player.init();
		camera = new Camera();

		Label infoLabel = new Label() {
			public void update() {
				text = "";
				text += String.format("%.6f", time) + "\n";
				text += tick + ", " + String.format("%.6f", deltaTick) + ", " + String.format("%.2f", targetTickRate) + ", " + String.format("%.2f", actualTickRate) + "\n";
				text += frame + ", " + String.format("%.6f", deltaFrame) + ", " + String.format("%.2f", targetFrameRate) + ", " + String.format("%.2f", actualFrameRate) + "\n";
				text += String.format("%.3f", player.position.x) + ", " + String.format("%.3f", player.position.y) + "\n";

				if (cursorPos != null) {
					text += cursorPos.x + ", " + cursorPos.y + "\n";
				} else {
					text += "null\n";
				}

				Vector3f worldPos = camera.screenToWorldPos(cursorPos);
				if (worldPos != null) {
					text += String.format("%.3f", worldPos.x) + ", " + String.format("%.3f", worldPos.y) + "\n";
				} else {
					text += "null\n";
				}

				Vector2i tilePos = world.worldToTilePos(worldPos);
				if (tilePos != null) {
					text += tilePos.x + ", " + tilePos.y + "\n";
				} else {
					text += "null\n";
				}

				Tile tile = world.getTile(tilePos);
				if (tile != null) {
					text += tile.type + ", " + tile.free + "\n";
				} else {
					text += "null\n";
				}

				text += player.selectedItem + ", " + player.itemRotation + "\n";
				text += world.buildings.size() + "\n";
				text += ui.elements.size() + "\n";

				updateMesh();
			}
		};
		infoLabel.position = new Vector2f(10f);
		infoLabel.size = new Vector2f(40f);
		infoLabel.color = new Vector3f(1f);
		infoLabel.fontAtlas = resources.arialTexture;
		infoLabel.init();
		ui.addElement(infoLabel);

	}

	private void loop() {
		while ( !glfwWindowShouldClose(window) ) {
			time = (float)glfwGetTime();
			deltaTick = time - lastTick;
			deltaFrame = time - lastFrame;

			if (deltaTick >= 1f / targetTickRate) {
				lastTick = time;
				actualTickRate = 1f / deltaTick;
				tick += 1;
				update();
			}

			if (deltaFrame >= 1f / targetFrameRate) {
				lastFrame = time;
				actualFrameRate = 1f / deltaFrame;
				frame += 1;
				draw();
			}

			glfwPollEvents();
		}
	}

	private void update() {
		world.update();
		player.update();
		camera.update();
		ui.update();
	}

	private void draw() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		world.draw();
		player.draw();
		ui.draw();

		glfwSwapBuffers(window);
	}

	private void cleanup() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();

		GLFWErrorCallback cb = glfwSetErrorCallback(null);
		if (cb != null) {
			cb.free();
		}
	}

}
