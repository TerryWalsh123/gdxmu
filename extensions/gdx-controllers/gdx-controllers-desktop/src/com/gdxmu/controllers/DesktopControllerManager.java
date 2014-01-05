package com.gdxmu.controllers;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SharedLibraryLoader;

public class DesktopControllerManager implements ControllerManager {
	final Array<Controller> controllers = new Array<Controller>();
	final Array<ControllerListener> listeners = new Array<ControllerListener>();
	final OisControllers oisControllers;

	public DesktopControllerManager () {
		new SharedLibraryLoader().load("gdx-controllers-desktop");
		oisControllers = new OisControllers(this);
	}
	
	public void createControllers () {
		oisControllers.createControllers();
	}
	
	public boolean destroyControllers () {
		return oisControllers.destroyControllers();
	}

	public Array<Controller> getControllers () {
		return controllers;
	}

	public void addListener (ControllerListener listener) {
		listeners.add(listener);
	}

	public void removeListener (ControllerListener listener) {
		listeners.removeValue(listener, true);
	}

	public void clearListeners() {
		listeners.clear();
	}
}
