package src.GUI;

import lib.ITextureLoader;

import org.lwjgl.opengl.GL11;

public class CImgButton extends GUIComponent{
	private String image;

	public CImgButton(GUI gui, int x, int y, int width, int height, String image, String workname) {
		super(gui, x, y, width, height, workname);
		this.image = image;
		visible = true;
	}

	public void draw() {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);

		if(isActive()){
			if(mouseOn) {
				GL11.glColor3f(0.8F, 0.8F, 0.8F);
			} else {
				GL11.glColor3f(1F, 1F, 1F);
			}
		} else {
			GL11.glColor3f(0.6F, 0.6F, 0.6F);
		}
		ITextureLoader.bindTexture(image);
		GUIManager.drawTexturedRect(width, height);
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
