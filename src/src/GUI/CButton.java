package src.GUI;

import lib.FontRenderer;

import org.lwjgl.opengl.GL11;



public class CButton extends GUIComponent{
	private String text;
	
	public CButton(GUI gui, int x, int y, int width, int height, String text, String name) {
		super(gui, x, y, width, height, name);
		setText(text);
		visible = true;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void draw() {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);

		if(isActive()){
			if(mouseOn) {
				GUIManager.drawFramedRect(width, height, 1, GUI.button_selected);
			} else {
				GUIManager.drawFramedRect(width, height, 1, GUI.button_idle);
			}
		} else {
			GUIManager.drawFramedRect(width, height, 1, GUI.button_disabled);
		}
		FontRenderer.drawStringCentred(width/2, height/2, text);
		GL11.glPopMatrix();
		super.draw();
	}
	
	public void onInputEvent(int EVENT_TYPE, int key) {
		if(mouseOn && EVENT_TYPE == GUIManager.EVENT_MOUSE_BUTTON_PRESSED && key == 0) {
			if(active) {
				if(parent != null) {
					parent.GUIEvent(GUIManager.EVENT_BUTTON_PRESSED, this);
				} else {
					gui.onGuiEvent(GUIManager.EVENT_BUTTON_PRESSED, this);
				}
			}
		}
		super.onInputEvent(EVENT_TYPE, key);
	}
}
