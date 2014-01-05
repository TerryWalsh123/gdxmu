package com.gdxmu.demos.advanced;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;

public class GamePadAxes implements Poolable {
	public int x;
	public int y;
	
	public GamePadAxes() { }
	
	public GamePadAxes(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public GamePadAxes set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public GamePadAxes set(GamePadAxes other) {
		if (other != null)
			return set(other.x, other.y);
		else
			return this;
	}

	public boolean isCentered() {
		return x == 0 && y == 0;
	}

	@Override
	public void reset() {
		x = 0;
		y = 0;
	}
	
	public static GamePadAxes obtainAxes() {
		return obtainAxes(0, 0);
	}
	
	public static GamePadAxes obtainAxes(GamePadAxes axes) {
		return obtainAxes(axes.x, axes.y);
	}
	
	public static GamePadAxes obtainAxes(int x, int y) {
		GamePadAxes axes = Pools.obtain(GamePadAxes.class);
		axes.set(x, y);
		return axes;
	}
	
	public static void freeAxes(GamePadAxes axes) {
		if (axes != null)
			Pools.free(axes);
	}
}

