package com.gdxmu.utils;

import com.gdxmu.controllers.HIDMonitor;
import com.gdxmu.utils.Listener.GdxmuEvent;

class MacUtils extends MiscUtils {
	private HIDMonitor hidMonitor;
	
	MacUtils() {
		hidMonitor = new HIDMonitor();
	}
	
	public void update() {
		int hidChanges = hidMonitor.getDeviceListChanges();
		if (hidChanges != 0) {
			hidMonitor.clearDeviceListChanges();
			if ((hidChanges & HIDMonitor.HID_ARRIVED) == HIDMonitor.HID_ARRIVED)
				onEvent(GdxmuEvent.HID, PARAM_HID_ARRIVED);
			if ((hidChanges & HIDMonitor.HID_REMOVED) == HIDMonitor.HID_REMOVED)
				onEvent(GdxmuEvent.HID, PARAM_HID_REMOVED);
		}
	}
}

