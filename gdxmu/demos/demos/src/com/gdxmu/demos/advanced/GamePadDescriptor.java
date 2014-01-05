package com.gdxmu.demos.advanced;

import com.badlogic.gdx.controllers.PovDirection;

public interface GamePadDescriptor {
	boolean isThumbstickAxis(int axisCode);
	float deadZoneForAxisCode(int axisCode);
	GamePadAxes axesForAxisCode(int axisCode);
	PovDirection button2PovDir(int buttonCode);
}

