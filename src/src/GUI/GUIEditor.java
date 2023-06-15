package src.GUI;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import src.Tools;
public class GUIEditor extends GUI {

	public GUIEditor() {
		x = Display.getWidth()-200;
		y = 0;
		width = 200;
		height = 400;
		this.open();
		this.addComponent(new CImgButton(this, 17, 20, 40 , 40, "res/textures/gui/editor_brush.png", "but_brush"));
		this.addComponent(new CImgButton(this, 59, 20, 40 , 40, "res/textures/gui/editor_smooth.png", "but_smooth"));
		this.addComponent(new CImgButton(this, 101, 20, 40 , 40, "res/textures/gui/editor_flat.png", "but_flat"));
		this.addComponent(new CImgButton(this, 143, 20, 40 , 40, "res/textures/gui/editor_blend.png", "but_blend"));
		
		this.addComponent(new CSlider(this, 20, 80, 100, 60, "Radius", "slider_radius", 0, 100, 1, 50));
		this.addComponent(new CSlider(this, 20, 120, 100, 60, "Force", "slider_force", 0, 100, 1, 50));
	}
	
	public void onGuiEvent(int EVENT_TYPE, GUIComponent component) {
		if(EVENT_TYPE == GUIManager.EVENT_BUTTON_PRESSED) {
			if(component.name == "but_brush") {
				Tools.setMode("brush");
			}
			if(component.name == "but_smooth") {
				Tools.setMode("smooth");
			}
			if(component.name == "but_flat") {
				Tools.setMode("flat");
			}
			if(component.name == "but_blend") {
				Tools.setMode("blend");
			}
		}
		
		if(EVENT_TYPE == GUIManager.EVENT_SLIDER_MOVED) {
			if(component.name == "slider_radius") {
				Tools.setRadius((int) ((CSlider)component).getVariable());
			}
			if(component.name == "slider_force") {
				Tools.setForce((int) ((CSlider)component).getVariable()/40F);
			}
		}
	}
	
	public void draw() {
		glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GUIManager.drawFrame(0, 0, 200, 400, GUI.frame_boarder, GUI.frame_body);
		glPopMatrix();
		super.draw();
	}
	
	public void open() {
		super.open();
		GUIManager.setInFocus(this);
	}
	
	public void close() {
		super.open();
		GUIManager.removeFocus(this);
	}
}
