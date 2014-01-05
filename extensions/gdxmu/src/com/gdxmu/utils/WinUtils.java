package com.gdxmu.utils;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Keys;
import com.gdxmu.utils.Listener.GdxmuEvent;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.BaseTSD.LONG_PTR;
import com.sun.jna.platform.win32.DBT;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HDEVNOTIFY;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

class WinUtils extends MiscUtils implements StdCallCallback {
	private static User32 user32;
	private HWND hWnd;
	private LONG_PTR lwjglWndProc;
	private ObjectMap<String, HDEVNOTIFY> notificationHandles = new ObjectMap<String, HDEVNOTIFY>();

	WinUtils(String windowTitle) {
		if (user32 == null)
			user32 = User32.GDXMU_INSTANCE;
		initializePointers(windowTitle);
	}
	
	private void initializePointers(String windowTitle) {
		hWnd = user32.FindWindow(null, windowTitle);
        
        if (hasValidHwnd()) {
            lwjglWndProc = new LONG_PTR(user32.GetWindowLong(hWnd, User32.GWL_WNDPROC));
            user32.SetWindowLong(hWnd, WinUser.GWL_WNDPROC, this);
            registerForHIDNotifications("HIDNotification");
        } else
            onError("Gdxmu: window with name '" + windowTitle + "' was not found. WinUtils will not operate properly.",
                    PARAM_ERROR_HWND_INVALID);
	}
    
    boolean hasValidHwnd() {
        return hWnd != null;
    }
	
	@Override
	public void windowWillDestroy() {
		super.windowWillDestroy();
		
		Keys<String> keys = notificationHandles.keys();
		while (keys.hasNext)
			unregisterForHIDNotifications(keys.next());
		destroyControllers();
	}
	
	@Override
	public void setWindow(String windowTitle) {
		super.setWindow(windowTitle);
		
		initializePointers(windowTitle);
        if (hasValidHwnd())
            createControllers();
	}

	public LRESULT callback(HWND hwnd, int uMsg, WPARAM wParam, LPARAM lParam) {
		switch (uMsg) {
			case WinUser.WM_SIZE:
			{
				int evParam = PARAM_NONE;
				long sizeType = wParam.longValue();
				
				if (sizeType == User32.SIZE_RESTORED)
					evParam = PARAM_WIN_STATE_RESTORED;
				else if (sizeType == User32.SIZE_MINIMIZED)
					evParam = PARAM_WIN_STATE_MINIMIZED;
				else if (sizeType == User32.SIZE_MAXIMIZED)
					evParam = PARAM_WIN_STATE_MAXIMIZED;
				else
					break;
				
				onEvent(GdxmuEvent.WIN_STATE, evParam);
			}	
				break;
			case WinUser.WM_DEVICECHANGE:
			{
				long dcType = wParam.longValue();
				if (dcType == DBT.DBT_DEVICEARRIVAL || dcType == DBT.DBT_DEVICEREMOVECOMPLETE) {
					long dbtType = lParam.longValue();
					
					if (dbtType != 0) {
						DBT.DEV_BROADCAST_HDR hdr = new DBT.DEV_BROADCAST_HDR(dbtType);
						
						if (hdr != null && hdr.dbch_devicetype == DBT.DBT_DEVTYP_DEVICEINTERFACE) {
							DBT.DEV_BROADCAST_DEVICEINTERFACE dvi = new DBT.DEV_BROADCAST_DEVICEINTERFACE(dbtType);
							
							if (dvi != null) {
								String hidFilter = getHIDFilter();
								
								if (hidFilter != null) {
									String deviceDesc = getDeviceRegValue("DeviceDesc", dvi);
									
									// Ignore the filter if device not found in registry
									if (deviceDesc != null && !deviceDesc.contains(hidFilter))
										break;
								}
								
								onEvent(GdxmuEvent.HID, dcType == DBT.DBT_DEVICEARRIVAL
										? PARAM_HID_ARRIVED
										: PARAM_HID_REMOVED);
							}
						}
					}
				}
			}
				break;
		}
		
		return user32.CallWindowProc(lwjglWndProc, hwnd, uMsg, wParam, lParam);
	}
	
	// Based on code from here:
	// http://stackoverflow.com/questions/2208722/how-to-get-friendly-device-name-from-dev-broadcast-deviceinterface-and-device-in
	private String getDeviceRegValue(String value, DBT.DEV_BROADCAST_DEVICEINTERFACE dvi) {
		try {
			String[] parts = dvi.getDbcc_name().split("#");
		    if (parts.length >= 3) {
		    	String DevType = parts[0].substring(parts[0].indexOf("?\\") + 2);
		    	String DeviceInstanceId = parts[1];
		    	String DeviceUniqueID = parts[2];
		        String RegPath = "SYSTEM\\CurrentControlSet\\Enum\\"
		        		+ DevType + "\\" + DeviceInstanceId + "\\" + DeviceUniqueID;
		        String regString = Advapi32Util.registryGetStringValue(
		        		WinReg.HKEY_LOCAL_MACHINE,
		        		RegPath,
		        		value);
		        return regString;
		    }
		} catch (Exception e) {
			onError("Gdxmu failed to apply HID filter.", PARAM_ERROR_HID_REGISTER);
		}
	    
	    return null;
	}
	
	@Override
	public void maximizeWindow() {
        if (hasValidHwnd())
            user32.PostMessage(hWnd, User32.WM_SYSCOMMAND, new WinDef.WPARAM(User32.SC_MAXIMIZE), new WinDef.LPARAM(0));
        else
            onError("Gdxmu maximizeWindow ignored due to invalid hwnd.", PARAM_ERROR_HWND_INVALID);
	}
	
	@Override
	public void registerForHIDNotifications(String uniqueID) {
        if (!hasValidHwnd()) {
            onError("Gdxmu registerForHIDNotifications ignored due to invalid hwnd.", PARAM_ERROR_HWND_INVALID);
            return;
        }
        
		if (uniqueID == null) {
			onError("Gdxmu registerForHIDNotifications requires a non-null key.", PARAM_ERROR_HID_REGISTER);
			return;
		}
		
		if (notificationHandles.containsKey(uniqueID)) {
			onError("Gdxmu registerForHIDNotifications key was not unique.", PARAM_ERROR_HID_REGISTER);
			return;
		}
		
		DBT.DEV_BROADCAST_DEVICEINTERFACE filter = new DBT.DEV_BROADCAST_DEVICEINTERFACE();    
		filter.dbcc_devicetype = DBT.DBT_DEVTYP_DEVICEINTERFACE;
		filter.dbcc_classguid = DBT.GUID_DEVINTERFACE_HID;
		filter.dbcc_size = filter.size();
		
		HDEVNOTIFY notificationHandle = user32.RegisterDeviceNotification(
				new HANDLE(hWnd.getPointer()),
				filter,
				User32.DEVICE_NOTIFY_WINDOW_HANDLE); // | User32.DEVICE_NOTIFY_ALL_INTERFACE_CLASSES);
		
		if (notificationHandle != null)
			notificationHandles.put(uniqueID, notificationHandle);
		else
			onError("Gdxmu failed to register for device notifications: " + uniqueID, PARAM_ERROR_HID_REGISTER);
	}
	
	@Override
	public void unregisterForHIDNotifications(String uniqueID) {
		if (uniqueID != null && notificationHandles.containsKey(uniqueID)) {
			if (user32.UnregisterDeviceNotification(notificationHandles.get(uniqueID)))
				notificationHandles.remove(uniqueID);
			else
				onError("Gdxmu unregisterForHIDNotifications failed.", PARAM_ERROR_HID_UNREGISTER);
		}
	}
}
