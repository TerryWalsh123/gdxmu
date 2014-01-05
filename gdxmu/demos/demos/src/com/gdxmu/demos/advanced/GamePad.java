package com.gdxmu.demos.advanced;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.utils.IntFloatMap;
import com.badlogic.gdx.utils.IntFloatMap.Keys;

// NOTE: You may like to add some more components like:
// class Thumbstick, Dpad, Button, Trigger, Rumbler, etc.
public class GamePad {
	private boolean isAxisCacheDirty;
	private IntFloatMap axisCache = new IntFloatMap(4);
	private GamePadDescriptor descriptor;
	private GamePadNormalizer normalizer;
	
	public GamePad(GamePadDescriptor descriptor, GamePadNormalizer normalizer) {
		if (descriptor == null || normalizer == null)
			throw new IllegalArgumentException("GamePad ctor arg/s was null.");
		this.descriptor = descriptor;
		this.normalizer = normalizer;
	}
	
	public void tick() {
		if (isAxisCacheDirty) {
			int moveAction = getMovementAction();
			if (moveAction != GameActions.ACTION_NONE)
				Gdx.app.log("GamePad", GameActions.moveAction2String(moveAction));
			isAxisCacheDirty = false;
		}
	}
	
	private int getMovementAction() {
		int actionCode = GameActions.ACTION_NONE;
		Keys keys = axisCache.keys();
		while (keys.hasNext) {
			int key = keys.next();
			actionCode |= normalizer.normalizeAxis(key, axisCache.get(key, 0));
		}
		return actionCode;
	}
	
	public boolean axisMoved(int axisCode, float value) {
		if (descriptor.isThumbstickAxis(axisCode)) {
			axisCache.put(axisCode, value);
			isAxisCacheDirty = true;
		}
		return false;
	}

	public boolean buttonDown(int buttonCode) {
		PovDirection dir = descriptor.button2PovDir(buttonCode);
		
		if (dir == null) {
			int actionCode = normalizer.normalizeButton(buttonCode);
			if (actionCode != GameActions.ACTION_NONE)
				Gdx.app.log("GamePad", GameActions.action2String(actionCode) + " on");
			return false;
		} else
			return povMoved(0, dir);
	}

	public boolean buttonUp(int buttonCode) {
		PovDirection dir = descriptor.button2PovDir(buttonCode);
		
		if (dir == null) {
			int actionCode = normalizer.normalizeButton(buttonCode);
			if (actionCode != GameActions.ACTION_NONE)
				Gdx.app.log("GamePad", GameActions.action2String(actionCode) + " off");
			return false;
		} else
			return povMoved(0, dir);
	}
	
	public boolean povMoved(int povCode, PovDirection value) {
		Gdx.app.log("GamePad", GameActions.povDir2String(value));
		return false;
	}
}
