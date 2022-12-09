package faketorio;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class App {
	public static long window = 0;
	public static int width = 1920;
	public static int height = 1080;

	public static int baseShader = 0;
	public static int instanceShader = 0;
	public static int uiShader = 0;
	public static int uiTexturedShader = 0;

	public static int testTexture = 0;
	public static int arialAtlas = 0;

	public static float time = 0.0f;
	public static float deltaTime = 0.0f;
	public static ArrayList<Float> frames = new ArrayList<Float>();
	public static float frameRate = 0.0f;
	public static int tick = 0;
	public static float lastTick = 0.0f;
	public static float desiredTickRate = 10.0f;

	public static Vector2i cursorPos = new Vector2i(0);

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

		window = glfwCreateWindow(width, height, "test", NULL, NULL);

		glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			App.width = width;
			App.height = height;
			glViewport(0, 0, width, height);
		});

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
				glfwSetWindowShouldClose(window, true);
			}
			if (key == GLFW_KEY_0 && action == GLFW_PRESS) {
				player.item = 0;
			}
			if (key == GLFW_KEY_1 && action == GLFW_PRESS) {
				player.item = 1;
			}
			if (key == GLFW_KEY_2 && action == GLFW_PRESS) {
				player.item = 2;
			}
			if (key == GLFW_KEY_3 && action == GLFW_PRESS) {
				player.item = 3;
			}
			if (key == GLFW_KEY_4 && action == GLFW_PRESS) {
				player.item = 4;
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
					if (player.item == 0) {
						world.interact(tilePos);
					} else {
						world.placeNew(tilePos, App.player.item);
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
		glfwSwapInterval(1);
		glfwShowWindow(window);

		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glViewport(0, 0, width, height);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		baseShader = compileShader("shaders/world_base");
		instanceShader = compileShader("shaders/world_instanced");
		uiShader = compileShader("shaders/ui_base");
		uiTexturedShader = compileShader("shaders/ui_textured");

		testTexture = loadTexture("textures/test.png");
		arialAtlas = loadTexture("fonts/arial.png");

		items = new ArrayList<Item>();
		Item item = new Item();
		item.id = 0;
		item.name = "iron ore";
		items.add(item);
		item = new Item();
		item.id = 1;
		item.name = "copper ore";
		items.add(item);

		ui = new Ui();
		ui.init();
		world = new World();
		world.size = new Vector2i(100, 100);
		world.init();
		player = new Player();
		player.color = new Vector3f(1f, 0f, 0f);
		player.name = "player";
		player.init();
		camera = new Camera();

		Label infoLabel = new Label() {
			public void instanceUpdate() {
				text = "";
				text += String.format("%.6f", time) + ", " + String.format("%.6f", deltaTime) + ", " + String.format("%.6f", frameRate) + "\n";
				text += tick + ", " + String.format("%.6f", lastTick) + "\n";
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

				text += player.item + "\n";
				text += world.entities.size() + "\n";
				text += ui.elements.size() + "\n";

				updateMesh();
			}
		};
		infoLabel.position = new Vector2f(10f);
		infoLabel.size = new Vector2f(40f);
		infoLabel.color = new Vector3f(1f);
		infoLabel.fontAtlas = arialAtlas;
		infoLabel.init();
		ui.addElement(infoLabel);

	}

	private void loop() {
		while ( !glfwWindowShouldClose(window) ) {
			deltaTime = (float)glfwGetTime() - time;
			time = (float)glfwGetTime();
			frames.add(deltaTime);
			if (frames.size() > 60) {
				frames.remove(0);
			}
			float frameSum = 0f;
			for (int i=0;i<frames.size();i++) {
				frameSum += frames.get(i);
			}
			frameRate = 1f / (frameSum / (float)frames.size());

			if (time - lastTick >= 1f / desiredTickRate) {
				tick += 1;
				lastTick = time;
			}

			update();
			draw();

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

	private int compileShader(String file) {
		String vertexSource = "";
		try {
			Scanner scanner = new Scanner(new File("app/src/main/resources/" + file + ".vs"));
			vertexSource = scanner.useDelimiter("\\Z").next();
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, vertexSource);
		glCompileShader(vertexShader);

		String fragmentSource = "";
		try {
			Scanner scanner = new Scanner(new File("app/src/main/resources/" + file + ".fs"));
			fragmentSource = scanner.useDelimiter("\\Z").next();
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, fragmentSource);
		glCompileShader(fragmentShader);

		int shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		glBindFragDataLocation(shaderProgram, 0, "fragColor");
		glLinkProgram(shaderProgram);

		return shaderProgram;
	}

	private int loadTexture(String file) {
		ByteBuffer image;
        int width, height;
		String path = new File("app/src/main/resources/" + file).getAbsolutePath();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            //stbi_set_flip_vertically_on_load(true);
            image = stbi_load(path, w, h, comp, 4);

            width = w.get();
            height = h.get();
        }

		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		glGenerateMipmap(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		stbi_image_free(image);

		return texture;
	}

}
