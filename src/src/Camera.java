package src;


import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Camera implements InputListener{
	private float x, y, z;
	private float pitch, yaw, roll;

	
	public Camera(int sx, int sy) {
		z = 25;
		x = sx;
		y = sy;
		pitch = 25;

	}
	
	public void update() {
		int mouseX = Mouse.getDX();
		int mouseY = Mouse.getDY();
		if(InputHandler.isMouseMiddleDown()) {
			x -= mouseX*0.014*(1 + 0.11*z);
			y -= mouseY*0.014*(1 + 0.11*z);
		}
		
		z -= Mouse.getDWheel()/120*(1 + z*0.02);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
	
	public void adjustPosition() {
		update();
		GL11.glRotatef(-pitch, 1, 0, 0);
		GL11.glRotatef(-yaw, 0, 1, 0);
		GL11.glRotatef(-roll, 0, 0, 1);
		GL11.glTranslatef(-x, -y, -z);
	}

	public void onMouseEvent(int button, boolean state) {
		
	}

	public void onKeyboardEvent(int key, boolean state) {
		
	}
}
	
