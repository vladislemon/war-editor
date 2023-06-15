package src.GUI;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class GUI{
	protected int x, y;
	protected int width;
	protected int height;
	private boolean visible;
	private boolean mouseOn;
	private ArrayList<GUIComponent> components = new ArrayList<GUIComponent>();
			
	
	public GUI() {
		visible = false;
		
	}
	
	public void update() {
		for(int i=0; i<components.size(); i++) {
			if(components.get(i).isActive()) {
				components.get(i).update();
			}
		}
		int mouseY = Display.getHeight() - Mouse.getY();
		if(isVisible() && Mouse.getX()>x && Mouse.getX()<x+width && mouseY<y+height && mouseY>y) {
			mouseOn = true;
		} else {
			mouseOn = false;
		}
	}
	
	public boolean mouseOn() {
		return mouseOn;
	}
	
	public void draw() {
		for(int i=0; i<components.size(); i++) {
			if(components.get(i).isVisible()) {
				components.get(i).draw();
			}
		}

	}
	
	public void open() {
		setVisible(true);
	}
	
	public void close() {
		setVisible(false);
	}
	
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	
	public boolean doesTakeInput() {
		return true;
	}
	
	public void onInputEvent(int EVENT_TYPE, int key) {
		for(int i=0; i<components.size(); i++) {
			components.get(i).onInputEvent(EVENT_TYPE, key);
		}
	}
	
	public void onGuiEvent(int EVENT_TYPE, GUIComponent component) {
		
	}
	
	public void addComponent(GUIComponent component) {
		components.add(component);
	}
	
	public void removeComponent(GUIComponent component) {
		components.remove(component);
	}
	
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}


	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}


	public static final Vector3f frame_boarder = new Vector3f(0, 0.1F, 0.4F);
	public static final Vector3f frame_body = new Vector3f(0, 0.1F, 0.6F);
	public static final Vector3f loadingbar_back = new Vector3f(0, 0.1F, 0.4F);
	public static final Vector3f loadingbar_bar = new Vector3f(0.8F, 0.6F, 0.1F);
	
	public static final Vector3f button_selected = new Vector3f(0, 0, 0.5F);
	public static final Vector3f button_idle = new Vector3f(0, 0, 0.7F);
	public static final Vector3f button_disabled = new Vector3f(0.5F, 0.5F, 0.5F);
	
	public static final Vector3f slider_line = new Vector3f(0.9F, 0.9F, 0.9F);
	public static final Vector3f slider_selected = new Vector3f(0, 0, 0.5F);
    public static final Vector3f slider_idle = new Vector3f(0, 0, 0.7F);
	public static final Vector3f slider_disabled = new Vector3f(0.5F, 0.5F, 0.5F);
	
	public static int cx = Display.getWidth()/2;
	public static int cy = Display.getHeight()/2;
	
}
