package src;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import lib.Util;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import src.GUI.GUIManager;

public class Tools {
	private static Vector3f cursor;
	private static String mode = "brush";
	private static int radius;
	
	private static float force;
	
	
	public static void frame() {
		cursor = getMouseVector();
		drawSelectionCircle(cursor.x, cursor.y, radius);
		if(!GUIManager.editor.mouseOn()) {
			if(mode == "brush") {
				if(InputHandler.isMouseRightDown()) {
					edit(radius, -force);
				}
				if(InputHandler.isMouseLeftDown()) {
					edit(radius, force);
				}
			}
			if(mode == "smooth") {
				if(InputHandler.isMouseLeftDown()) {
					smooth(radius, force);
				}
			}
			
			if(mode == "flat") {
				if(InputHandler.isMouseRightDown()) {
					flat(radius, -force);
				}
				if(InputHandler.isMouseLeftDown()) {
					flat(radius, force);
				}
			}
			
			if(mode == "blend") {
				if(InputHandler.isMouseRightDown()) {
					blend(radius, -force);
				}
				if(InputHandler.isMouseLeftDown()) {
					blend(radius, force);
				}
			}
		}
	}
	
	public static void renew(int minx, int miny, int maxx, int maxy) {
		Chunk[][] chunks = Terrain.instance.getChunks();
		
		for(int i=(minx-1)/64; i<(Math.min(maxx+1, Terrain.instance.getWidth()))/64F; i++) {
			for(int k=(miny-1)/64; k<(Math.min(maxy+1, Terrain.instance.getHeight()))/64F; k++) {
				chunks[i][k].destroy();
				chunks[i][k].build();
			}
		}
	}
	
	public static void edit(int radius, float force) {
		
		int minx = (int) Math.max(cursor.x - radius-1, 0);
		int miny = (int) Math.max(cursor.y - radius-1, 0);
		int maxx = (int) Math.min(cursor.x + radius+1, Terrain.instance.getWidth());
		int maxy = (int) Math.min(cursor.y + radius+1, Terrain.instance.getHeight());
		
		
		for(int i=minx; i<maxx; i++) {
			for(int k=miny; k<maxy; k++) {
				float distancesq = (i-cursor.x)*(i-cursor.x) + (k-cursor.y)*(k-cursor.y);
				if(distancesq <= radius*radius) {
					Terrain.instance.add(i, k, (float) (force*(radius - Math.sqrt(distancesq))/(radius)));
				}
			}
		}
		
		renew(minx, miny, maxx, maxy);
	}
	
	public static void smooth(int radius, float force) {
		int minx = (int) Math.max(cursor.x - radius-1, 0);
		int miny = (int) Math.max(cursor.y - radius-1, 0);
		int maxx = (int) Math.min(cursor.x + radius+1, Terrain.instance.getWidth());
		int maxy = (int) Math.min(cursor.y + radius+1, Terrain.instance.getHeight());
		
		/*ArrayList<Float> heights = new ArrayList<Float>();
		for(int i=minx; i<maxx; i++) {
			for(int k=miny; k<maxy; k++) {
				float distancesq = (i-cursor.x)*(i-cursor.x) + (k-cursor.y)*(k-cursor.y);
				if(distancesq <= radius*radius) {
					heights.add(Terrain.instance.get(i, k));
				}
			}
		}

		float[] data = new float[heights.size()];
		
		for(int i=0; i<heights.size(); i++) {
			data[i] = heights.get(i);
		}
		
		float average = Util.average(data);
		
		
		for(int i=minx; i<maxx; i++) {
			for(int k=miny; k<maxy; k++) {
				float distancesq = (i-cursor.x)*(i-cursor.x) + (k-cursor.y)*(k-cursor.y);
				if(distancesq <= radius*radius) {
					Terrain.instance.add(i, k, (float) ((average-Terrain.instance.get(i, k))/60*force*(radius - Math.sqrt(distancesq))/(radius)));
				}
			}
		}*/
		
		
		for(int i=minx; i<maxx; i++) {
			for(int k=miny; k<maxy; k++) {
				float distancesq = (i-cursor.x)*(i-cursor.x) + (k-cursor.y)*(k-cursor.y);
				if(distancesq <= radius*radius) {
					float average = Util.average(new float[] {Terrain.instance.get(i-1, k), Terrain.instance.get(i+1, k), Terrain.instance.get(i, k-1), Terrain.instance.get(i, k+1)});
					Terrain.instance.add(i, k, (float) ((average-Terrain.instance.get(i, k))*force/1.6F*(radius - Math.sqrt(distancesq))/(radius)));
				}
			}
		}
		
		renew(minx, miny, maxx, maxy);
	}
	
	
	
	public static void flat(int radius, float force) {
		int minx = (int) Math.max(cursor.x - radius-1, 0);
		int miny = (int) Math.max(cursor.y - radius-1, 0);
		int maxx = (int) Math.min(cursor.x + radius+1, Terrain.instance.getWidth());
		int maxy = (int) Math.min(cursor.y + radius+1, Terrain.instance.getHeight());
		
		for(int i=minx; i<maxx; i++) {
			for(int k=miny; k<maxy; k++) {
				float distancesq = (i-cursor.x)*(i-cursor.x) + (k-cursor.y)*(k-cursor.y);
				if(distancesq <= radius*radius) {
					Terrain.instance.add(i, k, (Terrain.instance.getInterpolated(cursor.x, cursor.y)-Terrain.instance.get(i, k))/10*force);
				}
			}
		}
		renew(minx, miny, maxx, maxy);
	}
	
	public static void blend(int radius, float force) {
		int minx = (int) Math.max(cursor.x - radius-1, 0);
		int miny = (int) Math.max(cursor.y - radius-1, 0);
		int maxx = (int) Math.min(cursor.x + radius+1, Terrain.instance.getWidth());
		int maxy = (int) Math.min(cursor.y + radius+1, Terrain.instance.getHeight());
		
		for(int i=minx; i<maxx; i++) {
			for(int k=miny; k<maxy; k++) {
				float distancesq = (i-cursor.x)*(i-cursor.x) + (k-cursor.y)*(k-cursor.y);
				if(distancesq <= radius*radius) {
					Terrain.instance.addBlend(i, k, (float) ((radius-Math.sqrt(distancesq))/radius*force/30));
				}
			}
		}
		renew(minx, miny, maxx, maxy);
	}
	
	
	public static void drawSelectionCircle(float x, float y, int r) {
		glDisable(GL_DEPTH_TEST);
		glBegin(GL11.GL_LINE_LOOP);
		for(int i=0; i<32; i++) {
			float circleX = Util.cos(i*360/32)*r;
			float circleY = Util.sin(i*360/32)*r;
			glVertex3f(circleX+x, circleY+y, Terrain.instance.getInterpolated(x+circleX, y+circleY));
		}
		glEnd();
		glEnable(GL_DEPTH_TEST);
	}
	
	public static Vector3f getMouseVector() {
		float mouseX = (float)Mouse.getX();
		float mouseY = (float)Mouse.getY();
		
		IntBuffer viewport = BufferUtils.createIntBuffer(16); 
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16); 
		FloatBuffer projection = BufferUtils.createFloatBuffer(16); 
		
		GL11.glGetFloat( GL11.GL_MODELVIEW_MATRIX, modelview ); 
		GL11.glGetFloat( GL11.GL_PROJECTION_MATRIX, projection ); 
		GL11.glGetInteger( GL11.GL_VIEWPORT, viewport ); 
		
		FloatBuffer vec1 = BufferUtils.createFloatBuffer(3);
		
		GLU.gluUnProject(mouseX, mouseY, 1, modelview, projection, viewport, vec1);
		
		float beamx = Render.render.camera.getX(), beamy = Render.render.camera.getY(), beamz = Render.render.camera.getZ();
		Vector3f move = new Vector3f(vec1.get(0) - beamx, vec1.get(1) - beamy, vec1.get(2) - beamz);
		for(int i=0; i<1000; i++) {
			beamx += move.x/1000;
			beamy += move.y/1000;
			beamz += move.z/1000;
			if(Terrain.instance.getInterpolated(beamx,  beamy) >= beamz) {
				float k = Terrain.instance.getInterpolated(beamx,  beamy) - beamz;
				
				beamz += k;
				beamx += k*move.x/move.z;
				beamy += k*move.y/move.z;

				return(new Vector3f(beamx, beamy, beamz));
			}
		}
		
		return(new Vector3f(-1, -1, -1));

		
	}
	


	public static String getMode() {
		return mode;
	}

	public static void setMode(String mode) {
		Tools.mode = mode;
	}
	
	public static int getRadius() {
		return radius;
	}

	public static void setRadius(int radius) {
		Tools.radius = radius;
	}

	public static float getForce() {
		return force;
	}

	public static void setForce(float force) {
		Tools.force = force;
	}
}
