package src.GUI;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import lib.ITextureLoader;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import src.Render;

public class GUIManager{
	private static GUI inFocus;
	
	public static void init() {

		debug = new GUIDebugPanel();
		editor = new GUIEditor();
	}
	
	public static void draw() {
		Render.render.enableOrtho();
		GL11.glDisable(GL11.GL_CULL_FACE);
		if(inFocus != null) {
			inFocus.update();
		}
	
		if(debug.isVisible()) {
			debug.draw();
		}
		if(editor.isVisible()) {
			editor.draw();
		}
		GL11.glEnable(GL11.GL_CULL_FACE);
		drawCursor();
		Render.render.disableOrtho();
	}
	
	public static void setInFocus(GUI gui) {
		inFocus = gui;
	}
	
	public static void removeFocus(GUI gui) {
		if(inFocus == gui) {
			inFocus = null;
		}
	}
	
	public static void onInputEvent(boolean mouse, int key, boolean state) {
		if(inFocus != null && inFocus.doesTakeInput()) {
			if(mouse) {
				if(state) {
					inFocus.onInputEvent(EVENT_MOUSE_BUTTON_PRESSED, key);
				} else {
					inFocus.onInputEvent(EVENT_MOUSE_BUTTON_RELEASED, key);
				}
			} else {
				if(state) {
					inFocus.onInputEvent(EVENT_KEYBOARD_KEY_PRESSED, key);
				} else {
					inFocus.onInputEvent(EVENT_KEYBOARD_KEY_RELEASED, key);
				}
			}
		}
		
		
		if(!mouse&&(key==Keyboard.KEY_D || key==Keyboard.KEY_LCONTROL || key==Keyboard.KEY_LSHIFT)&&(Keyboard.isKeyDown(Keyboard.KEY_D) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))&&state) {
			if(!debug.isVisible()) {
				debug.open();
			} else {
				debug.close();
			}
		}
	}
	
	public static void drawCursor() {
		ITextureLoader.bindTexture("res/textures/cursor.png");
		GL11.glColor4f(1, 1, 1, 1);
		float mouseX=Mouse.getX(); float mouseY=Mouse.getY();
		mouseY = Display.getHeight() - mouseY;
		
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);glVertex2f(   mouseX,    mouseY);
			glTexCoord2f(0, 1);glVertex2f(   mouseX, mouseY+32);
			glTexCoord2f(1, 1);glVertex2f(mouseX+32, mouseY+32);
			glTexCoord2f(1, 0);glVertex2f(mouseX+32,    mouseY);
		glEnd();
	}
	
	
	public static void drawFrameCentred(int width, int height, Vector3f c1, Vector3f c2) {
		GL11.glPushMatrix();
		GL11.glTranslatef(Display.getWidth()/2-width/2, Display.getHeight()/2-height/2, 0);
		GL11.glColor3f(c1.x, c1.y, c1.z);
		drawCornercutFillRect(width, height, 10);
		GL11.glTranslatef(4, 4, 0);
		GL11.glColor3f(c2.x, c2.y, c2.z);
		drawCornercutFillRect(width-8, height-8, 10);
		
		GL11.glPopMatrix();
	}
	
	public static void drawFrame(int x, int y, int width, int height, Vector3f c1, Vector3f c2) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glColor3f(c1.x, c1.y, c1.z);
		drawCornercutFillRect(width, height, 10);
		GL11.glTranslatef(4, 4, 0);
		GL11.glColor3f(c2.x, c2.y, c2.z);
		drawCornercutFillRect(width-8, height-8, 10);
		
		GL11.glPopMatrix();
	}
	
	
	public static void drawCornercutFillRect(int width, int height, int fallof) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(0,    fallof);
			GL11.glVertex2f(width,fallof);
			GL11.glVertex2f(width,height-fallof);
			GL11.glVertex2f(0,    height-fallof);
			
			GL11.glVertex2f(fallof,   	 0);
			GL11.glVertex2f(width-fallof,0);
			GL11.glVertex2f(width-fallof,fallof);
			GL11.glVertex2f(fallof,  	 fallof);
			
			GL11.glVertex2f(fallof,  	 height-fallof);
			GL11.glVertex2f(width-fallof,height-fallof);
			GL11.glVertex2f(width-fallof,height);
			GL11.glVertex2f(fallof, 	 height);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_TRIANGLES);
			GL11.glVertex2f(fallof, 0);
			GL11.glVertex2f(fallof, fallof);
			GL11.glVertex2f(0 ,     fallof);
			
			GL11.glVertex2f(fallof, height);
			GL11.glVertex2f(fallof, height-fallof);
			GL11.glVertex2f(0,      height-fallof);
			
			GL11.glVertex2f(width-fallof, height);
			GL11.glVertex2f(width-fallof, height-fallof);
			GL11.glVertex2f(width,        height-fallof);
			
			GL11.glVertex2f(width-fallof, 0);
			GL11.glVertex2f(width-fallof, fallof);
			GL11.glVertex2f(width ,       fallof);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void drawFramedRect(int width, int height, int boarder, Vector3f c1) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(c1.x, c1.y, c1.z);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(width, 0);
			GL11.glVertex2f(0,     0);
			GL11.glVertex2f(0,     height);
			GL11.glVertex2f(width, height);
		GL11.glEnd();
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(width, 0);
			GL11.glVertex2f(0,     0);
			GL11.glVertex2f(0,     height);
			GL11.glVertex2f(width, height);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void drawRect(int width, int height, Vector3f c1) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(c1.x, c1.y, c1.z);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(width, 0);
			GL11.glVertex2f(0,     0);
			GL11.glVertex2f(0,     height);
			GL11.glVertex2f(width, height);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void drawTexturedRect(int width, int height) {
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1, 0);GL11.glVertex2f(width, 0);
			GL11.glTexCoord2f(0, 0);GL11.glVertex2f(0,     0);
			GL11.glTexCoord2f(0, 1);GL11.glVertex2f(0,     height);
			GL11.glTexCoord2f(1, 1);GL11.glVertex2f(width, height);
		GL11.glEnd();
	}
	
	public static GUIDebugPanel debug;
	public static GUIEditor editor;
			
	
	public static final int EVENT_MOUSE_BUTTON_PRESSED = 0;
	public static final int EVENT_MOUSE_BUTTON_RELEASED = 1;
	public static final int EVENT_KEYBOARD_KEY_PRESSED = 2;
	public static final int EVENT_KEYBOARD_KEY_RELEASED = 3;
	public static final int EVENT_BUTTON_PRESSED = 4;
	public static final int EVENT_SLIDER_MOVED = 5;
	
}
