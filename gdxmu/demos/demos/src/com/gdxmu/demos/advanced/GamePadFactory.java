package com.gdxmu.demos.advanced;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;

public class GamePadFactory {
	public static final int GAMEPAD_XBOX_360 = 1;
	
	private static IntMap<Class<? extends GamePadDescriptor>> descriptors =
			new IntMap<Class<? extends GamePadDescriptor>>();
	private static ObjectMap<Class<? extends GamePadDescriptor>,
		Class<? extends GamePadNormalizer>> normalizers =
			new ObjectMap<Class<? extends GamePadDescriptor>,
				Class<? extends GamePadNormalizer>>();
	
	private GamePadFactory() { }
	
	// Define type creations before creating stuff
	public static void defineGamePadType(int type,
			Class<? extends GamePadDescriptor> descClazz,
			Class<? extends GamePadNormalizer> normClazz)
	{
		descriptors.put(type, descClazz);
		normalizers.put(descClazz, normClazz);
	}
	
	public static GamePad createGamePad(String controllerName) {
		GamePadDescriptor desc = createGamePadDescriptor(controllerName);
		GamePadNormalizer normalizer = createGamePadNormalizer(desc);
		return new GamePad(desc, normalizer);
	}
	
	// TODO: map more controller types
	private static GamePadDescriptor createGamePadDescriptor(String name) {
		GamePadDescriptor gpd = null;
		if (name != null && (name.toLowerCase().contains("xbox")
				|| name.toLowerCase().contains("microsoft"))
				&& name.contains("360")) {
			Class<? extends GamePadDescriptor> clazz =
					descriptors.get(GAMEPAD_XBOX_360);
			
			try {
				Constructor<? extends GamePadDescriptor> ctor =
						clazz.getConstructor();
				gpd = ctor.newInstance();
			} catch (Exception e) {
				Gdx.app.log("Gdxmu.GamePadFactory", e.getMessage());
			}
		}

		return gpd;
	}
	
	@SuppressWarnings("unchecked")
	private static GamePadNormalizer createGamePadNormalizer(GamePadDescriptor desc) {
		GamePadNormalizer normalizer = null;
		if (desc != null && normalizers != null) {
			Class<? extends GamePadNormalizer> clazz = normalizers.get(desc.getClass());
			if (clazz != null) {
				try {
					Constructor<? extends GamePadNormalizer> ctor = null;
					Constructor<?>[] ctors = clazz.getConstructors();
					for (int i = 0; i < ctors.length; i++) {
						Class<?>[] params = ctors[i].getParameterTypes();
						if (params.length == 1) {
							if (params[0].isAssignableFrom(desc.getClass())) {
								ctor = (Constructor<? extends GamePadNormalizer>)ctors[i];
								break;
							}
						}
					}
					
					normalizer = ctor.newInstance(desc);
				} catch (Exception e) {
					Gdx.app.log("Gdxmu.GamePadFactory", e.getMessage());
				}
			}
		}
		
		return normalizer;
	}
}

