package src.GUI;

import lib.FontRenderer;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class CSlider extends GUIComponent {
	
	private String text;
	private float step, min, max, var, sliderX = 0;
	private int sliderWidth = 10, sliderHeight = 15, sliderY = 30, numW = 20, numH = 5;
	private boolean grab, mouseOnSlider;

	public CSlider(GUI gui, int x, int y, int width, int height, String text, String workname, float min, float max, float step, float defaultVar) {
		super(gui, x, y, width, height, workname);
		setText(text);
		this.min = min;
		this.max = max;
		this.step = step;
		setVariable(defaultVar);
		sliderY = height - sliderHeight;
		visible = true;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public float getVariable() {
		return var;
	}
	
	public void onInputEvent(int EVENT_TYPE, int key) {
		if(active) {
			if(mouseOnSlider && EVENT_TYPE == GUIManager.EVENT_MOUSE_BUTTON_PRESSED && key == 0) {
				grab = true;
			}
			else if(EVENT_TYPE == GUIManager.EVENT_MOUSE_BUTTON_RELEASED && key == 0) {
				grab = false;
				if(parent != null) {
					parent.GUIEvent(GUIManager.EVENT_SLIDER_MOVED, this);
				} else {
					gui.onGuiEvent(GUIManager.EVENT_SLIDER_MOVED, this);
				}
			}
				
		}
		super.onInputEvent(EVENT_TYPE, key);
	}
	
	public void setVariable(float var) {
		  if(var >= min && var <= max) {
		   this.var = var;
		   sliderX = (var - min) * width / (max - min);
		  }
		 }
	
	public void draw() {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		FontRenderer.drawStringCentred(width/2, height/2, text);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y + sliderY, 0);
		GUIManager.drawRect(width + sliderWidth, 3, GUI.slider_line);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x + sliderX, y + sliderY - sliderHeight/2, 0);
		if(isActive()) {
			if(mouseOnSlider || grab)
				GUIManager.drawFramedRect(sliderWidth, sliderHeight, 1, GUI.slider_selected);
			else
				GUIManager.drawFramedRect(sliderWidth, sliderHeight, 1, GUI.slider_idle);
		} else {
			GUIManager.drawFramedRect(sliderWidth, sliderHeight, 1, GUI.slider_disabled);
		}
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x + width, y + sliderY - sliderHeight, 0);
		FontRenderer.drawString(numW, numH, ((int) (var*(1f/step)))/(1f/step) + "");

		GL11.glPopMatrix();
		super.draw();
	}
	
	public void update() {
		super.update();
		mouseOnSlider = mouseOnSlider();
		if(grab) {
			float dx = Mouse.getX() - getX();
			if(dx > width)
				sliderX = width;
			else if(dx < 0)
				sliderX = 0;
			else {
				sliderX = Math.round((max - min) * (dx / (float)width) / step) * step * ((float)width / (max - min));
			}
		}
		var = min + (max - min) * (sliderX / (float)width);
	}
	
	private boolean mouseOnSlider() {
		int mouseY = Display.getHeight() - Mouse.getY();
		if(mouseY <= getY() + sliderY + sliderHeight/2 && mouseY >= getY() + sliderY  - sliderHeight/2 &&
				Mouse.getX() <= getX() + sliderX + sliderWidth && Mouse.getX() >= getX() + sliderX)
			return true;
		return false;
	}
}
