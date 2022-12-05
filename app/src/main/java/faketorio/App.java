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

	public static Vector2f cursorPos = new Vector2f(0f, 0f);

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

			cursorPos = new Vector2f((float)xpos, (float)ypos);
		});

		glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
				Vector3f worldPos = camera.screenToWorldPos(cursorPos);
				Vector2i tilePos = world.worldToTilePos(worldPos);
				if (tilePos != null) {
					world.placeNew(tilePos, App.player.item);
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

		world = new World();
		world.size = new Vector2i(100, 100);
		world.init();
		player = new Player();
		player.tint = new Vector3f(1f, 0f, 0f);
		player.init();
		camera = new Camera();
		ui = new Ui();
		ui.init();

		Quad testQuad = new Quad();
		testQuad.position = new Vector3f(100f, 100f, 0f);
		testQuad.size = new Vector3f(100f, 100f, 0f);
		testQuad.tint = new Vector3f(1f, 1f, 0f);
		testQuad.init();
		ui.addElement(testQuad);
		
		TexturedQuad testTexturedQuad = new TexturedQuad();
		testTexturedQuad.position = new Vector3f(300f, 100f, 0f);
		testTexturedQuad.size = new Vector3f(100f, 100f, 0f);
		testTexturedQuad.tint = new Vector3f(0f, 1f, 1f);
		testTexturedQuad.texture = testTexture;
		testTexturedQuad.init();
		ui.addElement(testTexturedQuad);

		Label testLabel = new Label();
		testLabel.position = new Vector3f(100f, 300f, 0f);
		testLabel.size = new Vector3f(100f, 100f, 0f);
		testLabel.tint = new Vector3f(1f, 0f, 1f);
		testLabel.fontAtlas = arialAtlas;
		testLabel.text = "test - ijkl \n123! %$[;]";
		testLabel.init();
		ui.addElement(testLabel);

		Triangle testTriangle = new Triangle();
		testTriangle.position = new Vector3f(0f, 0f, 0f);
		testTriangle.tint = new Vector3f(-1f, -1f, -1f);
		testTriangle.init();
		world.placeEntity(new Vector2i((int)Math.floor(testTriangle.position.x), (int)Math.floor(testTriangle.position.y)), testTriangle);

		Cube testCube = new Cube();
		testCube.position = new Vector3f(4f, 8f, 0f);
		testCube.tint = new Vector3f(0f, 0f, 1f);
		testCube.init();
		world.placeEntity(new Vector2i((int)Math.floor(testCube.position.x), (int)Math.floor(testCube.position.y)), testCube);
		
		Sphere testSphere = new Sphere();
		testSphere.position = new Vector3f(5f, 3f, 0f);
		testSphere.tint = new Vector3f(1f, 0f, 1f);
		testSphere.init();
		world.placeEntity(new Vector2i((int)Math.floor(testSphere.position.x), (int)Math.floor(testSphere.position.y)), testSphere);
		
		Label testTetheredLabel = new Label();
		testTetheredLabel.position = new Vector3f(100f, 300f, 0f);
		testTetheredLabel.size = new Vector3f(24f, 24f, 0f);
		testTetheredLabel.tint = new Vector3f(1f, 1f, 1f);
		testTetheredLabel.fontAtlas = arialAtlas;
		testTetheredLabel.text = "this is a funky ball\nlook at him go!";
		testTetheredLabel.tether = testSphere;
		testTetheredLabel.tetherWorldOffset = new Vector3f(0f, 0f, 1f);
		testTetheredLabel.init();
		ui.addElement(testTetheredLabel);
	}

	private void loop() {
		while ( !glfwWindowShouldClose(window) ) {
			deltaTime = (float)glfwGetTime() - time;
			time = (float)glfwGetTime();
			System.out.println((String.format("%.6f", time) + ", " + String.format("%.6f", deltaTime)));
			// System.out.println(world.entities.size());

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
