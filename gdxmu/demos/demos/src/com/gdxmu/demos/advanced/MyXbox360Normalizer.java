package com.gdxmu.demos.advanced;

import com.gdxmu.demos.advanced.GameActions.ActionState;

public class MyXbox360Normalizer implements GamePadNormalizer {
	private Xbox360Desc desc;
	
	public MyXbox360Normalizer(Xbox360Desc descriptor) {
		if (descriptor == null)
			throw new IllegalArgumentException("Xbox360Normalizer descriptor was null.");
		this.desc = descriptor;
	}
	
	public int normalizeButton(int buttonCode) {
		if (buttonCode == desc.BUTTON_A())
		{
			if (GameActions.getState() == ActionState.MENU)
				return GameActions.ACTION_MENU_OK;
			else
				return GameActions.ACTION_PLAYING_SHOOT;
		}
		else if (buttonCode == desc.BUTTON_B())
		{
			if (GameActions.getState() == ActionState.MENU)
				return GameActions.ACTION_MENU_CANCEL;
			else
				return GameActions.ACTION_PLAYING_SHIELD;
		}
		else if (buttonCode == desc.BUTTON_BACK())
		{
			if (GameActions.getState() == ActionState.MENU)
				return GameActions.ACTION_MENU_CANCEL;
			else
				return GameActions.ACTION_PLAYING_RESTART;
		}
		else if (buttonCode == desc.BUTTON_START())
		{
			if (GameActions.getState() == ActionState.PLAYING)
				return GameActions.ACTION_PLAYING_MENU;
		}
		
		return GameActions.ACTION_NONE;
	}

	public int normalizeAxis(int axisCode, float value) {
		if (Math.abs(value) > desc.deadZoneForAxisCode(axisCode)) {
			if (axisCode == desc.AXIS_LEFT_Y() || axisCode == desc.AXIS_RIGHT_Y())
				return value < 0 ? GameActions.ACTION_NORTH : GameActions.ACTION_SOUTH;
			else if (axisCode == desc.AXIS_LEFT_X() || axisCode == desc.AXIS_RIGHT_X())
				return value < 0 ? GameActions.ACTION_WEST : GameActions.ACTION_EAST;
		}
		
		return GameActions.ACTION_NONE;
	}
}
