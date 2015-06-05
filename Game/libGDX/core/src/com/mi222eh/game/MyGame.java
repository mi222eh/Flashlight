package com.mi222eh.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mi222eh.game.handlers.Assets;
import com.mi222eh.game.screens.SplashScreen;

public class MyGame extends Game {

	// Constants
	public static float MAIN_MENU_BUTTON_WIDTH = 500;

	// Settings
	public static boolean sound;

	// Title
	public static final String TITLE = "Flashlight";

	// I have the width and height of the game
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	// MASKBIT CATEGORIRE
	public static final short BIT_LIGHT = 2;
	public static final short BIT_WALL = 4;
	public static final short BIT_TRAP = 8;
	public static final short BIT_PLAYER = 16;
	public static final short BIT_DOOR = 32;
	public static final short BIT_ENEMY = 64;

	// Number of maps
	public static final int NUMBER_OF_MAPS = 10;
	
	//Completed levels
	public static int COMPLETED_LEVELS;

	public OrthographicCamera camera;

	public MyGame() {

	}

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		Assets.Load();
		Assets.LoadMenuResources();
		setScreen(new SplashScreen(this));

		sound = Assets.getVolumeSetting();
		COMPLETED_LEVELS = Assets.GetNumberOfMapsCompleted();
	}
}
