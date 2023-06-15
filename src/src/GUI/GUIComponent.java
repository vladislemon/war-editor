package src.GUI;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GUIComponent {
	protected int x, y, width, height;
	public String name;
	protected ArrayList<GUIComponent> children = new ArrayList<GUIComponent>();
    protected GUIComponent parent;
    protected GUI gui;
	protected boolean visible = false;
	protected boolean mouseOn = false;
	protected boolean active = true;
	
	public void GUIEvent(int EVENT_TYPE, GUIComponent component) {
		
	}
	
	public GUIComponent(GUI gui, int x, int y, int width, int height, String workname) {
		this.gui = gui;
		this.x = gui.x + x;
		this.y = gui.y + y;
		this.width = width;
		this.height = height;
		this.name = workname;
		
	}
	
	
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
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
	
	public void addChild(GUIComponent child) {
		children.add(child);
		child.setParent(this);
		child.setGui(gui);
	}
	
	public void removeChild(GUIComponent child) {
		children.remove(child);
	}

	public void onInputEvent(int EVENT_TYPE, int key) {
		for(int i=0; i<children.size(); i++) {
			children.get(i).onInputEvent(EVENT_TYPE, key);
		}
	}
	
	
	public void draw() {		
		for(int i=0; i<children.size(); i++) {
			if(children.get(i).isVisible()) {
				children.get(i).draw();
			}
		}
	}
	
	public void update() {
		int mouseY = Display.getHeight() - Mouse.getY();
		if(isVisible() && Mouse.getX()>x && Mouse.getX()<x+width && mouseY<y+height && mouseY>y) {
			mouseOn = true;
		} else {
			mouseOn = false;
		}
		
		for(int i=0; i<children.size(); i++) {
			if(children.get(i).isActive()) {
				children.get(i).update();
			}
		}
	}

	public boolean isVisible() {
		return visible;
	}
	public boolean isActuallyVisible () {
		if(parent != null) {
			return parent.isActuallyVisible() && visible;
		} else {
			return gui.isVisible() && visible;
		}
	}
	

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public GUIComponent getParent() {
		return parent;
	}

	public void setParent(GUIComponent parent) {
		this.parent = parent;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public GUI getGui() {
		return gui;
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}
	
	public String getName() {
		return name;
	}
	

}
