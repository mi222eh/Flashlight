package com.mi222eh.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mi222eh.game.MyGame;
import com.mi222eh.game.handlers.Assets;

public class SettingsScreen implements Screen{
	
	private MyGame game;
	private Stage stage;
	private TextButton back, sound, credits;
	private TextButtonStyle style, darkStyle;
	private Table table, tableHeading;
	private Label heading, backLabel;
	
	public SettingsScreen(MyGame game){
		this.game = game;
		
		stage = new Stage();
		table = new Table();
		tableHeading = new Table();
		
		heading = new Label("Settings", new LabelStyle(Assets.titleFont,
				Assets.titleFont.getColor()));
		backLabel = new Label("Back", new LabelStyle(Assets.font,
				Assets.font.getColor()));
		
		//Regular style
		style = new TextButtonStyle();
		style.up = Assets.buttonSkin.getDrawable("button.up");
		style.down = Assets.buttonSkin.getDrawable("button.down");
		style.pressedOffsetX = 1;
		style.pressedOffsetY = -1;
		style.font = Assets.font;
		
		//Dark style
		darkStyle = new TextButtonStyle();
		darkStyle.up = Assets.buttonDarkSkin.getDrawable("button.up");
		darkStyle.down = Assets.buttonDarkSkin.getDrawable("button.down");
		darkStyle.pressedOffsetX = 1;
		darkStyle.pressedOffsetY = -1;
		darkStyle.font = Assets.font;
	}
	@Override
	public void show() {
		
		//Credits button
		credits = new TextButton("Credits", style);
		credits.pad(20, 50, 20, 50);
		credits.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				
				game.setScreen(new CreditsScreen(game, 1));
				dispose();
				Assets.clickAudio(true);
			}
		});
		
		
		//Sound button
		sound = new TextButton("Sound:", style);

		if (MyGame.sound) {
			sound.setText("Sound:On");
		} else {
			sound.setText("Sound:Off");
		}
		sound.pad(20, 50, 20, 50);
		sound.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (MyGame.sound) {
					Assets.saveVolumeSetting(false);
					sound.setText("Sound:Off");
				} else {
					Assets.saveVolumeSetting(true);
					sound.setText("Sound:On");
				}
				Assets.clickAudio(true);

			}
		});
		
		//Back button
		back = new TextButton("", style);
		back.setSize(220, 45);
		back.add(backLabel);
		back.setPosition(20, 20);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				Assets.clickAudio(true);
				game.setScreen(new MainMenuScreen(game));
				dispose();
			}
		});
		
		Gdx.input.setInputProcessor(stage);
		
		tableHeading.setBounds(0, MyGame.HEIGHT * 3 / 4F, MyGame.WIDTH,
				MyGame.HEIGHT * 1 / 4F);
		tableHeading.add(heading).pad(20);

		table.setBounds(0, 0, MyGame.WIDTH, MyGame.HEIGHT * 3 / 4F);
		table.add(sound).width(MyGame.MAIN_MENU_BUTTON_WIDTH).pad(10);
		table.row();
		table.add(credits).width(MyGame.MAIN_MENU_BUTTON_WIDTH).pad(10);
		
		stage.addActor(back);
		stage.addActor(tableHeading);
		stage.addActor(table);
		
		game.camera.position.set(MyGame.WIDTH / 2, MyGame.HEIGHT / 2,
				game.camera.position.z);

		game.camera.update();

		stage.getViewport().setCamera(game.camera);

		stage.getBatch().setProjectionMatrix(game.camera.combined);
		
	}

	@Override
	public void render(float delta) {
		stage.getBatch().setProjectionMatrix(game.camera.combined);
		game.camera.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act();
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

	@Override
	public void dispose() {
		stage.dispose();
		
	}

}
