package com.mi222eh.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mi222eh.game.MyGame;
import com.mi222eh.game.handlers.Assets;

public class MainMenuScreen implements Screen {

	private Stage stage;
	private MyGame game;
	private Table tableButtons, tableHeading;
	private TextButton buttonPlay, buttonExit, buttonVolume, buttonInstructions;
	private Label heading;
	private TextButtonStyle style;

	@Override
	public void dispose() {
		stage.dispose();

	}

	public MainMenuScreen(MyGame _game) {

		// set game and load
		game = _game;

		// new stage, tables
		stage = new Stage();
		tableButtons = new Table(Assets.buttonSkin);
		tableHeading = new Table();

		// button style
		style = new TextButtonStyle();
		style.up = Assets.buttonSkin.getDrawable("button.up");
		style.down = Assets.buttonSkin.getDrawable("button.down");
		style.pressedOffsetX = 1;
		style.pressedOffsetY = -1;
		style.font = Assets.font;

		// Buttons
		//Volume Button
			buttonVolume = new TextButton("Settings", style);
			buttonVolume.pad(20, 50, 20, 50);
			buttonVolume.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					game.setScreen(new SettingsScreen(game));
					Assets.clickAudio(true);

				}
			});

		//Play button
		buttonPlay = new TextButton("Play", style);

		buttonPlay.pad(20, 30, 20, 30);

		buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);

				Assets.clickAudio(true);
				game.setScreen(new LevelSelectScreen(game));
				dispose();
			}
		});
		
		//Exit button
		
		buttonExit = new TextButton("Exit", style);
		buttonExit.pad(20, 30, 20, 30);

		buttonExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				
				Assets.clickAudio(true);

				Gdx.app.exit();
			}
		});
		

		//Button intructions
		buttonInstructions = new TextButton("Instructions", style);
		buttonInstructions.pad(20, 30, 20, 30);
		
		buttonInstructions.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				Assets.clickAudio(true);
				game.setScreen(new InstructionsScreen(game, 1));
			}
		});

		heading = new Label("Flashlight", new LabelStyle(Assets.titleFont,
				Assets.titleFont.getColor()));

		// Main menu stage
		tableHeading.setBounds(0, MyGame.HEIGHT * 2 / 3F, MyGame.WIDTH,
				MyGame.HEIGHT * 1 / 3F);
		tableHeading.add(heading);

		tableButtons.setBounds(0, 0, MyGame.WIDTH, MyGame.HEIGHT * 2 / 3F);
		tableButtons.center();
		tableButtons.add(buttonPlay).pad(10)
				.width(MyGame.MAIN_MENU_BUTTON_WIDTH);
		tableButtons.row();
		tableButtons.add(buttonInstructions).pad(10).width(MyGame.MAIN_MENU_BUTTON_WIDTH);
		tableButtons.row();
		tableButtons.add(buttonVolume).pad(10)
				.width(MyGame.MAIN_MENU_BUTTON_WIDTH);
		tableButtons.row();
		tableButtons.add(buttonExit).pad(10)
				.width(MyGame.MAIN_MENU_BUTTON_WIDTH);
		
		tableButtons.center();

		stage.addActor(tableHeading);
		stage.addActor(tableButtons);
		Gdx.input.setInputProcessor(stage);

		game.camera.position.set(MyGame.WIDTH / 2, MyGame.HEIGHT / 2,
				game.camera.position.z);

		game.camera.update();

		stage.getViewport().setCamera(game.camera);

		stage.getBatch().setProjectionMatrix(game.camera.combined);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		stage.getBatch().setProjectionMatrix(game.camera.combined);
		game.camera.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height);
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

}
