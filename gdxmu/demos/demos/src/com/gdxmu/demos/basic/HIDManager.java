package com.gdxmu.demos.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class HIDManager implements ControllerListener {
	private static float AXIS_DEADZONE = 0.3f;
	private static HIDManager singleton = new HIDManager();
	
	// NOTE: This dummy ivar represents whatever structure you use to store
	// game pads. It would most likely be an ObjectMap<Controller,GamePad>
	// or some kind of associative container. The point is that you
	// should clear cached Controllers when you receive a NOTICE event
	// from MiscUtils with param of MiscUtils.PARAM_NOTICE_CTLS_WILL_DESTROY
	// else you will be pointing to invalid data. There is currently no way
	// to re-associate new controllers with players after a call to
	// HIDManager.invalidateGamepads(). Until this feature is implemented,
	// multi-player games will have to prompt players to re-map gamepads
	// manually via a custom in-game GUI.
	private Array<Controller> dummyGamepads =
			new Array<Controller>(true, 4, Controller.class);
	
	private HIDManager() { }
	
	public static HIDManager HM() {
        return singleton;
    }
	
	public void invalidateGamepads() {
		dummyGamepads.clear();
	}
	
	private boolean addController(Controller c) {
		if (c != null && !dummyGamepads.contains(c, true)) {
			dummyGamepads.add(c);
			Gdx.app.log("Added Controller", c.getName());
		}
		return c != null;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		if (addController(controller) && Math.abs(value) > AXIS_DEADZONE)
			Gdx.app.log("axisMoved", "axisCode = " + axisCode + " value = " + value);
		return false;
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if (addController(controller))
			Gdx.app.log("buttonDown", "buttonCode = " + buttonCode);
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if (addController(controller))
			Gdx.app.log("buttonUp", "buttonCode = " + buttonCode);
		return false;
	}

	@Override
	public void connected(Controller controller) { /* Doesn't work */ }

	@Override
	public void disconnected(Controller controller) { /* Doesn't work */ }

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if (addController(controller))
			Gdx.app.log("povMoved",
					"povCode = " + povCode + " PovDirection = " + value.toString());
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		if (addController(controller))
			Gdx.app.log("xSliderMoved",
					"sliderCode = " + sliderCode + " value = " + value);
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		if (addController(controller))
			Gdx.app.log("ySliderMoved",
					"sliderCode = " + sliderCode + " value = " + value);
		return false;
	}
}
