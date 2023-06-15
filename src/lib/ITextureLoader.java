package lib;


import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

import app.Editor;

public class ITextureLoader {
	private static HashMap<String, Integer> textures = new HashMap<String, Integer>();
	private static ArrayList<String> paths = new ArrayList<String>();
	
	public static void init() {
		scanFiles("res/textures");
	}
	
	public static void cleanup() {
		paths = null;
	}
	
	public static void loadTextures() {
		for(int i=0; i<paths.size(); i++) {
			textures.put(paths.get(i).replace('\\', '/'), getTexture(paths.get(i)));
		}
		cleanup();
	}
	
	private static int getTexture(String path) {
		File file = new File(path);
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        
        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); 
                buffer.put((byte) ((pixel >> 8) & 0xFF)); 
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();
		
        
		int textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL,8);
        glTexParameteri(GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP,GL11.GL_TRUE);
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glBindTexture(GL_TEXTURE_2D, 0);
        
        return textureId;
	}
	
	private static void scanFiles(String path) {
		File list[] = new File(path).listFiles();
		for(int i=0; i<list.length; i++) {
			if(list[i].isDirectory()) {
				scanFiles(list[i].getPath());
			}
			if(list[i].isFile() && (list[i].getName().endsWith(".png") || list[i].getName().endsWith(".PNG"))) {
				paths.add(list[i].getPath());
			}
			
		}
	}
	
	public static int getSRCtextureCount() {
		return paths.size();
	}
	
	public static int getTextureCount() {
		return textures.size();
	}
	
	
	public static ByteBuffer getImageData(String path) {
		try {
			File file = new File(path);
			if(!file.exists())
				return BufferUtils.createByteBuffer(1);
			BufferedImage image = ImageIO.read(file);
			
			int[] pixels = new int[image.getWidth() * image.getHeight()];
	        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

	        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB
	        
	        for(int y = 0; y < image.getHeight(); y++){
	            for(int x = 0; x < image.getWidth(); x++){
	                int pixel = pixels[y * image.getWidth() + x];
	                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
	                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
	                buffer.put((byte) (pixel & 0xFF));               // Blue component
	                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
	            }
	        }

	        buffer.flip();
	        return buffer;
		} catch (IOException e) {
			Editor.onError(e);
		}
		return BufferUtils.createByteBuffer(1);
	}
	
	
	public static void bindTexture(String str) {		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		if(!textures.containsKey(str)) {
			glBindTexture(GL_TEXTURE_2D, textures.get("res/textures/null.png"));
		} else {
			glBindTexture(GL_TEXTURE_2D, textures.get(str));
		}

	}
	
	public static void bindTexture(int bank, String str) {		
		GL13.glActiveTexture(GL13.GL_TEXTURE0+bank);
		if(!textures.containsKey(str)) {
			glBindTexture(GL_TEXTURE_2D, textures.get("res/textures/null.png"));
		} else {
			glBindTexture(GL_TEXTURE_2D, textures.get(str));
		}
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}
	
}
