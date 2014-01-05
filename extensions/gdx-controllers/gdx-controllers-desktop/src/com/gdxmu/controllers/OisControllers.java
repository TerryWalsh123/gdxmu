package com.gdxmu.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.GraphicsType;
import com.badlogic.gdx.controllers.ControlType;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.desktop.ois.OisJoystick;
import com.badlogic.gdx.controllers.desktop.ois.OisJoystick.OisPov;
import com.badlogic.gdx.controllers.desktop.ois.OisListener;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

class OisControllers {
	final DesktopControllerManager manager;
	Ois ois = new Ois(getWindowHandle());
	OisController[] controllers;

	public OisControllers (DesktopControllerManager manager) {
		this.manager = manager;
		initialize();

		new Runnable() {
			public void run () {
				if (ois != null) ois.update();
				Gdx.app.postRunnable(this);
			}
		}.run();
	}
	
	private void initialize () {
		ois = new Ois(getWindowHandle());

		ArrayList<OisJoystick> joysticks = ois.getJoysticks();
		controllers = new OisController[joysticks.size()];
		
		for (int i = 0, n = joysticks.size(); i < n; i++) {
			OisJoystick joystick = joysticks.get(i);
			controllers[i] = new OisController(joystick);
			manager.controllers.add(controllers[i]);
		}
	}
	
	public void createControllers () {
		// Don't simply assume to destroy (in case client is holding old Controller references)
		if (ois != null)
			throw new IllegalStateException("First destroy controllers before re-creating them.");
		initialize();
	}
	
	public boolean destroyControllers () {
		manager.controllers.clear();
		boolean retval = ois.destroy();
		ois = null;
		return retval;
	}

	/** @author Nathan Sweet */
	class OisController implements Controller {
		private final OisJoystick joystick;
		final Array<ControllerListener> listeners = new Array();

		public OisController (OisJoystick joystick) {
			this.joystick = joystick;
			joystick.setListener(new OisListener() {
				public void buttonReleased (OisJoystick joystick, int buttonIndex) {
					Array<ControllerListener> allListeners = manager.listeners;
					for (int ii = 0, nn = allListeners.size; ii < nn; ii++)
						allListeners.get(ii).buttonUp(OisController.this, buttonIndex);
					for (int ii = 0, nn = listeners.size; ii < nn; ii++)
						listeners.get(ii).buttonUp(OisController.this, buttonIndex);
				}

				public void buttonPressed (OisJoystick joystick, int buttonIndex) {
					Array<ControllerListener> allListeners = manager.listeners;
					for (int ii = 0, nn = allListeners.size; ii < nn; ii++)
						allListeners.get(ii).buttonDown(OisController.this, buttonIndex);
					for (int ii = 0, nn = listeners.size; ii < nn; ii++)
						listeners.get(ii).buttonDown(OisController.this, buttonIndex);
				}

				public void axisMoved (OisJoystick joystick, int axisIndex, float value) {
					Array<ControllerListener> allListeners = manager.listeners;
					for (int ii = 0, nn = allListeners.size; ii < nn; ii++)
						allListeners.get(ii).axisMoved(OisController.this, axisIndex, value);
					for (int ii = 0, nn = listeners.size; ii < nn; ii++)
						listeners.get(ii).axisMoved(OisController.this, axisIndex, value);
				}

				public void povMoved (OisJoystick joystick, int povIndex, OisPov ignored) {
					PovDirection value = getPov(povIndex);
					Array<ControllerListener> allListeners = manager.listeners;
					for (int ii = 0, nn = allListeners.size; ii < nn; ii++)
						allListeners.get(ii).povMoved(OisController.this, povIndex, value);
					for (int ii = 0, nn = listeners.size; ii < nn; ii++)
						listeners.get(ii).povMoved(OisController.this, povIndex, value);
				}

				public void xSliderMoved (OisJoystick joystick, int sliderIndex, boolean value) {
					Array<ControllerListener> allListeners = manager.listeners;
					for (int ii = 0, nn = allListeners.size; ii < nn; ii++)
						allListeners.get(ii).xSliderMoved(OisController.this, sliderIndex, value);
					for (int ii = 0, nn = listeners.size; ii < nn; ii++)
						listeners.get(ii).xSliderMoved(OisController.this, sliderIndex, value);
				}

				public void ySliderMoved (OisJoystick joystick, int sliderIndex, boolean value) {
					Array<ControllerListener> allListeners = manager.listeners;
					for (int ii = 0, nn = allListeners.size; ii < nn; ii++)
						allListeners.get(ii).ySliderMoved(OisController.this, sliderIndex, value);
					for (int ii = 0, nn = listeners.size; ii < nn; ii++)
						listeners.get(ii).ySliderMoved(OisController.this, sliderIndex, value);
				}
			});
		}

		public boolean getButton (int buttonIndex) {
			return joystick.isButtonPressed(buttonIndex);
		}

		public float getAxis (int axisIndex) {
			return joystick.getAxis(axisIndex);
		}

		public PovDirection getPov (int povIndex) {
			OisPov pov = joystick.getPov(povIndex);
			switch (pov) {
			case Centered:
				return PovDirection.center;
			case East:
				return PovDirection.east;
			case North:
				return PovDirection.north;
			case NorthEast:
				return PovDirection.northEast;
			case NorthWest:
				return PovDirection.northWest;
			case South:
				return PovDirection.south;
			case SouthEast:
				return PovDirection.southEast;
			case SouthWest:
				return PovDirection.southWest;
			case West:
				return PovDirection.west;
			}
			return null; // Impossible.
		}

		public boolean getSliderX (int sliderIndex) {
			return joystick.getSliderX(sliderIndex);
		}

		public boolean getSliderY (int sliderIndex) {
			return joystick.getSliderY(sliderIndex);
		}

		public Vector3 getAccelerometer (int accelerometerIndex) {
			throw new GdxRuntimeException("Invalid accelerometer index: " + accelerometerIndex);
		}

		public void setAccelerometerSensitivity (float sensitivity) {
		}

		public int getControlCount (ControlType type) {
			switch (type) {
			case button:
				return joystick.getButtonCount();
			case axis:
				return joystick.getAxisCount();
			case slider:
				return joystick.getSliderCount();
			case pov:
				return joystick.getPovCount();
			}
			return 0;
		}

		public void addListener (ControllerListener listener) {
			listeners.add(listener);
		}

		public void removeListener (ControllerListener listener) {
			listeners.removeValue(listener, true);
		}

		public String getName () {
			return joystick.getName();
		}

		public String toString () {
			return joystick.getName();
		}
	}

	/** Returns the window handle from LWJGL needed by OIS. */
	static public long getWindowHandle () {
		// don't need a window handle for Mac OS X
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			return 0;
		}

		try {
			if (Gdx.graphics.getType() == GraphicsType.JGLFW)
				return (Long)Gdx.graphics.getClass().getDeclaredMethod("getWindow").invoke(null);

			if (Gdx.graphics.getType() == GraphicsType.LWJGL) {
				if (Gdx.app.getClass().getName().equals("com.badlogic.gdx.backends.lwjgl.LwjglCanvas")) {
					Class canvasClass = Class.forName("com.badlogic.gdx.backends.lwjgl.LwjglCanvas");
					Object canvas = canvasClass.getDeclaredMethod("getCanvas").invoke(Gdx.app);
					return (Long)invokeMethod(invokeMethod(SwingUtilities.windowForComponent((Component)canvas), "getPeer"), "getHWnd");
				}

				Class displayClass = Class.forName("org.lwjgl.opengl.Display");
				Method getImplementation = displayClass.getDeclaredMethod("getImplementation", new Class[0]);
				getImplementation.setAccessible(true);
				Object display = getImplementation.invoke(null, (Object[])null);
				String fieldName = System.getProperty("os.name").toLowerCase().contains("windows") ? "hwnd" : "parent_window";
				Field field = display.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				return (Long)field.get(display);
			}
		} catch (Exception ex) {
			throw new RuntimeException("Unable to get window handle.", ex);
		}

		return 0;
	}

	static private Object invokeMethod (Object object, String methodName) throws Exception {
		for (Method m : object.getClass().getMethods())
			if (m.getName().equals(methodName)) return m.invoke(object);
		throw new RuntimeException("Could not find method '" + methodName + "' on class: " + object.getClass());
	}
}
