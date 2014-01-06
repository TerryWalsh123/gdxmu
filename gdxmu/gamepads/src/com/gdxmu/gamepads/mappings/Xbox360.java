package com.gdxmu.gamepads.mappings;

import com.badlogic.gdx.controllers.PovDirection;
import com.gdxmu.gamepads.mappings.OsCheck.OSType;

public class Xbox360 {
	public static final int BUTTON_DASHBOARD;
	public static final int BUTTON_X;
	public static final int BUTTON_Y;
	public static final int BUTTON_A;
	public static final int BUTTON_B;
	public static final int BUTTON_BACK;
	public static final int BUTTON_START;
	public static final int BUTTON_LB;
	public static final int BUTTON_L3;
	public static final int BUTTON_RB;
	public static final int BUTTON_R3;
	public static final int AXIS_LEFT_X;
	public static final int AXIS_LEFT_Y;
	public static final int AXIS_LEFT_TRIGGER;
	public static final int AXIS_RIGHT_X;
	public static final int AXIS_RIGHT_Y;
	public static final int AXIS_RIGHT_TRIGGER;
	
	static {
		OsCheck.OSType ostype=OsCheck.getOperatingSystemType();
		
		switch (ostype) {
			case MacOS:
			{
				//NOTE: for use with 0.12 TattieBogle (Colin Munro) driver
				//http://tattiebogle.net/index.php/ProjectRoot/Xbox360Controller/OsxDriver
				BUTTON_DASHBOARD = 10;
				BUTTON_X = 13;
				BUTTON_Y = 14;
				BUTTON_A = 11;
				BUTTON_B = 12;
				BUTTON_BACK = 5;
				BUTTON_START = 4;
				BUTTON_LB = 8;
				BUTTON_L3 = 6;
				BUTTON_RB = 9;
				BUTTON_R3 = 7;
				AXIS_LEFT_X = 2; //-1 is left | +1 is right
				AXIS_LEFT_Y = 3; //-1 is up | +1 is down
				AXIS_LEFT_TRIGGER = 0; //value -1 to 1
				AXIS_RIGHT_X = 4; //-1 is left | +1 is right
				AXIS_RIGHT_Y = 5; //-1 is up | +1 is down
				AXIS_RIGHT_TRIGGER = 1; //value -1 to 1
			}
				break;
			case Linux:
			{
				//NOTE: For use with "xpad" driver
				BUTTON_DASHBOARD = 8;
				BUTTON_X = 2;
				BUTTON_Y = 3;
				BUTTON_A = 0;
				BUTTON_B = 1;
				BUTTON_BACK = 6;
				BUTTON_START = 7;
				BUTTON_LB = 4;
				BUTTON_L3 = 9;
				BUTTON_RB = 5;
				BUTTON_R3 = 10;
				AXIS_LEFT_X = 0; //-1 is left | +1 is right
				AXIS_LEFT_Y = 1; //-1 is up | +1 is down
				AXIS_LEFT_TRIGGER = 2; //value -1 to 1
				AXIS_RIGHT_X = 3; //-1 is left | +1 is right
				AXIS_RIGHT_Y = 4; //-1 is up | +1 is down
				AXIS_RIGHT_TRIGGER = 5; //value -1 to 1
			}
				break;
			case Windows:
			default:
			{
				BUTTON_DASHBOARD = 10;
				BUTTON_X = 2;
				BUTTON_Y = 3;
				BUTTON_A = 0;
				BUTTON_B = 1;
				BUTTON_BACK = 6;
				BUTTON_START = 7;
				BUTTON_LB = 4;
				BUTTON_L3 = 8;
				BUTTON_RB = 5;
				BUTTON_R3 = 9;
				AXIS_LEFT_X = 1; //-1 is left | +1 is right
				AXIS_LEFT_Y = 0; //-1 is up | +1 is down
				AXIS_LEFT_TRIGGER = 4; //value 0 to 1f
				AXIS_RIGHT_X = 3; //-1 is left | +1 is right
				AXIS_RIGHT_Y = 2; //-1 is up | +1 is down
				AXIS_RIGHT_TRIGGER = 5; //value 0 to -1f
			}
				break;
		}
	}

	
	// NOTE: some gamepad drivers fire button events for dpad
	// movement rather than the expected povMoved events.
	public static PovDirection button2PovDir(int buttonCode) {
		OsCheck.OSType ostype=OsCheck.getOperatingSystemType();
		
		switch (ostype) {
			case MacOS:
			{
				switch (buttonCode) {
					case 0: return PovDirection.north;
					case 1: return PovDirection.south;
					case 2: return PovDirection.west;
					case 3: return PovDirection.east;
				}
			}
				break;
			case Linux:
			{
				switch (buttonCode) {
					case 11: return PovDirection.west;
					case 12: return PovDirection.east;
					case 13: return PovDirection.north;
					case 14: return PovDirection.south;
				}
			}
				break;
			case Windows:
			default:
				break;
		}
		
		return null;
	}
	
	// NOTE: Given these three axis boundaries we have everything
	// needed to understand the state of thumbsticks and triggers
	// when combined with the axis' current value.
	public static float axisIdle(int axisCode) {
		return 0f;
	}
	
	public static float axisMin(int axisCode) {
		OsCheck.OSType ostype=OsCheck.getOperatingSystemType();
		
		if (ostype == OSType.Windows) {
			if (axisCode == AXIS_LEFT_TRIGGER)
				return 0f;
			else if (axisCode == AXIS_RIGHT_TRIGGER)
				return -1f;
		}
		
		return -1f;
	}
	
	public static float axisMax(int axisCode) {
		OsCheck.OSType ostype=OsCheck.getOperatingSystemType();
		
		if (ostype == OSType.Windows) {
			if (axisCode == AXIS_LEFT_TRIGGER)
				return 1f;
			else if (axisCode == AXIS_RIGHT_TRIGGER)
				return 0f;
		}
		
		return 1f;
	}
}
