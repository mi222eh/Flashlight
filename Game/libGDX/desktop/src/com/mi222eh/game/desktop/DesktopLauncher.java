package com.mi222eh.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mi222eh.game.MyGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = MyGame.TITLE;
		config.width = MyGame.WIDTH;
		config.height = MyGame.HEIGHT;
		config.useGL30 = false;
		config.resizable = true;
		config.addIcon("img/icon.png", Files.FileType.Internal);
		new LwjglApplication(new MyGame(), config);
	}
}
