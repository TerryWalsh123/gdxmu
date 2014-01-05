package com.gdxmu.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.ControllerManager;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class Controllers extends com.badlogic.gdx.controllers.Controllers {
	private static final String TAG = "Gdxmu.Controllers";
	
	static private DesktopControllerManager getManager() {
		return (DesktopControllerManager)ReflectUtils.callHiddenStaticVoidMethod(
				"getManager", com.badlogic.gdx.controllers.Controllers.class);
	}
	
	static public void createControllers() {
		DesktopControllerManager manager = getManager();
		if (manager != null)
			manager.createControllers();
    }
	
	static public boolean destroyControllers() {
		DesktopControllerManager manager = getManager();
		return manager == null || manager.destroyControllers();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static public void initialize() {
		final ObjectMap<Application, ControllerManager> managers =
				(ObjectMap<Application, ControllerManager>)ReflectUtils.getHiddenObject(
						"managers", null, com.badlogic.gdx.controllers.Controllers.class);
		if(managers.containsKey(Gdx.app)) return;
		
		String className = null;
		ApplicationType type = Gdx.app.getType();
		ControllerManager manager = null;
		
		if(type != ApplicationType.Desktop) {
			throw new IllegalStateException(
					"com.gdxmu.controllers.Controllers only supports desktop apps.");
		}
		
		className = "com.gdxmu.controllers.DesktopControllerManager";
		
		try {
			Class controllerManagerClass = ClassReflection.forName(className);
			manager = (ControllerManager)ClassReflection.newInstance(controllerManagerClass);
		} catch (Throwable ex) {
			throw new GdxRuntimeException("Error creating controller manager: " + className, ex);
		}
		
		managers.put(Gdx.app, manager);
		final Application app = Gdx.app;
		Gdx.app.addLifecycleListener(new LifecycleListener() {
                
                @Override
                public void resume () {
                }
                
                @Override
                public void pause () {
                }
                
                @Override
                public void dispose () {
                        managers.remove(app);
                        Gdx.app.log(TAG, "removed manager for application, " + managers.size + " managers active");

                }
        });
		Gdx.app.log(TAG, "added manager for application, " + managers.size + " managers active");
	}
}
