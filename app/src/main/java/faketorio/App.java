package faketorio;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.Configuration;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class App {
	public static long window = 0;
	public static int width = 1920;
	public static int height = 1080;

	public static int shaderProgram = 0;

	public static float time = 0.0f;
	public static float deltaTime = 0.0f;

	public static Vector2f cursorPos = new Vector2f(0f, 0f);

	public static World world;
	public static Player player;
	public static Camera camera;
	public static ArrayList<Entity> entities;

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
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		window = glfwCreateWindow(width, height, "test", NULL, NULL);

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
				glfwSetWindowShouldClose(window, true);
			}
		});

		glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
			if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_2) == GLFW_PRESS) {
				camera.rotation -= ((float)xpos - cursorPos.x) * 0.005f;
				camera.angle -= ((float)ypos - cursorPos.y) * 0.005f;
				if (camera.angle > (float)Math.PI / 2f - 0.1f) {
					camera.angle = (float)Math.PI / 2f - 0.1f;
				} else if (camera.angle < 0.1f) {
					camera.angle = 0.1f;
				}
			}

			cursorPos.x = (float)xpos;
			cursorPos.y = (float)ypos;
		});

		glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
				Cube cube = new Cube();
				cube.model.translate(new Vector3f(player.position).floor());
				entities.add(cube);
			}
			if (button == GLFW_MOUSE_BUTTON_3 && action == GLFW_PRESS) {
				camera.rotation = -(float)Math.PI / 2f;
				camera.angle = (float)Math.PI / 4f;
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
		glfwSwapInterval(0);
		glfwShowWindow(window);

		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glViewport(0, 0, width, height);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		String vertexSource = "";
		try {
			Scanner scanner = new Scanner(new File("app/src/main/resources/shader.vs"));
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
			Scanner scanner = new Scanner(new File("app/src/main/resources/shader.fs"));
			fragmentSource = scanner.useDelimiter("\\Z").next();
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, fragmentSource);
		glCompileShader(fragmentShader);

		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		glBindFragDataLocation(shaderProgram, 0, "fragColor");
		glLinkProgram(shaderProgram);

		world = new World();
		player = new Player();
		camera = new Camera();
		entities = new ArrayList<Entity>();

		Entity testTriangle = new Triangle();
		testTriangle.model.translate(0f, 0f, 1f);
		entities.add(testTriangle);

		Entity testCube = new Cube();
		testCube.model.translate(4f, 8f, 0f);
		entities.add(testCube);
	}

	private void loop() {
		while ( !glfwWindowShouldClose(window) ) {
			deltaTime = (float)glfwGetTime() - time;
			time = (float)glfwGetTime();
			System.out.println((String.format("%.6f", time) + ", " + String.format("%.6f", deltaTime)));

			update();
			draw();

			glfwPollEvents();
		}
	}

	private void update() {
		world.update();
		player.update();
		camera.update();
		for (Entity entity : entities) {
			entity.update();
		}
	}

	private void draw() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		world.draw();
		player.draw();
		for (Entity entity : entities) {
			entity.draw();
		}

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
