package com.gdxmu.demos.advanced;

import com.badlogic.gdx.controllers.PovDirection;
import com.gdxmu.gamepads.mappings.Xbox360;

public class Xbox360Desc implements GamePadDescriptor {
	protected GamePadAxes axesCache = new GamePadAxes();
	
	public int BUTTON_DASHBOARD() { return Xbox360.BUTTON_DASHBOARD; }
	public int BUTTON_X() { return Xbox360.BUTTON_X; }
	public int BUTTON_Y() { return Xbox360.BUTTON_Y; }
	public int BUTTON_A() { return Xbox360.BUTTON_A; }
	public int BUTTON_B() { return Xbox360.BUTTON_B; }
	public int BUTTON_BACK() { return Xbox360.BUTTON_BACK; }
	public int BUTTON_START() { return Xbox360.BUTTON_START; }
	public int BUTTON_LB() { return Xbox360.BUTTON_LB; }
	public int BUTTON_L3() { return Xbox360.BUTTON_L3; }
	public int BUTTON_RB() { return Xbox360.BUTTON_RB; }
	public int BUTTON_R3() { return Xbox360.BUTTON_R3; }
	public int AXIS_LEFT_X() { return Xbox360.AXIS_LEFT_X; }
	public int AXIS_LEFT_Y() { return Xbox360.AXIS_LEFT_Y; }
	public int AXIS_LEFT_TRIGGER() { return Xbox360.AXIS_LEFT_TRIGGER; }
	public int AXIS_RIGHT_X() { return Xbox360.AXIS_RIGHT_X; }
	public int AXIS_RIGHT_Y() { return Xbox360.AXIS_RIGHT_Y; }
	public int AXIS_RIGHT_TRIGGER() { return Xbox360.AXIS_RIGHT_TRIGGER; }
	
	public Xbox360Desc() { }
	
	@Override
	public boolean isThumbstickAxis(int axisCode) {
		return axisCode == AXIS_LEFT_X() || axisCode == AXIS_LEFT_Y() ||
				axisCode == AXIS_RIGHT_X() || axisCode == AXIS_RIGHT_Y();
	}
	
	@Override
	public float deadZoneForAxisCode(int axisCode) {
		return 0.25f;
	}
	
	@Override
	public GamePadAxes axesForAxisCode(int axisCode) {
		if (axisCode == AXIS_LEFT_Y() || axisCode == AXIS_LEFT_X())
			return axesCache.set(AXIS_LEFT_X(), AXIS_LEFT_Y());
		else if (axisCode == AXIS_RIGHT_X() || axisCode == AXIS_RIGHT_Y())
			return axesCache.set(AXIS_RIGHT_X(), AXIS_RIGHT_Y());
		else if (axisCode == AXIS_LEFT_TRIGGER() || axisCode == AXIS_RIGHT_TRIGGER())
			return axesCache.set(AXIS_LEFT_TRIGGER(), AXIS_RIGHT_TRIGGER());
		else
			return null;
	}

	@Override
	public PovDirection button2PovDir(int buttonCode) {
		return Xbox360.button2PovDir(buttonCode);
	}
}
