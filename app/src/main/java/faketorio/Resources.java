package faketorio;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Resources {
	
	public int worldBaseShader = 0;
	public int worldInstancedShader = 0;
	public int uiBaseShader = 0;
	public int uiTexturedShader = 0;

	public int testTexture = 0;
	public int arialTexture = 0;
	
	public ArrayList<ArrayList<Float>> monkeVerts;
	
	public void init() {
		worldBaseShader = compileShader("shaders/world_base");
		worldInstancedShader = compileShader("shaders/world_instanced");
		uiBaseShader = compileShader("shaders/ui_base");
		uiTexturedShader = compileShader("shaders/ui_textured");

		testTexture = loadTexture("textures/test.png");
		arialTexture = loadTexture("fonts/arial.png");

		monkeVerts = loadModel("models/monke.obj");
	}

	public int compileShader(String file) {
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

	public int loadTexture(String file) {
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

	public ArrayList<ArrayList<Float>> loadModel(String file) {
		ArrayList<ArrayList<Float>> positions = new ArrayList<ArrayList<Float>>();
		ArrayList<ArrayList<Float>> normals = new ArrayList<ArrayList<Float>>();
		ArrayList<ArrayList<Float>> verts = new ArrayList<ArrayList<Float>>();
		try {
			Scanner scanner = new Scanner(new File("app/src/main/resources/" + file));
			while (scanner.hasNextLine()) {
				Scanner line = new Scanner(scanner.nextLine());
				if (line.hasNext()) {
					String lineType = line.next();
					if (lineType.equals("v")) {
						ArrayList<Float> position = new ArrayList<Float>();
						position.add(line.nextFloat());
						position.add(line.nextFloat());
						position.add(line.nextFloat());
						positions.add(position);
					} else if (lineType.equals("vn")) {
						ArrayList<Float> normal = new ArrayList<Float>();
						normal.add(line.nextFloat());
						normal.add(line.nextFloat());
						normal.add(line.nextFloat());
						normals.add(normal);
					} else if (lineType.equals("f")) {
						for (int i=0;i<3;i++) {
							ArrayList<Float> vert = new ArrayList<Float>();
							String vertIndex = line.next();

							int positionIndex = Integer.parseInt(vertIndex.split("/")[0]);
							vert.add(positions.get(positionIndex-1).get(0));
							vert.add(positions.get(positionIndex-1).get(1));
							vert.add(positions.get(positionIndex-1).get(2));
		
							int normalIndex = Integer.parseInt(vertIndex.split("/")[2]);
							vert.add(normals.get(normalIndex-1).get(0));
							vert.add(normals.get(normalIndex-1).get(1));
							vert.add(normals.get(normalIndex-1).get(2));
		
							vert.add(1f);
							vert.add(1f);
							vert.add(1f);
		
							verts.add(vert);
						}
					}
				}
				line.close();
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return verts;
	}
}
