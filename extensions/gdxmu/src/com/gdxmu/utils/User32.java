package com.gdxmu.utils;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.BaseTSD.LONG_PTR;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;

interface User32 extends com.sun.jna.platform.win32.User32 {
	User32 GDXMU_INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
	
	long SIZE_RESTORED = 0;
	long SIZE_MINIMIZED = 1;
	long SIZE_MAXIMIZED = 2;
	long SIZE_MAXSHOW = 3;
	long SIZE_MAXHIDE = 4;

    int SetWindowLong(WinDef.HWND hWnd, int nIndex, Callback callback);
    LRESULT CallWindowProc(LONG_PTR lpPrevWndFunc, HWND hWnd, int uMsg, WPARAM wParam, LPARAM lParam);
}
