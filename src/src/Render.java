package src;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION_MATRIX;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import lib.FontRenderer;
import lib.ITextureLoader;
import lib.Info;
import lib.ModelLoader;
import lib.ShaderLoader;
import lib.Util;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import src.GUI.GUIManager;
import app.Editor;

public class Render implements InputListener{
	
	public int fps, fpsCount;
	private long frame;
	private boolean wireframe = false;
	public String caption = "Test map editor";
	public Camera camera = new Camera(20, 20);	
	
	
	public void create() {
		try {
			if(!Boolean.parseBoolean(Options.get("full_screen"))) {
				Display.setDisplayMode(new DisplayMode(Integer.parseInt(Options.get("window_width")), Integer.parseInt(Options.get("window_height"))));
			} else {
				Display.setFullscreen(true);
			}
			Display.setResizable(false);
			Display.setVSyncEnabled(Boolean.parseBoolean(Options.get("vsync")));
			Display.setTitle(caption);
			Display.setIcon(getIcon());
			Display.create();
		} catch (LWJGLException e) {
			Editor.onError(e);
		}
	}
	
	
	public void init() {
		glClearColor(0.4f, 0.4f, 0.4f, 1f);
		
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glShadeModel(GL_SMOOTH);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(70,  (float)Display.getWidth()/(float)Display.getHeight(), 0.1F, 1000);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		removeNativeCursor();
		
		InputHandler.register(this);
		
		
		
	}
	
	public void run() {
		frame = System.nanoTime() / 1000000;
		
		while(Editor.isRunning) {
			if(Display.isCloseRequested()) {
				Editor.isRunning = false;
			}
			if(!Display.isActive()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					Editor.onError(e);
				}
			}
			render();
		}
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		
		
		glPushMatrix();
		{
			
			camera.adjustPosition();

			ShaderLoader.setProgram("main");
			
			
			ShaderLoader.setUniform("tex0", 1, 0, 0, 0, 0);
			ShaderLoader.setUniform("tex1", 1, 1, 0, 0, 0);
			ShaderLoader.setUniform("tex2", 1, 2, 0, 0, 0);
			ShaderLoader.setUniform("tex3", 1, 3, 0, 0, 0);
			ShaderLoader.setUniform("tex4", 1, 4, 0, 0, 0);
			ShaderLoader.setUniform("tex5", 1, 5, 0, 0, 0);
			ShaderLoader.setUniform("tex6", 1, 6, 0, 0, 0);
			ShaderLoader.setUniform("tex7", 1, 7, 0, 0, 0);
			
			
			FloatBuffer matrix = Util.emptyFloatBuffer(16);
			glGetFloat(GL_MODELVIEW_MATRIX, matrix);
			ShaderLoader.setUniformMatrix("in_modelview", matrix);
			glGetFloat(GL_PROJECTION_MATRIX, matrix);
			ShaderLoader.setUniformMatrix("in_projection", matrix);
			
			
			
			if(Terrain.instance != null) {
				Terrain.instance.render();
			}
			
			ShaderLoader.setProgram("none");
			
			
			glPushMatrix();
			glTranslatef(50, 50, 0);
			
			
			GL11.glColor3f(0.2F,0.8F,1);
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex3f(0, 0, 20);
				GL11.glVertex3f(0, 10, 20);
				GL11.glVertex3f(10, 10, 20);
				GL11.glVertex3f(10, 0, 20);
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			GL11.glColor3f(1,1,1);
			
			
			
			
			glScalef(0.5F, 0.5F, 0.5F);
			glRotatef(185, 0, 1, 0);
			glRotatef(8, 1, 0, 0);
			ITextureLoader.bindTexture("res/textures/objects/tank.png");
			ModelLoader.get("tank.obj").draw();
			glPopMatrix();

			Tools.frame();
		}
		glPopMatrix();
		
		
		InputHandler.handleInput();
		FontRenderer.lg();
		GUIManager.draw();
		updateFps();
		Display.update();
	
	}
	
	
	public void enableOrtho() {
		glDisable(GL_DEPTH_TEST);
		if(wireframe) glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		GLU.gluOrtho2D(0, Display.getWidth(), Display.getHeight(), 0);
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
	}
	
	public void disableOrtho() {
		if(wireframe) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glEnable(GL_DEPTH_TEST);
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
	}
	
	
	public void onMouseEvent(int button, boolean state) {}
	public void onKeyboardEvent(int key, boolean state) {
		if(key==Keyboard.KEY_F3) {
			if(state==true) {
				ScreenShoter.saveScreen();
			}
		}
		
		if(key==Keyboard.KEY_F2) {
			if(state==true) {
				wireframe = !wireframe;
				if(wireframe) {
					GUIManager.debug.setVarible("render", "wireframe");
					glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
				} else {
					GUIManager.debug.setVarible("render", "normal");
					glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
				}
				
			}
		}
		
		if(key==Keyboard.KEY_F4 && state==true) {
			ShaderLoader.reloadShaders();
			Info.writeln("Shaders have been reloaded!");
		}
		if((key==Keyboard.KEY_LCONTROL || key == Keyboard.KEY_S) && state==true && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_S)) {
			LevelLoader.compile(Terrain.instance.getHeightMap(), "maps/usermap.shf");
		}
	}

	
	public void removeNativeCursor() {
		try {
			Mouse.setNativeCursor(new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null));
		} catch (LWJGLException e) {
			Editor.onError(e);
		}
	}
 	
	public void updateFps() {
		fpsCount++;
		if(System.nanoTime() / 1000000 > frame + 1000) {
			fps = fpsCount;
			fpsCount = 0;
			frame += 1000;
			System.gc();
		}
	}
	
	public void falseFrame() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GUIManager.draw();
		Display.update();
	}
	
	public void destroy() {
		Display.destroy();
		Editor.isRunning = false;
	}
	
	private ByteBuffer[] getIcon() {
		ByteBuffer[] buffer = new ByteBuffer[] {
			ITextureLoader.getImageData("res/icon/icon32.png"),
			ITextureLoader.getImageData("res/icon/icon16.png")
		};
		return buffer;
	}
	
	public static Render render = new Render();
	Info info = new Info();
}