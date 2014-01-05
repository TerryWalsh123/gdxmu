
package com.gdxmu.controllers;

import java.util.ArrayList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import com.badlogic.gdx.controllers.desktop.ois.OisJoystick;
import com.badlogic.gdx.Gdx;

class Ois extends com.badlogic.gdx.controllers.desktop.ois.Ois {
	public Ois (long hwnd) {
		super(hwnd);
	}

	@SuppressWarnings("unchecked")
	public boolean destroy () {
		do {
			final ArrayList<OisJoystick> joysticks;
			Field inputMgrField;
			
			if ((joysticks = (ArrayList<OisJoystick>)ReflectUtils.getHiddenObject(
					"joysticks", this,
					com.badlogic.gdx.controllers.desktop.ois.Ois.class)) == null)
				break;
			if ((inputMgrField = ReflectUtils.getHiddenField("inputManagerPtr",
					com.badlogic.gdx.controllers.desktop.ois.Ois.class)) == null)
				break;
			if (inputMgrField.getType() != long.class)
				break;
			
			try {
				joysticks.clear();

				final long inputManagerPtr = inputMgrField.getLong(this);
				if (inputManagerPtr != 0) {
					destroyInputManager(inputManagerPtr);
					inputMgrField.setLong(this, 0);
				}
			}
			catch (Exception e) { break; }
			return true;
		} while (false);

		return false;
	}
	
	// @off
	/*JNI
	#include <OISInputManager.h>
	*/
	
	private native void destroyInputManager (long inputManagerPtr); /*
		OIS::InputManager* inputManager = (OIS::InputManager*)inputManagerPtr;
		OIS::InputManager::destroyInputSystem(inputManager);
	*/
}
