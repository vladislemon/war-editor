package src;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import lib.Info;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import app.Editor;

public class ScreenShoter {
	
	public static void saveScreen() {
		GL11.glReadBuffer(GL11.GL_FRONT);
		int width = Display.getDisplayMode().getWidth();
		int height= Display.getDisplayMode().getHeight();
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		
		if(!new File("screenshots/").exists())
			new File("screenshots/").mkdir();
		String name = Info.getDateAndTime()+".PNG";
		name=name.replace("/", "");
		name=name.replace(":", "");
		name=name.replace("_", "");
		
		File file = new File("screenshots/" + name);
		try {
			file.createNewFile();
		} catch (IOException e) {
			Editor.onError(e);
		}
		String format = "PNG"; //"PNG" or "JPG"
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		  
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}
		  
		try {
			ImageIO.write(image, format, file);
		} catch (IOException e) {
			Editor.onError(e);
		}
		
		Info.writeln("Screenshot saved as /screenshots/"+name);
	}
	
}
