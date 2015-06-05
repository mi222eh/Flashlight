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

public class LevelScreen implements Screen {

	private MyGame game;
	private int level;
	private Stage stage;
	private TextButton play, back, Next, Previous;
	private TextButtonStyle style, darkStyle;
	private Table table, tableHeading;
	private Label heading, time, backLabel;

	@Override
	public void dispose() {
		stage.dispose();
	}

	public LevelScreen(MyGame game, int level) {

		this.game = game;
		this.level = level;

		stage = new Stage();

		table = new Table(Assets.buttonSkin);
		tableHeading = new Table();
		heading = new Label("Level " + level, new LabelStyle(Assets.font,
				Assets.font.getColor()));
		backLabel = new Label("Back", new LabelStyle(Assets.font,
				Assets.font.getColor()));
		time = new Label("Level not completed", new LabelStyle(Assets.font,
				Assets.font.getColor()));
		if (Assets.getTime(level) != 0F) {
			time.setText(Assets.getTime(level) + " seconds");
		}
	}

	@Override
	public void show() {
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

		//Play button
		play = new TextButton("Start", style);
		play.pad(20, 30, 20, 30);
		play.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				game.setScreen(new GameScreen(game, level));
			}
		});
		
		
		//Previous button
		if (level <= 1) {
			Previous = new TextButton("Prev.", darkStyle);
			Previous.pad(20, 30, 20, 30);
			Previous.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					// TODO Auto-generated method stub
					super.clicked(event, x, y);
					Assets.clickAudio(false);
				}
			});
		}
		else {
			Previous = new TextButton("Prev.", style);
			Previous.pad(20, 30, 20, 30);
			Previous.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					// TODO Auto-generated method stub
					super.clicked(event, x, y);
					Assets.clickAudio(true);
					game.setScreen(new LevelScreen(game, level - 1));
					dispose();
				}
			});
		}
		
		//Next button
		if (level >= MyGame.NUMBER_OF_MAPS || level >= MyGame.COMPLETED_LEVELS + 1) {
			Next = new TextButton("Next", darkStyle);
			Next.pad(20, 30, 20, 30);
			Next.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					// TODO Auto-generated method stub
					super.clicked(event, x, y);
					Assets.clickAudio(false);
				}
			});
		}
		else {
			Next = new TextButton("Next", style);
			Next.pad(20, 30, 20, 30);
			Next.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					// TODO Auto-generated method stub
					super.clicked(event, x, y);
					game.setScreen(new LevelScreen(game, level + 1));
					Assets.clickAudio(true);
				}
			});
		}
		
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
				game.setScreen(new LevelSelectScreen(game));
				dispose();
			}
		});
		tableHeading.pad(10);
		tableHeading.setBounds(0, MyGame.HEIGHT * 2 / 3F, MyGame.WIDTH,
				MyGame.HEIGHT * 1 / 3F);
		tableHeading.add(heading);
		tableHeading.row();
		tableHeading.add(time).pad(30, 0, 0, 0);

		table.pad(10);
		table.setBounds(0, 0, MyGame.WIDTH, MyGame.HEIGHT * 2 / 3F);
		table.add(play).pad(10).colspan(2).width(MyGame.MAIN_MENU_BUTTON_WIDTH);
		table.row();
		table.add(Previous).pad(10).width((MyGame.MAIN_MENU_BUTTON_WIDTH / 2) - 20);
		table.add(Next).pad(10).width((MyGame.MAIN_MENU_BUTTON_WIDTH / 2) - 20);

		stage.addActor(tableHeading);
		stage.addActor(back);
		stage.addActor(table);

		Gdx.input.setInputProcessor(stage);

		game.camera.update();
		game.camera.position.set(MyGame.WIDTH / 2, MyGame.HEIGHT / 2,
				game.camera.position.z);

		stage.getViewport().setCamera(game.camera);

		stage.getBatch().setProjectionMatrix(game.camera.combined);

	}

	@Override
	public void render(float delta) {
		game.camera.update();
		stage.getBatch().setProjectionMatrix(game.camera.combined);

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
