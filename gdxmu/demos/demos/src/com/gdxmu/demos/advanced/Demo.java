package com.gdxmu.demos.advanced;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdxmu.utils.Listener;
import com.gdxmu.utils.MiscUtils;

//NOTE: Press 'F' key to toggle fullscreen.
public class Demo implements ApplicationListener, Listener {
	// NOTE: It's important for Windows that WINDOW_NAME matches your
	// LwjglApplication title String.
	private static final String WINDOW_NAME = "Gdxmu Advanced Demo";
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private int w, h;
	private boolean isMaximized,isFKeyPressed;
	
	@Override
	public void create() {		
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/(float)w);
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		MiscUtils.initialize(WINDOW_NAME);
    	MiscUtils.MU().setListener(this);
    	Controllers.addListener(HIDManager.HM());
    	
    	// NOTE: To test maximize-on-launch functionality, set isMaximized
    	// to true. Typically this state would be read from a preferences file.
    	// (Currently only applicable to Windows builds. Linux & Mac will ignore.
    	// If requested, a zoom method could be added for Mac, but it seems
    	// unnecessary at this point.)
    	isMaximized = false;
    	if (isMaximized)
    		MiscUtils.MU().maximizeWindow();
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}
	
	@Override
	public void render() {
		// NOTE: You should call update at whatever time interval you deem
		// appropriate for the timely servicing of gamepad connections and
		// disconnections. Although unprofiled, there's not much happening
		// in update, so it's unlikely to negatively affect performance by
		// calling it each frame.
		MiscUtils.MU().update();
		HIDManager.HM().tick();
		
		// Check for fullscreen toggle via 'F' key. Toggle on
		// keyup to avoid bug where key can get stuck down.
		boolean newFKeyState = Gdx.input.isKeyPressed(Input.Keys.F);
		if (newFKeyState == false && isFKeyPressed)
			enableFullscreen(!isFullscreen());
		isFKeyPressed = newFKeyState;
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}
	
	public void maximizeWindow() {
		MiscUtils.MU().maximizeWindow();
	}
	
	public void setMaximized(boolean isMaximized) {
		this.isMaximized = isMaximized;
	}
	
	public boolean isFullscreen() { return Gdx.graphics.isFullscreen(); }
	
	// NOTE: Press 'F' key to toggle fullscreen.
	public void enableFullscreen(boolean enable) {
		if (enable != isFullscreen()) {
			// NOTE: detach from soon-to-be-dead window handle
			MiscUtils.MU().windowWillDestroy();
			
			boolean didChange = false;
			if (enable) {
				didChange = Gdx.graphics.setDisplayMode(
						Gdx.graphics.getDesktopDisplayMode().width,
						Gdx.graphics.getDesktopDisplayMode().height,
						true);
			} else {
				didChange = Gdx.graphics.setDisplayMode(
						w,
						h,
						false);
			}
			
			// NOTE: attach to new window handle
			MiscUtils.MU().setWindow(WINDOW_NAME);
			
			// Restore maximized window
			if (didChange && !enable && isMaximized)
				maximizeWindow();	
		}
	}

	@Override
	public void resize(int width, int height) {
		if (!isMaximized && !isFullscreen()) {
			w = width; h = height;
			Gdx.app.log("Window Size", "w:" + width + " h:" + height);
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void onGdxmuEvent(GdxmuEvent ev, int param) {
		switch (ev) {
			case ERROR:
				Gdx.app.log("Gdxmu Error", MiscUtils.MU().getLastErrorMsg());
				break;
			case NOTICE:
				switch (param) {
					case MiscUtils.PARAM_NOTICE_CTLS_CREATED:
						Gdx.app.log("Gdxmu Notice", "OisControllers created.");
						break;
					case MiscUtils.PARAM_NOTICE_CTLS_WILL_DESTROY:
						// NOTE: Important to prevent holding invalid pointers. See
						// note in HIDManager.
						HIDManager.HM().invalidateGamepads();
						Gdx.app.log("Gdxmu Notice", "OisControllers will destroy.");
						break;
				}
				break;
			case HID:
				switch (param) {
					case MiscUtils.PARAM_HID_ARRIVED:
						Gdx.app.log("Gdxmu HID", "HID arrived.");
						MiscUtils.MU().refreshControllers();
						break;
					case MiscUtils.PARAM_HID_REMOVED:
						Gdx.app.log("Gdxmu HID", "HID removed.");
						MiscUtils.MU().refreshControllers();
						break;
				}
				break;
			case WIN_STATE: // Currently Windows only
				switch (param) {
					case MiscUtils.PARAM_WIN_STATE_RESTORED:
						setMaximized(false);
						Gdx.app.log("Gdxmu Window State", "Window restored.");
						break;
					case MiscUtils.PARAM_WIN_STATE_MINIMIZED:
						Gdx.app.log("Gdxmu Window State", "Window minimized.");
						break;
					case MiscUtils.PARAM_WIN_STATE_MAXIMIZED:
						setMaximized(true);
						Gdx.app.log("Gdxmu Window State", "Window maximized.");
						break;
				}
				break;
		}
	}
}
