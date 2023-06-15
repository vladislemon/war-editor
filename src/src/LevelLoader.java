package src;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import lib.Info;
import app.Editor;

public class LevelLoader {
	private static float[][] heightMap;;
	private static int sizeX, sizeY;
	
	
	public static void load(String path) {
		Info.writeln("Loading level");
		File file = new File(path);
		if(!file.exists()) {
			Info.error("Could not find level data file");
			return;
		}
		if(!file.canRead()) {
			Info.error("Can't acces level data file, it might be used by another program");
		}

		DataInputStream reader;
		try {
			reader = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			if(reader.readChar() != 's') Info.error("Level data file signature error");
			if(reader.readChar() != 'h') Info.error("Level data file signature error");
			if(reader.readChar() != 'f') Info.error("Level data file signature error");
			
			sizeX = reader.readInt();
			sizeY = reader.readInt();
			
			heightMap = new float[sizeX][sizeY];
			
			ByteBuffer buf = ByteBuffer.allocate(sizeX*sizeY*4);
			byte[] bytes = new byte[sizeX*sizeY*4];
			reader.read(bytes);
			buf.put(bytes);
			buf.flip();
			for(int i = 0; i<sizeX; i++) {
				for(int k = 0; k<sizeY; k++) {
					heightMap[i][k] = buf.getFloat();
				}
			}
			
			
			reader.close();
			
			Info.writeln("Initialising terrain");
			Terrain.instance.init(heightMap);

			
		} catch (IOException e) {
			Editor.onError(e);
		}
		
	}
	
	public static void compile(float[][] hMap, String path) {
		heightMap = hMap;
		File file = new File(path);
		try {
			file.createNewFile();
		} catch (IOException e) {
			Editor.onError(e);
		}

		DataOutputStream writer;
		try {
			writer = new DataOutputStream(new FileOutputStream(file));

			writer.writeChar('s');
			writer.writeChar('h');
			writer.writeChar('f');
			
			sizeX = heightMap.length;
			sizeY = heightMap[0].length;
			
			writer.writeInt(sizeX);
			writer.writeInt(sizeY);
			
			ByteBuffer buf = ByteBuffer.allocate(4*sizeX*sizeY);
			
			for(int i=0; i<sizeX; i++) {
				for(int k=0; k<sizeY; k++) {
					buf.putFloat(hMap[i][k]);
				}
			}
			
			buf.flip();
			
			heightMap = null;
			
			writer.write(buf.array());

			writer.close();
			
		} catch (IOException e) {
			Editor.onError(e);
		}
		
	}
	
	public static void compile(String source, String path) {
		BufferedImage buf = null;
		try {
			 buf = ImageIO.read(new File(source));
		} catch (IOException e) {
			Info.error("Could not load Heightmap");
		}
		
		
		
		float[][] height = new float[buf.getWidth()][buf.getHeight()];

		
		
		
		for(int x=0; x<height.length; x++) {
			for(int y=0; y<height[0].length; y++) {
				Color c = new Color(buf.getRGB(x, y));
				height[x][y]=(c.getRed() + c.getGreen()*255 + c.getBlue()*255*255)/832332F;

			} 
		}
		
		
		compile(height, path);
	}
	
	public static String getSize() {
		if(sizeX*sizeY >= 1024*1024) {
			return "medium";
		} 
		else {
			return "small";
		}	

	}
	
	
}
