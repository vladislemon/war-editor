package lib;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;


public class FontRenderer {
	private static UnicodeFont  font;
	
	
	@SuppressWarnings("unchecked")
	public static void load() {
		try {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			font = new UnicodeFont("/res/fonts/font.ttf", 22, false, false);
		    font.addNeheGlyphs();
		    font.addGlyphs("ÀÁÂÃÄÅ¨ÆÇÈÉÊËÌÍÎÏĞÑÒÓÔÕ×ÙÚÛÜàáâãäå¸æçèéêëìíîïğñòóôõ÷ùúûü");
		    font.addGlyphs(400, 600);
		    font.getEffects().add(new ColorEffect(java.awt.Color.white));
		    font.loadGlyphs();
		    GL11.glEnable(GL11.GL_TEXTURE_2D);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void lg() {
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public static void drawString(float x, float y, String s) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		font.drawString(x+1, y+1, s, Color.gray);
		font.drawString(x, y, s);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void drawStringCentred(float x, float y, String s) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		drawString(x-font.getWidth(s)/2, y-font.getLineHeight()/2, s);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void drawStringCentred(float x, float y, String s, Color c) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		drawString(x-font.getWidth(s)/2, y-font.getLineHeight()/2, s, c);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void drawString(float x, float y, String s, Color c) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		font.drawString(x+1, y+1, s, Color.gray);
		font.drawString(x, y, s, c);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
}
