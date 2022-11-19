package faketorio;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class App {
	long window;
	int width = 1920;
	int height = 1080;
	int vertexShader;
	int fragmentShader;
	int shaderProgram;
	double time;
	double deltaTime;
	Camera camera;
	ArrayList<Entity> entities;

	public static void main(String[] args) {
		new App().run();
	}

	public void run() {
		init();
		loop();
		cleanup();
	}
	
	private void init() {
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

		glfwMakeContextCurrent(window);
		glfwSwapInterval(0);
		glfwShowWindow(window);

		GL.createCapabilities();
		glViewport(0, 0, width, height);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		try {
			vertexShader = glCreateShader(GL_VERTEX_SHADER);
			String vertexSource = new String(Files.readAllBytes(Paths.get("app/src/main/resources/shader.vs")));
			glShaderSource(vertexShader, vertexSource);
			glCompileShader(vertexShader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
			String fragmentSource = new String(Files.readAllBytes(Paths.get("app/src/main/resources/shader.fs")));
			glShaderSource(fragmentShader, fragmentSource);
			glCompileShader(fragmentShader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		glBindFragDataLocation(shaderProgram, 0, "fragColor");
		glLinkProgram(shaderProgram);

		camera = new Camera();
		entities = new ArrayList<Entity>();
		entities.add(new Entity(shaderProgram));
		entities.add(new Entity(shaderProgram));
		entities.get(1).model.translate(0f, 1f, 0f);
	}

	private void loop() {
		while ( !glfwWindowShouldClose(window) ) {
			deltaTime = glfwGetTime() - time;
			time = glfwGetTime();
			System.out.println((String.format("%.6f", time) + ", " + String.format("%.6f", deltaTime)));

			camera.update(window, deltaTime);
			
			for (Entity entity : entities) {
				entity.update(deltaTime);
			}

			draw();

			glfwPollEvents();
		}
	}

	private void draw() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glUseProgram(shaderProgram);
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "view"), false, camera.view.get(stack.mallocFloat(16)));
			glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "projection"), false, camera.projection.get(stack.mallocFloat(16)));
		}

		for (Entity entity : entities) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "model"), false, entity.model.get(stack.mallocFloat(16)));
			}
			
			glBindVertexArray(entity.vao);
			glDrawArrays(GL_TRIANGLES, 0, 3);
			glBindVertexArray(0);
		}

		glUseProgram(0);
		
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
