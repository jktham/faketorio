package faketorio;

import org.joml.Vector2f;
import org.joml.Vector2i;
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
				Vector3f worldPos = camera.getCursorWorldPos();
				if (worldPos != null) {
					Vector3f tilePos = new Vector3f(worldPos).floor();
					if (world.tiles[(int)tilePos.x+world.size.x/2][(int)tilePos.y+world.size.y/2].free) {
						if (player.item == 1) {
							Cube cube = new Cube();
							cube.position = new Vector3f(tilePos.x, tilePos.y, 0f);
							cube.color = new Vector3f(0f, 0f, 1f);
							cube.init();
							world.entities.add(cube);
							world.tiles[(int)tilePos.x+world.size.x/2][(int)tilePos.y+world.size.y/2].free = false;
						} else if (player.item == 2) {
							Triangle triangle = new Triangle();
							triangle.position = new Vector3f(tilePos.x, tilePos.y, 0f);
							triangle.color = new Vector3f(-1f, -1f, -1f);
							triangle.init();
							world.entities.add(triangle);
							world.tiles[(int)tilePos.x+world.size.x/2][(int)tilePos.y+world.size.y/2].free = false;
						} else if (player.item == 3) {
							Sphere sphere = new Sphere();
							sphere.position = new Vector3f(tilePos.x, tilePos.y, 0f);
							sphere.color = new Vector3f(1f, 0f, 1f);
							sphere.init();
							world.entities.add(sphere);
							world.tiles[(int)tilePos.x+world.size.x/2][(int)tilePos.y+world.size.y/2].free = false;
						}
					}
				}
			}
			if (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS) {
				Vector3f worldPos = camera.getCursorWorldPos();
				if (worldPos != null) {
					Vector3f tilePos = new Vector3f(worldPos).floor();
					if (!world.tiles[(int)tilePos.x+world.size.x/2][(int)tilePos.y+world.size.y/2].free) {
						for (Entity e : world.entities) {
							if (new Vector2f(e.position.x, e.position.y).floor().equals(new Vector2f(tilePos.x, tilePos.y))) {
								world.entities.remove(e);
								world.tiles[(int)tilePos.x+world.size.x/2][(int)tilePos.y+world.size.y/2].free = true;
								break;
							}
						}
					}
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
		world.size = new Vector2i(100, 100);
		world.init();
		player = new Player();
		player.color = new Vector3f(1f, 0f, 0f);
		player.init();
		camera = new Camera();

		Triangle testTriangle = new Triangle();
		testTriangle.position = new Vector3f(0f, 0f, 0f);
		testTriangle.color = new Vector3f(-1f, -1f, -1f);
		testTriangle.init();
		world.entities.add(testTriangle);
		world.tiles[(int)Math.floor(testTriangle.position.x)+world.size.x/2][(int)Math.floor(testTriangle.position.y)+world.size.y/2].free = false;

		Cube testCube = new Cube();
		testCube.position = new Vector3f(4f, 8f, 0f);
		testCube.color = new Vector3f(0f, 0f, 1f);
		testCube.init();
		world.entities.add(testCube);
		world.tiles[(int)Math.floor(testCube.position.x)+world.size.x/2][(int)Math.floor(testCube.position.y)+world.size.y/2].free = false;
		
		Sphere testSphere = new Sphere();
		testSphere.position = new Vector3f(5f, 3f, 0f);
		testSphere.color = new Vector3f(1f, 0f, 1f);
		testSphere.init();
		world.entities.add(testSphere);
		world.tiles[(int)Math.floor(testSphere.position.x)+world.size.x/2][(int)Math.floor(testSphere.position.y)+world.size.y/2].free = false;
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
	}

	private void draw() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		world.draw();
		player.draw();

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
