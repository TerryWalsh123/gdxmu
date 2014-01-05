package com.gdxmu.demos.advanced;

import com.badlogic.gdx.controllers.PovDirection;

//NOTE: edit to suit your own game's actions
public class GameActions {
	public static final int ACTION_NONE = 0;
	// OR-able for 8-way movement. You can also have
	// continuous axis movement via the axisMoved float
	// values passed to your ControllerListener, but these
	// directions can also be useful in that instance by
	// acting as axis deadzone flags so that you get
	// smoother movement.
	public static final int ACTION_NORTH = 1<<0;
	public static final int ACTION_SOUTH = 1<<1;
	public static final int ACTION_EAST = 1<<2;
	public static final int ACTION_WEST = 1<<3;
	// It doesn't matter that these stomp on movement bits
	// because movement will be interpreted in movement logic
	// and non-movement in non-movement logic.
	public static final int ACTION_MENU_OK = 10;
	public static final int ACTION_MENU_CANCEL = 11;
	public static final int ACTION_PLAYING_SHOOT = 12;
	public static final int ACTION_PLAYING_SHIELD = 13;
	public static final int ACTION_PLAYING_RESTART = 14;
	public static final int ACTION_PLAYING_MENU = 15;
	// etc
	
	public enum ActionState { MENU, PLAYING };
	private static ActionState state = ActionState.MENU;
	
	public static ActionState getState() {
		return state;
	}
	
	public static void setState(ActionState state) {
		GameActions.state = state;
	}
	
	public static String moveAction2String(int actionCode) {
		String str = "";
		if ((actionCode & ACTION_NORTH) == ACTION_NORTH)
			str += "North";
		else if ((actionCode & ACTION_SOUTH) == ACTION_SOUTH)
			str += "South";
		if ((actionCode & ACTION_EAST) == ACTION_EAST)
			str += "East";
		else if ((actionCode & ACTION_WEST) == ACTION_WEST)
			str += "West";
		return str;
	}
	
	public static String action2String(int actionCode) {
		switch (actionCode) {
			case ACTION_MENU_OK: return "Menu OK";
			case ACTION_MENU_CANCEL: return "Menu cancel";
			case ACTION_PLAYING_SHOOT: return "Shoot";
			case ACTION_PLAYING_SHIELD: return "Shield";
			case ACTION_PLAYING_RESTART: return "Restart";
			case ACTION_PLAYING_MENU: return "Launch Menu";
		}
		return "";
	}
	
	public static String povDir2String(PovDirection povDir) {
		return povDir != null ? povDir.toString() : "center";
	}
}
