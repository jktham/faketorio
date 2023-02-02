package faketorio.engine;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Scanner;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class Resources {
	public int worldBaseShader;
	public int worldInstancedShader;
	public int worldLineShader;
	public int uiBaseShader;
	public int uiTexturedShader;

	public int testTexture;
	public int arialTexture;
	
	public Model emptyModel;
	public Model cubeModel;
	public Model playerModel;
	public Model triangleModel;
	public Model sphereModel;
	public Model minerModel;
	public Model monkeModel;
	public Model chestModel;
	public Model beltModel;
	public Model splitterModel;
	public Model mergerModel;
	public Model throwerModel;
	public Model extractorModel;
	public Model assemblerModel;
	public Model spawnerModel;
	public Model enemyModel;
	public Model wallModel;
	public Model turretModel;
	
	public void init() {
		worldBaseShader = compileShader("shaders/world_base");
		worldInstancedShader = compileShader("shaders/world_instanced");
		worldLineShader = compileShader("shaders/world_line");
		uiBaseShader = compileShader("shaders/ui_base");
		uiTexturedShader = compileShader("shaders/ui_textured");

		testTexture = loadTexture("textures/test.png");
		arialTexture = loadTexture("fonts/arial.png");

		emptyModel = new Model();
		cubeModel = new Model(worldBaseShader, "models/cube.obj");
		playerModel = new Model(worldBaseShader, "models/player.obj");
		triangleModel = new Model(worldBaseShader, "models/triangle.obj");
		sphereModel = new Model(worldBaseShader, "models/sphere.obj");
		minerModel = new Model(worldBaseShader, "models/miner.obj");
		monkeModel = new Model(worldBaseShader, "models/monke.obj");
		chestModel = new Model(worldBaseShader, "models/chest.obj");
		beltModel = new Model(worldBaseShader, "models/belt.obj");
		splitterModel = new Model(worldBaseShader, "models/splitter.obj");
		mergerModel = new Model(worldBaseShader, "models/merger.obj");
		throwerModel = new Model(worldBaseShader, "models/thrower.obj");
		extractorModel = new Model(worldBaseShader, "models/extractor.obj");
		assemblerModel = new Model(worldBaseShader, "models/assembler.obj");
		spawnerModel = new Model(worldBaseShader, "models/spawner.obj");
		enemyModel = new Model(worldBaseShader, "models/enemy.obj");
		wallModel = new Model(worldBaseShader, "models/wall.obj");
		turretModel = new Model(worldBaseShader, "models/turret.obj");
	}

	public int compileShader(String path) {
		String vertexSource = "";
		try {
			Scanner scanner = new Scanner(new File("app/src/main/resources/" + path + ".vs"));
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
			Scanner scanner = new Scanner(new File("app/src/main/resources/" + path + ".fs"));
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

	public int loadTexture(String path) {
		ByteBuffer image;
        int width, height;
		String absPath = new File("app/src/main/resources/" + path).getAbsolutePath();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            //stbi_set_flip_vertically_on_load(true);
            image = stbi_load(absPath, w, h, comp, 4);

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

	public FloatBuffer loadMesh(MemoryStack stack, String path, Model model) {
		ArrayList<ArrayList<Float>> positions = new ArrayList<ArrayList<Float>>();
		ArrayList<ArrayList<Float>> colors = new ArrayList<ArrayList<Float>>();
		ArrayList<ArrayList<Float>> normals = new ArrayList<ArrayList<Float>>();
		ArrayList<ArrayList<Float>> verts = new ArrayList<ArrayList<Float>>();
		try {
			Scanner scanner = new Scanner(new File("app/src/main/resources/" + path));
			while (scanner.hasNextLine()) {
				Scanner line = new Scanner(scanner.nextLine());
				if (line.hasNext()) {
					String lineType = line.next();
					if (lineType.equals("o")) {
						model.meshSizes.add(0);
						model.meshOffsets.add(verts.size());
						model.meshTransforms.add(new Matrix4f());
						model.meshColors.add(new Vector3f(-1f));
						model.meshHidden.add(false);
					} else if (lineType.equals("v")) {
						ArrayList<Float> position = new ArrayList<Float>();
						position.add(line.nextFloat());
						position.add(line.nextFloat());
						position.add(line.nextFloat());
						positions.add(position);
						if (line.hasNextFloat()) {
							ArrayList<Float> color = new ArrayList<Float>();
							color.add(line.nextFloat());
							color.add(line.nextFloat());
							color.add(line.nextFloat());
							colors.add(color);
						} else {
							ArrayList<Float> color = new ArrayList<Float>();
							color.add(1f);
							color.add(1f);
							color.add(1f);
							colors.add(color);
						}
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
		
							int colorIndex = Integer.parseInt(vertIndex.split("/")[0]);
							vert.add(colors.get(colorIndex-1).get(0));
							vert.add(colors.get(colorIndex-1).get(1));
							vert.add(colors.get(colorIndex-1).get(2));
		
							verts.add(vert);

							model.meshSizes.set(model.meshSizes.size()-1, model.meshSizes.get(model.meshSizes.size()-1)+1);
						}
					}
				}
				line.close();
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		FloatBuffer mesh = stack.mallocFloat(9 * verts.size());
		for (ArrayList<Float> vert : verts) {
			for (float v : vert) {
				mesh.put(v);
			}
		}
		mesh.flip();
		return mesh;
	}
}
