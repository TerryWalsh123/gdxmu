package com.gdxmu.utils;

public interface Listener {
	public enum GdxmuEvent { ERROR, NOTICE, HID, WIN_STATE };
	void onGdxmuEvent(GdxmuEvent ev, int param);
}
