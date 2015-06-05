package com.mi222eh.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mi222eh.game.MyGame;
import com.mi222eh.game.handlers.Assets;

public class LevelSelectScreen implements Screen {

	private Stage stage;
	private MyGame game;
	private Table table, tableHeading;
	private Label heading, backLabel;
	private TextButtonStyle style, darkStyle;
	private int level;
	private TextButton back;
	private Array<TextButton> buttons;

	@Override
	public void dispose() {
		stage.dispose();
		buttons.clear();

	}

	public LevelSelectScreen(MyGame game) {

		game.camera = new OrthographicCamera();
		game.camera.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
		game.camera.position.set(MyGame.WIDTH / 2, MyGame.HEIGHT / 2,
				game.camera.position.z);

		// Button style
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

		buttons = new Array<TextButton>();
		stage = new Stage();
		table = new Table(Assets.buttonSkin);
		tableHeading = new Table();
		heading = new Label("Select Level", new LabelStyle(Assets.font,
				Assets.font.getColor()));
		backLabel = new Label("Back", new LabelStyle(Assets.font,
				Assets.font.getColor()));

		this.game = game;
	}

	@Override
	public void show() {

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

		for (level = 0; level < MyGame.NUMBER_OF_MAPS; level++) {
			final int mapLevel = level + 1;
			TextButton button = new TextButton("" + mapLevel, style);
			button.pad(10, 10, 10, 10);
			if (level < MyGame.COMPLETED_LEVELS + 1) {
				button.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						super.clicked(event, x, y);
						Assets.clickAudio(true);
						game.setScreen(new LevelScreen(game, mapLevel));
						dispose();
					}
				});
			}
			else {
				button.setStyle(darkStyle);
				button.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent event, float x, float y) {
						// TODO Auto-generated method stub
						super.clicked(event, x, y);
						Assets.clickAudio(false);
					}
				});
			}
			
			buttons.add(button);
		}

		tableHeading.setBounds(0, MyGame.HEIGHT * 3 / 4F, MyGame.WIDTH,
				MyGame.HEIGHT * 1 / 4F);
		tableHeading.add(heading).pad(20);

		table.setBounds(0, 0, MyGame.WIDTH, MyGame.HEIGHT * 3 / 4F);
		table.row();
		int tmpRowChangeValue = 0;
		for (int i = 0; i < buttons.size; i++) {
			if (tmpRowChangeValue >= 5) {
				table.row();
				tmpRowChangeValue = 0;
			}
			table.add(buttons.get(i)).pad(10).width(200);
			tmpRowChangeValue++;
		}

		stage.addActor(tableHeading);
		stage.addActor(table);
		stage.addActor(back);
		Gdx.input.setInputProcessor(stage);

		game.camera.update();
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
