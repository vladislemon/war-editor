package lib;

import org.lwjgl.opengl.GL30;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderLoader {
	
	private static HashMap<String, Integer> programs = new HashMap<String, Integer>();
	private static ArrayList<UniAttr> uniforms = new ArrayList<UniAttr>();
	private static ArrayList<UniAttr> attributes = new ArrayList<UniAttr>();
	private static int currentProgram = 0, mainProgram = 0;
	
	public static void setProgram(String name) {
		int program = 0;
		if(name.toLowerCase().equals("none")) {
			glUseProgram(0);
			currentProgram = 0;
		} else if(name.toLowerCase().equals("main")) {
			glUseProgram(mainProgram);
			currentProgram = mainProgram;
		} else if((program = programs.get(name)) != 0) {
			glUseProgram(program);
			currentProgram = program;
		}
	}
	
	public static void setMainProgram(String name) {
		int program = 0;
		if(name.toLowerCase().equals("none")) {
			mainProgram = 0;
		} else if((program = getProgram(name)) != 0) {
			mainProgram = program;
		}
	}
	
	public static int getCurrentProgram() {
		return currentProgram;
	}
	
	public static int getMainProgram() {
		return mainProgram;
	}
	
    public static int getAttributeLocation(String s) {
    	return glGetAttribLocation(currentProgram, s);
    }
	
	public static void setUniform(String name, int count, int par1, int par2, int par3, int par4) {
		int location = -1;
		for(int i = 0; i < uniforms.size(); i++) {
			if(uniforms.get(i).getName().equals(name) && uniforms.get(i).getProgram() == currentProgram) {
				location = uniforms.get(i).getId();
				if(location == -1)
					return;
				switch(count) {
				case 1: glUniform1i(location, par1); break;
				case 2: glUniform2i(location, par1, par2); break;
				case 3: glUniform3i(location, par1, par2, par3); break;
				case 4: glUniform4i(location, par1, par2, par3, par4); break;
				}
			}
		}
	}
	
	public static void setUniform(String name, int count, float par1, float par2, float par3, float par4) {
		int location = -1;
		for(int i = 0; i < uniforms.size(); i++) {
			if(uniforms.get(i).getName().equals(name) && uniforms.get(i).getProgram() == currentProgram) {
				location = uniforms.get(i).getId();
				if(location == -1)
					return;
				switch(count) {
				case 1: glUniform1f(location, par1); break;
				case 2: glUniform2f(location, par1, par2); break;
				case 3: glUniform3f(location, par1, par2, par3); break;
				case 4: glUniform4f(location, par1, par2, par3, par4); break;
				
				}
			}
		}
	}
	
	public static void setUniformMatrix(String name, FloatBuffer par1) {
		int location = -1;
		for(int i = 0; i < uniforms.size(); i++) {
			if(uniforms.get(i).getName().equals(name) && uniforms.get(i).getProgram() == currentProgram) {
				location = uniforms.get(i).getId();
				if(location == -1)
					return;
				glUniformMatrix4(location, true, par1);
			}
		}
	}
	
	public static void setAttribute(String name, int count, short par1, short par2, short par3, short par4) {
		int location = -1;
		for(int i = 0; i < attributes.size(); i++) {
			if(attributes.get(i).getName().equals(name) && attributes.get(i).getProgram() == currentProgram) {
				location = attributes.get(i).getId();
				if(location == -1)
					return;
				switch(count) {
				case 1: glVertexAttrib1s(location, par1); break;
				case 2: glVertexAttrib2s(location, par1, par2); break;
				case 3: glVertexAttrib3s(location, par1, par2, par3); break;
				case 4: glVertexAttrib4s(location, par1, par2, par3, par4); break;
				}
			}
		}
	}
	
	public static void setAttribute(String name, int count, float par1, float par2, float par3, float par4) {
		int location = -1;
		for(int i = 0; i < attributes.size(); i++) {
			if(attributes.get(i).getName().equals(name) && attributes.get(i).getProgram() == currentProgram) {
				location = attributes.get(i).getId();
				if(location == -1)
					return;
				switch(count) {
				case 1: glVertexAttrib1f(location, par1); break;
				case 2: glVertexAttrib2f(location, par1, par2); break;
				case 3: glVertexAttrib3f(location, par1, par2, par3); break;
				case 4: glVertexAttrib4f(location, par1, par2, par3, par4); break;
				}
			}
		}
	}
	
	public static void loadShaders() {
		try {
			String name = "", vSource = "", fSource = "", line = "";
			int vShader = 0, fShader = 0;
			BufferedReader file;
			String[][] shaderList = getFiles();
			boolean[] usedShaders = new boolean[shaderList.length];
			for(int i = 0; i < usedShaders.length; i++)
				usedShaders[i] = false;
			for(int i = 0; i < shaderList.length; i++) {
				if(!usedShaders[i]) {
					name = shaderList[i][0];
					file = new BufferedReader(new FileReader(shaderList[i][1]));
					line = file.readLine();
					if(line.equals("[Vertex_Shader]"))
						vShader = i;
					else if(line.equals("[Fragment_Shader]"))
						fShader = i;
					usedShaders[i] = true;
					file.close();
					for(int k = i + 1; k < shaderList.length; k++) {
						if(shaderList[k][0].equals(name) && !usedShaders[k]) {
							file = new BufferedReader(new FileReader(shaderList[k][1]));
							line = file.readLine();
							if(line.equals("[Vertex_Shader]"))
								vShader = k;
							else if(line.equals("[Fragment_Shader]"))
								fShader = k;
							usedShaders[k] = true;
							break;
						}
					}
					file = new BufferedReader(new FileReader(shaderList[vShader][1]));
					file.readLine();
					while((line = file.readLine()) != null) {
						vSource += line + ("\n");
					}
					file.close();
					file = null;
					file = new BufferedReader(new FileReader(shaderList[fShader][1]));
					file.readLine();
					while((line = file.readLine()) != null) {
						fSource += line + ("\n");
					}
					file.close();
					file = null;
					
					
					initShaders(name, vSource, fSource);
					vSource = "";
					fSource = "";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void extractUniforms(String source, int program) {
		String[] lines = source.split("\n");
		String name = "";
		int length = 0;
		for(int i = 0; i < lines.length; i ++) {
			if(Util.getWord(lines[i], 1).toLowerCase().equals("uniform")) {
				name = Util.getWord(lines[i], 3);
				length = name.length();
				name = name.substring(0, length - 1);
				uniforms.add(new UniAttr(
						glGetUniformLocation(program, name),
						program,
						name));
			}
		}
	}
	
	private static void extractAttributes(String source, int program) {
		String[] lines = source.split("\n");
		String name = "";
		int length = 0;
		for(int i = 0; i < lines.length; i ++) {
			if(Util.getWord(lines[i], 1).toLowerCase().equals("attribute")) {
				name = Util.getWord(lines[i], 3);
				length = name.length();
				name = name.substring(0, length - 1);
				attributes.add(new UniAttr(
						glGetAttribLocation(program, name),
						program,
						name));
			}
		}
	}
	
	private static void initShaders(String name, String vSource, String fSource) {
		int program = glCreateProgram();
		int vShader = glCreateShader(GL_VERTEX_SHADER);
		int fShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vShader, vSource);
		glShaderSource(fShader, fSource);
		glCompileShader(vShader);
		glCompileShader(fShader);
		if(glGetShader(vShader, GL_COMPILE_STATUS) == GL_FALSE) {
			Info.writeln("Vertex shader \"" + name + "\" not loaded!");
			printLogs(vShader, fShader, name);
			return;
		}
		if(glGetShader(fShader, GL_COMPILE_STATUS) == GL_FALSE) {
			Info.writeln("Fragment shader \"" + name + "\" not loaded!");
			printLogs(vShader, fShader, name);
			return;
		}
		glAttachShader(program, vShader);
		glAttachShader(program, fShader);
		
		GL30.glBindFragDataLocation(program, 0, "out_color");
		glLinkProgram(program);
		
		if(glGetProgram(program, GL_LINK_STATUS) == GL_FALSE) {
			Info.writeln("Could not link " + name + " program");
			printLogs(vShader, fShader, name);
			return;
		}
		
		glValidateProgram(program);
		
		if(glGetProgram(program, GL_VALIDATE_STATUS) == GL_FALSE) {
			Info.writeln("Could not validate " + name + " program");
			printLogs(vShader, fShader, name);
			return;
		}
		
		
		programs.put(name, program);
		extractUniforms(vSource + "\n" + fSource, program);
		extractAttributes(vSource, program);
	}
	
	
	public static void reloadShaders() {
		setProgram("none");
		for(int i=0; i<programs.size(); i++) {
            glDeleteProgram(programs.values().toArray(new Integer[programs.values().size()])[i]);
		}
		programs.clear();
		uniforms.clear();
		attributes.clear();
		loadShaders();
	}
	
	public static int getProgram(String name) {
		if(!programs.containsKey(name)) {
			return 0;
		} else {
			return programs.get(name);
		}
	}
	
	private static String[][] getFiles() {
		if(!new File("res/shaders/").exists())
			new File("res/shaders/").mkdir();
		File[] list = new File("res/shaders/").listFiles();
		File[] sList;
		String[][] shaderList;
		int count = 0;
		for(int i = 0; i < list.length; i++) {
			if(list[i].isDirectory()) {
				sList = list[i].listFiles();
				for(int k = 0; k < sList.length; k++) {
					if(sList[k].isFile() && sList[k].getName().endsWith(".glsl"))
						count++;
				}
			}
		}
		shaderList = new String[count][2];
		count = 0;
		for(int i = 0; i < list.length; i++) {
			if(list[i].isDirectory()) {
				sList = list[i].listFiles();
				for(int k = 0; k < sList.length; k++) {
					if(sList[k].isFile() && sList[k].getName().endsWith(".glsl")) {
						shaderList[count][0] = list[i].getName();
						shaderList[count][1] = sList[k].getAbsolutePath();
						count++;
					}
				}
			}
		}
		return shaderList;
	}
	
	private static void printLogs(int v, int f, String name) {
		Info.writeln("[VERTEX " + name +"]\n"+glGetShaderInfoLog(v, 1000));
		Info.writeln("[FRAGMENT " + name +"]\n"+glGetShaderInfoLog(f, 1000));
	}
	
}
