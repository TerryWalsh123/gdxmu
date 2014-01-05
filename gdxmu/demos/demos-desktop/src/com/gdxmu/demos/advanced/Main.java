package com.gdxmu.demos.advanced;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdxmu.demos.advanced.Demo;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Gdxmu Advanced Demo";
		cfg.useGL20 = false;
		cfg.vSyncEnabled = true;
		cfg.width = 480;
		cfg.height = 320;
		
		new LwjglApplication(new Demo(), cfg);
	}
}
