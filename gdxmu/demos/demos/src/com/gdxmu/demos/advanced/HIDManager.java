package com.gdxmu.demos.advanced;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Keys;
import com.gdxmu.demos.advanced.GameActions.ActionState;

public class HIDManager implements ControllerListener {
	private static HIDManager singleton = new HIDManager();
	
	private ObjectMap<Controller, GamePad> gamepads =
			new ObjectMap<Controller, GamePad>();
	private Array<Controller> unmappedGamepads =
			new Array<Controller>(true, 4, Controller.class);
	
	private HIDManager() {
		GameActions.setState(ActionState.PLAYING);
		
		/* Make sure your normalizer has a constructor that takes
		   a single argument that isAssignableFrom the 2nd arg you
		   pass to defineGamePadType */
		GamePadFactory.defineGamePadType(
				GamePadFactory.GAMEPAD_XBOX_360,
				MyXbox360Desc.class,
				MyXbox360Normalizer.class);
	}
	
	public static HIDManager HM() {
        return singleton;
    }
	
	// NOTE: You should clear cached Controllers when you receive a NOTICE event
	// from MiscUtils with param of MiscUtils.PARAM_NOTICE_CTLS_WILL_DESTROY
	// else you will be pointing to invalid data. There is currently no way
	// to re-associate new controllers with players after a call to
	// HIDManager.invalidateGamepads(). Until this feature is implemented,
	// multi-player games will have to prompt players to re-map gamepads
	// manually via a custom in-game GUI.
	public void invalidateGamepads() {
		gamepads.clear();
		unmappedGamepads.clear();
	}
	
	private GamePad addController(Controller c) {
		if (c != null && !gamepads.containsKey(c)) {
			try {
				GamePad gamepad = GamePadFactory.createGamePad(c.getName());
				gamepads.put(c, gamepad);
				Gdx.app.log("Added controller", c.getName());
			} catch (IllegalArgumentException e) {
				if (!unmappedGamepads.contains(c, true)) {
					unmappedGamepads.add(c);
					Gdx.app.log("Unmapped controller detected", c.getName());
				}
			}
		}
		return c != null ? gamepads.get(c) : null;
	}
	
	public void tick() {
		Keys<Controller> keys = gamepads.keys();
		while (keys.hasNext)
			gamepads.get(keys.next()).tick();
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		GamePad gamepad = addController(controller);
		if (gamepad != null)
			return gamepad.axisMoved(axisCode, value);
		else
			Gdx.app.log("axisMoved", "axisCode:" + axisCode + " value:" + value);
		return false;
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		GamePad gamepad = addController(controller);
		if (gamepad != null)
			return gamepad.buttonDown(buttonCode);
		else
			Gdx.app.log("buttonDown", "buttonCode:" + buttonCode);
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		GamePad gamepad = addController(controller);
		if (gamepad != null)
			return gamepad.buttonUp(buttonCode);
		else
			Gdx.app.log("buttonUp", "buttonCode:" + buttonCode);
		return false;
	}

	@Override
	public void connected(Controller controller) { /* Doesn't work */ }

	@Override
	public void disconnected(Controller controller) { /* Doesn't work */ }

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		GamePad gamepad = addController(controller);
		if (gamepad != null)
			return gamepad.povMoved(povCode, value);
		else
			Gdx.app.log("povMoved", "povCode:" + povCode + " value:" + value.toString());
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		addController(controller);
		Gdx.app.log("xSliderMoved",
				"sliderCode = " + sliderCode + " value = " + value);
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		addController(controller);
		Gdx.app.log("ySliderMoved",
				"sliderCode = " + sliderCode + " value = " + value);
		return false;
	}
}
