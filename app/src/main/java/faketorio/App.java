package faketorio;

import org.joml.Matrix4f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
	long window;
	int width = 1920;
	int height = 1080;
	int vertexShader;
	int fragmentShader;
	int shaderProgram;
	int vao;
	int vbo;
	Matrix4f model;
	Matrix4f view;
	Matrix4f projection;
	double time;
	double deltaTime;

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
		glfwSwapInterval(1);
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

		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer vertices = stack.mallocFloat(3 * 6);
			vertices.put(-0.5f).put(-0.5f).put(0f).put(1f).put(0f).put(0f);
			vertices.put(0.5f).put(-0.5f).put(0f).put(0f).put(1f).put(0f);
			vertices.put(0f).put(0.8f).put(0f).put(0f).put(0f).put(1f);
			vertices.flip();

			glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		}

		int floatSize = 4;
		int aPos = glGetAttribLocation(shaderProgram, "position");
		glEnableVertexAttribArray(aPos);
		glVertexAttribPointer(aPos, 3, GL_FLOAT, false, 6 * floatSize, 0);

		int aCol = glGetAttribLocation(shaderProgram, "color");
		glEnableVertexAttribArray(aCol);
		glVertexAttribPointer(aCol, 3, GL_FLOAT, false, 6 * floatSize, 3 * floatSize);

		glBindVertexArray(0);

		model = new Matrix4f();
		view = new Matrix4f().translate(0f, 0f, -1f);
		projection = new Matrix4f().perspective(90f, width/height, 0f, 100f);

	}

	private void loop() {
		while ( !glfwWindowShouldClose(window) ) {
			deltaTime = glfwGetTime() - time;
			time = glfwGetTime();
			System.out.println((String.format("%.4f", time) + ", " + String.format("%.4f", deltaTime)));

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			glUseProgram(shaderProgram);
			glBindVertexArray(vao);
			
			try (MemoryStack stack = MemoryStack.stackPush()) {
				glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "model"), false, model.get(stack.mallocFloat(16)));
				glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "view"), false, view.get(stack.mallocFloat(16)));
				glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "projection"), false, projection.get(stack.mallocFloat(16)));
			}

			glDrawArrays(GL_TRIANGLES, 0, 3);

			glBindVertexArray(0);
			glUseProgram(0);

			glfwSwapBuffers(window);
			glfwPollEvents();
		}
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
