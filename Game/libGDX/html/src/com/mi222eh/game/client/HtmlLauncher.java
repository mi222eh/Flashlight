package com.mi222eh.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.mi222eh.game.MyGame;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(MyGame.WIDTH, MyGame.HEIGHT);
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return new MyGame();
	}
}