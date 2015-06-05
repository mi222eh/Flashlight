package com.mi222eh.game.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mi222eh.game.MyGame;
import com.mi222eh.game.handlers.SpriteAccessor;

public class SplashScreen implements Screen {

	public MyGame game;
	private Texture texture;
	private Sprite splash;
	private TweenManager tweenmanager;
	private SpriteBatch batch;
	Tween tween;

	public SplashScreen(MyGame _game) {

		this.game = _game;
		batch = new SpriteBatch();
		texture = new Texture("img/splash.png");
		splash = new Sprite(texture);
		tweenmanager = new TweenManager();

		Tween.registerAccessor(Sprite.class, new SpriteAccessor());

		splash.setSize(MyGame.WIDTH, MyGame.HEIGHT);

		Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenmanager);
		tween = Tween.to(splash, SpriteAccessor.ALPHA, 2F).target(1)
				.repeatYoyo(1, 0F).setCallback(new TweenCallback() {

					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						game.setScreen(new MainMenuScreen(game));
					}
				}).start(tweenmanager);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		tweenmanager.update(delta);
		batch.setProjectionMatrix(game.camera.combined);
		batch.begin();
		splash.draw(batch);
		batch.end();
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)
				|| Gdx.input.isKeyJustPressed(Keys.ESCAPE)
				|| Gdx.input.justTouched()) {
			tween.kill();
			dispose();
			game.setScreen(new MainMenuScreen(game));
		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		texture.dispose();
		batch.dispose();

	}

}
