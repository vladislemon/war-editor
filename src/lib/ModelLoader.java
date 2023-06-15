package lib;

/*
 * .OBJ format model loader made by miker9(Andrew silkin) on 02.04.2013
 * Converts .OBJ files into buffers later to be used for rendering VBO's
 * You are allowed to copy, modify and spread code, if you specify the author
 * 
 * You can contact me on 00miker9@gmail.com
 * */




import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import app.Editor;


public class ModelLoader {
	
	private static ArrayList<float[]> vertice = new ArrayList<float[]>();
	private static ArrayList<float[]> normal = new ArrayList<float[]>();
	private static ArrayList<float[]> texcoord = new ArrayList<float[]>();
	private static ArrayList<int[]> faces = new ArrayList<int[]>();
	
	private static HashMap<String, Model> models = new HashMap<String, Model>();
	
	
	public static void load() {
		File[] fileList;
		fileList = getFiles();
		
		for(int i=0; i<fileList.length; i++) {
				models.put(fileList[i].getName(), loadModel("/res/models/"+fileList[i].getName()));
		}
		
		vertice = null;
		normal = null;
		texcoord = null;
		faces = null;
	}
	
	
	public static Model loadModel(String path) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(".").getCanonicalPath()+path);
		} catch (IOException e) {
			Editor.onError(e);
		}
		BufferedReader br = new BufferedReader(fr);
		cleanup();
		
		try {
			Model m = loadObject(br);
			fr.close();
			br.close();
			return m;
		} catch (IOException e) {
			Editor.onError(e);
		}
		
		return null;
	}
	
	public static Model get(String s) {
		return models.get(s);
	}
	
	public static int getModelNum() {
		return models.size();
	}
	
	public static int getSRCModelNum() {
		return getFiles().length;
	}
	
	private static File[] getFiles() {
		File[] fileList;
		fileList = new File("res/models").listFiles();
		int texcount=0;
		for(int i=0; i<fileList.length; i++) {
			if(fileList[i].getName().contains(".obj")) {
				texcount++;
			}
		}
		
		File[] texfiles = new File[texcount];
		int x=0;
		for(int i=0; i<fileList.length; i++) {
			if(fileList[i].getName().contains(".obj")) {
				texfiles[x] = fileList[i];
				x++;
			}
		}
		return texfiles;
	}
	
	
	private static void cleanup() {
		vertice.clear();
		normal.clear();
		texcoord.clear();
		faces.clear();
	}
	
	
	private static Model loadObject(BufferedReader br) throws IOException {
		String line;
		
		while ((line = br.readLine()) != null) {
			line = line.trim();
			
			if(line.isEmpty()) {
				continue;
			}
			
	String type = Util.getWord(line, 1);
			
			if(type.equals("v")) {
				float components[] = new float[3];
				components[0] =  Float.parseFloat(Util.getWord(line, 2));
				components[1] =  Float.parseFloat(Util.getWord(line, 3));
				components[2] =  Float.parseFloat(Util.getWord(line, 4));
				vertice.add(components);
			}
			if(type.equals("vt")) {
				float components[] = new float[2];
				components[0] =  Float.parseFloat(Util.getWord(line, 2));
				components[1] =  1-Float.parseFloat(Util.getWord(line, 3));
				texcoord.add(components);
			}
			if(type.equals("vn")) {
				float components[] = new float[3];
				components[0] =  Float.parseFloat(Util.getWord(line, 2));
				components[1] =  Float.parseFloat(Util.getWord(line, 3));
				components[2] =  Float.parseFloat(Util.getWord(line, 4));
				normal.add(components);
			}
			if(type.equals("f")) {
				for(int i=0; i<3; i++) {
					String parts[] = Util.getWord(line, i+2).split("/");
					int components[] = new int[3];
					
					components[0] = Integer.parseInt(parts[0]);
					components[1] = Integer.parseInt(parts[1]);
					components[2] = Integer.parseInt(parts[2]);
					
					faces.add(components);
				}
			}
		}
		
		
		
		FloatBuffer vertices = Util.emptyFloatBuffer(faces.size()*3);
		FloatBuffer normals = Util.emptyFloatBuffer(faces.size()*3);
		FloatBuffer texcoords = Util.emptyFloatBuffer(faces.size()*2);
		
		for(int i=0; i<faces.size(); i++) {
			int components[] = faces.get(i);
			vertices.put(vertice.get(components[0]-1));
			texcoords.put(texcoord.get(components[1]-1));
			normals.put(normal.get(components[2]-1));
			
		}
		
		vertices.flip();
		texcoords.flip();
		normals.flip();
		
		int VBOVertex =Util.genGLBufferID();
		int VBOTexture = Util.genGLBufferID();
		int VBONormal = Util.genGLBufferID();
		
		Util.writeToVBO(VBOVertex, vertices);
		Util.writeToVBO(VBOTexture, texcoords);
		Util.writeToVBO(VBONormal, normals);
		
		Model m = new Model(VBOVertex, VBONormal, VBOTexture, 0, 0, 0, faces.size()/3, faces.size());
		return m;
		
	}
}
