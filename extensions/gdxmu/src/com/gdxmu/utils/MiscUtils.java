package com.gdxmu.utils;

import com.gdxmu.controllers.Controllers;
import com.gdxmu.utils.Listener.GdxmuEvent;
import com.sun.jna.Platform;

public class MiscUtils {
	private static final int PARAM_SEPARATION = 1000;
	public static final int PARAM_NONE = 0;
	
	private static final int PARAM_ERROR = 1000;
    public static final int PARAM_ERROR_HWND_INVALID = PARAM_ERROR + 1;
	public static final int PARAM_ERROR_CTLS_CREATE_FAILED = PARAM_ERROR + 2;
	public static final int PARAM_ERROR_CTLS_DESTROY_FAILED = PARAM_ERROR + 3;
	public static final int PARAM_ERROR_HID_REGISTER = PARAM_ERROR + 4;
	public static final int PARAM_ERROR_HID_UNREGISTER = PARAM_ERROR + 5;
	
	private static final int PARAM_NOTICE = PARAM_ERROR + PARAM_SEPARATION;
	public static final int PARAM_NOTICE_CTLS_CREATED = PARAM_NOTICE + 1;
	public static final int PARAM_NOTICE_CTLS_WILL_DESTROY = PARAM_NOTICE + 2;
	
	private static final int PARAM_HID = PARAM_NOTICE + PARAM_SEPARATION;
	public static final int PARAM_HID_ARRIVED = PARAM_HID + 1;
	public static final int PARAM_HID_REMOVED = PARAM_HID + 2;
	
	private static final int PARAM_WIN_STATE = PARAM_HID + PARAM_SEPARATION;
	public static final int PARAM_WIN_STATE_RESTORED = PARAM_WIN_STATE + 1;
	public static final int PARAM_WIN_STATE_MINIMIZED = PARAM_WIN_STATE + 2;
	public static final int PARAM_WIN_STATE_MAXIMIZED = PARAM_WIN_STATE + 3;
	
	private static MiscUtils singleton;
	private String lastErrorMsg;
	private String hidFilter;
	private Listener listener;
	
	protected MiscUtils() { }
	
	public static MiscUtils initialize(String windowName) {
		if (singleton == null) {
			com.gdxmu.controllers.Controllers.initialize();
			
			if (Platform.isWindows()) {
				WinUtils winUtils = new WinUtils(windowName);
                if (winUtils.hasValidHwnd())
                    singleton = winUtils;
                else
                    throw new IllegalArgumentException("Invalid Window name arg passed to MiscUtils. WinUtils creation aborted.");
            } else if (Platform.isMac())
				singleton = new MacUtils();
			//else if (Platform.isX11())
			//	singleton = new LinUtils();
			
            if (singleton == null)
				singleton = new MiscUtils();
		}
        return singleton;
	}
	
	public static MiscUtils MU() {
		if (singleton == null)
			throw new IllegalStateException("Must call Gdxmu.MiscUtils.initialize(String windowName) first.");
        return singleton;
    }
	
	protected void onEvent(GdxmuEvent ev, int param) {
		if (listener != null && ev != null)
			listener.onGdxmuEvent(ev, param);
	}
	
	protected void onError(String msg, int param) {
		lastErrorMsg = msg;
		onEvent(GdxmuEvent.ERROR, param);
	}
	
	public String getLastErrorMsg() { return lastErrorMsg; }
	
	public void setListener(Listener listener) { this.listener = listener; }
	
	protected String getHIDFilter() { return hidFilter; }
	
	public void setHIDFilter(String filter) { hidFilter = filter; }
	
	// Call this before going to/from fullscreen.
	public void windowWillDestroy() { }
	
	// Call this after going to/from fullscreen
	public void setWindow(String windowTitle) { }
	
	public void refreshControllers() {
		destroyControllers();
		createControllers();
	}
	
	public void createControllers() {
		try {
			Controllers.createControllers();
			onEvent(GdxmuEvent.NOTICE, PARAM_NOTICE_CTLS_CREATED);
		} catch (IllegalStateException e) {
			onError("Gdxmu failed to create controllers" + e.getMessage(),
					PARAM_ERROR_CTLS_CREATE_FAILED);
		}
	}
	
	public void destroyControllers() {
		onEvent(GdxmuEvent.NOTICE, PARAM_NOTICE_CTLS_WILL_DESTROY);
		if (!Controllers.destroyControllers())
			onError("Gdxmu failed to destroy controllers.",
					PARAM_ERROR_CTLS_DESTROY_FAILED);
	}
	
	public void maximizeWindow() { }
	
	public void registerForHIDNotifications(String uniqueID) { }
	
	public void unregisterForHIDNotifications(String uniqueID) { }
	
	public void update() { }
}
