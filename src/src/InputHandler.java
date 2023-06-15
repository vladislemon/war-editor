package src;

import java.util.ArrayList;

import lib.Info;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import src.GUI.GUIManager;




public class InputHandler {
	private static boolean keyboard[] = new boolean[256];
	private static boolean mouse[] = new boolean[3];
	private static ArrayList<InputListener> listeners = new ArrayList<InputListener>();
	
	public static void register(InputListener listener) {
		listeners.add(listener);
	}
	
	
	public static boolean isKeyDown(int key) {
		return keyboard[key];
	}
	
	public static boolean isMouseLeftDown() {
		return mouse[0];
	}
	
	public static boolean isMouseRightDown() {
		return mouse[1];
	}
	
	public static boolean isMouseMiddleDown() {
		return mouse[2];
	}
	
	public static void handleInput() {
		while(Keyboard.next()) {
			if(Keyboard.getEventKey()==-1) {
				continue;
			}
			keyboard[Keyboard.getEventKey()]=Keyboard.getEventKeyState();
			warnListeners(false, Keyboard.getEventKey(), Keyboard.getEventKeyState());
		}
		while(Mouse.next()) {
			if(Mouse.getEventButton()==-1) {
				continue;
			}
			mouse[Mouse.getEventButton()]=Mouse.getEventButtonState();
			warnListeners(true, Mouse.getEventButton(), Mouse.getEventButtonState());
		}
	}
	
	private static void warnListeners(boolean mouse, int but, boolean state) {
		for(int i=0; i<listeners.size(); i++) {
			if(mouse) {
				listeners.get(i).onMouseEvent(but, state);
			} else {
				listeners.get(i).onKeyboardEvent(but, state);
			}
		}
		GUIManager.onInputEvent(mouse, but, state);
	}
	
	@SuppressWarnings("unused")
	private static Info info = new Info();
}
