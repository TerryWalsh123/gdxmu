
package com.gdxmu.controllers;

import com.badlogic.gdx.Gdx;

public class HIDMonitor  {
	public static final int HID_ARRIVED = 1<<0;
	public static final int HID_REMOVED = 1<<1;

	boolean isRunning;
	int deviceListChanges;
	
	public HIDMonitor () {
		monitorHIDs();
	}
	
	public void monitorHIDs () {
		if (isRunning)
			return;
		
		if (initHIDMonitor()) {
			isRunning = true;
			new Runnable () {
				public void run () {
					deviceListChanges |= update();
					Gdx.app.postRunnable(this);
				}
			}.run();
		}
	}
	
	public void stopMonitoringHIDs () {
		destroyHIDMonitor();
		clearDeviceListChanges();
		isRunning = false;
	}

	public boolean isRunning () { return isRunning; }

	public int getDeviceListChanges () {
		return deviceListChanges;
	}

	public void clearDeviceListChanges () {
		deviceListChanges = 0;
	}
	
	int update () {
		return updateHIDMonitor();
	}

	// @off
	/*JNI
	#include <HIDMonitor.h>
	*/

	private native boolean initHIDMonitor (); /*
		return HID::HIDMonitor::initHIDMonitor();
	*/

	private native int updateHIDMonitor (); /*
		return HID::HIDMonitor::updateHIDMonitor();
	*/

	private native void destroyHIDMonitor (); /*
		HID::HIDMonitor::destroyHIDMonitor();
	*/
}

