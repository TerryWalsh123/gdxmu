package com.gdxmu.controllers;

import com.badlogic.gdx.jnigen.AntScriptGenerator;
import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildExecutor;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.NativeCodeGenerator;
import com.badlogic.gdx.jnigen.BuildTarget.TargetOs;

public class GdxmuControllersBuild {
	public static void main (String[] args) throws Exception {
		new NativeCodeGenerator().generate("src/", "bin/", "jni/");
		BuildConfig buildConfig = new BuildConfig("gdxmu-controllers-desktop");
		
		String[] windowsSrc = {"*.cpp",
			"ois-v1-4svn/src/*.cpp",
			"ois-v1-4svn/src/win32/*.cpp",
			"HIDMonitor/src/HIDMonitor.cpp"
		};
		
		String[] linuxSrc = { "*.cpp",
			"ois-v1-4svn/src/*.cpp",
			"ois-v1-4svn/src/linux/*.cpp",
			"HIDMonitor/src/HIDMonitor.cpp"
		};
		
		String[] mac64Src = { "*.cpp",
			"ois-v1-4svn/src/*.cpp",
			"ois-v1-4svn/src/mac/*.mm",
			"ois-v1-4svn/src/mac/MacHIDManager.cpp",
			"ois-v1-4svn/src/mac/MacJoyStick.cpp",
			"HIDMonitor/src/HIDMonitor.cpp"
		};
		
		String[] includes = new String[] {
			"ois-v1-4svn/includes",
			"dinput/",
			"HIDMonitor/includes"
		};
		
		BuildTarget win32home = BuildTarget.newDefaultTarget(TargetOs.Windows, false);
		win32home.buildFileName = "build-windows32home.xml";
		win32home.excludeFromMasterBuildFile = true;
		win32home.is64Bit = false;
		win32home.compilerPrefix = "";
		win32home.cppIncludes = windowsSrc;
		win32home.headerDirs = includes;
		win32home.cIncludes = new String[0];
		win32home.libraries = "-ldinput8 -ldxguid";
		
		BuildTarget win32 = BuildTarget.newDefaultTarget(TargetOs.Windows, false);
		win32.cppIncludes = windowsSrc;
		win32.headerDirs = includes;
		win32.libraries = "-ldinput8 -ldxguid";
		
		BuildTarget win64 = BuildTarget.newDefaultTarget(TargetOs.Windows, true);
		win64.cppIncludes = windowsSrc;
		win64.headerDirs = includes;
		win64.libraries = "-ldinput8 -ldxguid";
		
		BuildTarget lin32 = BuildTarget.newDefaultTarget(TargetOs.Linux, false);
		lin32.cppIncludes = linuxSrc;
		lin32.headerDirs = includes;
		lin32.libraries = "-lX11";

		BuildTarget lin64 = BuildTarget.newDefaultTarget(TargetOs.Linux, true);
		lin64.cppIncludes = linuxSrc;
		lin64.headerDirs = includes;
		lin64.libraries = "-lX11";
		
		BuildTarget mac = BuildTarget.newDefaultTarget(TargetOs.MacOsX, false);
		mac.cppIncludes = mac64Src;
		mac.headerDirs = includes;
		mac.cppFlags += " -x objective-c++";
		mac.libraries = "-framework CoreServices -framework Carbon -framework IOKit -framework Cocoa";
		
		new AntScriptGenerator().generate(buildConfig, win32home, win32, win64, lin32); //, lin64, mac);
//		if(!BuildExecutor.executeAnt("jni/build-macosx32.xml", "-Dhas-compiler=true -v postcompile")) {
//			throw new Exception("build failed");
//		}
		BuildExecutor.executeAnt("jni/build.xml", "pack-natives");
	}
}
