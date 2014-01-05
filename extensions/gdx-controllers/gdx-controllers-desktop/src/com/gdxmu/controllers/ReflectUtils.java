package com.gdxmu.controllers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.badlogic.gdx.Gdx;

class ReflectUtils {
	static public Field getHiddenField(String fieldName, Class<?> clazz) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			Gdx.app.log("Gdxmu.ReflectUtils",
				"getHiddenField failed " + e.getMessage());
		}
		return null;
	}
	
	static public Object getHiddenObject(String fieldName, Object obj, Class<?> clazz) {
		try {
			Field field = getHiddenField(fieldName, clazz);
			return field.get(obj);
		} catch (Exception e) {
			Gdx.app.log("Gdxmu.ReflectUtils",
				"getHiddenObject failed " + e.getMessage());
		}
		return null;
	}
	
	static public Object callHiddenStaticVoidMethod(String fieldName, Class<?> clazz) {
		try {
			Method m = clazz.getDeclaredMethod(fieldName);
			m.setAccessible(true);
			return m.invoke(null);
		} catch (Exception e) {
			Gdx.app.log("Gdxmu.ReflectUtils",
				"callHiddenStaticVoidMethod failed " + e.getMessage());
		}
		return null;
	}
}
