package com.mi222eh.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class CreditsScreen implements Screen{
	private MyGame game;
	private int numberOfCredits;
	private int currentPage;
	private TextButtonStyle style, darkStyle;
	private TextButton back, Previous, Next;
	private Label backLabel;
	private Stage stage;
	private Texture credits;
	private Sprite drawPage;
	private SpriteBatch batch;
	private Table buttons;
	public CreditsScreen(MyGame game, int page) {
		
		this.game = game;
		stage = new Stage();
		buttons = new Table();
		currentPage = page;
		numberOfCredits = 3;
		batch = new SpriteBatch();
		credits = new Texture("img/credits/page" + currentPage + ".png");
		drawPage = new Sprite(credits);
		
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

	private void changePage(int page){
		currentPage = page;
		update();
	}
	
	private void update(){
		credits.dispose();
		credits = new Texture("img/credits/page" + currentPage + ".png");
		drawPage = new Sprite(credits);
		if (currentPage <= 1) {
			Previous.setStyle(darkStyle);
		}
		else {
			Previous.setStyle(style);
		}
		if (currentPage >= numberOfCredits) {
			Next.setStyle(darkStyle);
		}
		else {
			Next.setStyle(style);
		}
	}

	@Override
	public void show() {
		
		
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
				game.setScreen(new SettingsScreen(game));
				dispose();
			}
		});
		
		//Previous button
		Previous = new TextButton("Prev.", style);
		Previous.pad(20, 30, 20, 30);
		Previous.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				Assets.clickAudio((currentPage > 1));
				if (currentPage > 1) {
					changePage(currentPage - 1);
				}
			}
		});
		
		//Next button
		Next = new TextButton("Next", style);
		Next.pad(20, 30, 20, 30);
		Next.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				Assets.clickAudio((currentPage < numberOfCredits));
				if (currentPage < numberOfCredits) {
					changePage(currentPage + 1);
				}
			}
		});
		
		if (currentPage <= 1) {
			Previous.setStyle(darkStyle);
		}
		else {
			Previous.setStyle(style);
		}
		if (currentPage >= numberOfCredits) {
			Next.setStyle(darkStyle);
		}
		else {
			Next.setStyle(style);
		}
		
		buttons.setBounds(0, 0, MyGame.WIDTH, MyGame.HEIGHT / 4F);
		
		buttons.add(Previous).pad(5).width(MyGame.MAIN_MENU_BUTTON_WIDTH/2);
		buttons.add(Next).pad(5).width(MyGame.MAIN_MENU_BUTTON_WIDTH/2);
		
		stage.addActor(buttons);
		stage.addActor(back);
		
		Gdx.input.setInputProcessor(stage);
		stage.getViewport().setCamera(game.camera);
		stage.getBatch().setProjectionMatrix(game.camera.combined);
		
		batch.setProjectionMatrix(game.camera.combined);
		
	}

	@Override
	public void render(float delta) {
		game.camera.update();
		stage.getBatch().setProjectionMatrix(game.camera.combined);
		batch.setProjectionMatrix(game.camera.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		drawPage.draw(batch);
		batch.end();
		
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		batch.dispose();
		credits.dispose();
		
	}
}
