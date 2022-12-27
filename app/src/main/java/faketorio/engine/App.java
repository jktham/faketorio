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
						world.placeNew(tilePos, App.player.selectedItem);
					}
				}
			}
			if (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS) {
				Vector3f worldPos = camera.screenToWorldPos(cursorPos);
				Vector2i tilePos = world.worldToTilePos(worldPos);
				if (tilePos != null) {
					world.destroy(tilePos);
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
