package com.mi222eh.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mi222eh.game.MyGame;

public class Assets {

	public static Texture PlayerTexture, TrapTexture, GoalTexture,
			GameOverTexture, LeverTexture, FlippedLeverTexture, EnemyTexture,
			GoalOpenTexture, HDoorTexture,VDoorTexture ,HingeTexture;
	public static Sprite PlayerSprite, TrapSprite, GoalSprite, GameOverSprite,
			LeverSprite, FlippedLeverSprite, GoalOpenSprite, HDoorSprite, VDoorSprite,HingeSprite, EnemySprite;
	public static Sound PlayerStep1, PlayerStep2, PlayerStep3, Dead, Lever,
			DoorOpen, Door, BlockClick, Click;
	public static BitmapFont font, titleFont;
	public static TextureAtlas buttonAtlas, buttonDarkAtlas;
	public static Skin buttonSkin, buttonDarkSkin;
	public static Preferences pref;

	public static void LoadMapResources() {
		GoalOpenTexture = new Texture("img/GoalOpen.png");
		GoalOpenSprite = new Sprite(GoalOpenTexture);

		LeverTexture = new Texture("img/leverNonFlipped.png");
		LeverSprite = new Sprite(LeverTexture);

		FlippedLeverTexture = new Texture("img/leverFlipped.png");
		FlippedLeverSprite = new Sprite(FlippedLeverTexture);

		GameOverTexture = new Texture("img/GameOver.png");
		GameOverSprite = new Sprite(GameOverTexture);

		PlayerTexture = new Texture("img/player.png");
		PlayerSprite = new Sprite(PlayerTexture);

		TrapTexture = new Texture("img/trap2.png");
		TrapSprite = new Sprite(TrapTexture);

		GoalTexture = new Texture("img/Goal.png");
		GoalSprite = new Sprite(GoalTexture);
		
		HDoorTexture = new Texture("img/hdoor.png");
		HDoorSprite = new Sprite(HDoorTexture);
		
		HingeTexture = new Texture("img/hinge.png");
		HingeSprite = new Sprite(HingeTexture);
		
		VDoorTexture = new Texture("img/vdoor.png");
		VDoorSprite = new Sprite(VDoorTexture);
		
		EnemyTexture = new Texture("img/enemy.png");
		EnemySprite = new Sprite(EnemyTexture);
		
		

	}
	public static void clickAudio(boolean success){
		if (MyGame.sound) {
			if (success) {
				Click.play();
			}
			else {
				BlockClick.play();
			}
			
		}
	}
	public static void DisposeMapResources() {
		PlayerTexture.dispose();
		TrapTexture.dispose();
		GoalTexture.dispose();
		GameOverTexture.dispose();
		GoalOpenTexture.dispose();
		HingeTexture.dispose();
		HDoorTexture.dispose();
		LeverTexture.dispose();
		FlippedLeverTexture.dispose();
		VDoorTexture.dispose();
		EnemyTexture.dispose();
		
	}

	public static void Load() {
		DoorOpen = Gdx.audio.newSound(Gdx.files.internal("sound/dooropen.wav"));
		Lever = Gdx.audio.newSound(Gdx.files.internal("sound/lever.wav"));
		PlayerStep1 = Gdx.audio.newSound(Gdx.files.internal("sound/walk.wav"));
		PlayerStep2 = Gdx.audio.newSound(Gdx.files.internal("sound/walk2.wav"));
		PlayerStep3 = Gdx.audio.newSound(Gdx.files.internal("sound/walk3.wav"));
		Dead = Gdx.audio.newSound(Gdx.files.internal("sound/dead.wav"));
		Door = Gdx.audio.newSound(Gdx.files.internal("sound/door.wav"));
		font = new BitmapFont(Gdx.files.internal("font/font.fnt"));
		pref = Gdx.app.getPreferences("Flashlight.details");
		BlockClick = Gdx.audio.newSound(Gdx.files.internal("sound/blockClick.wav"));
		Click = Gdx.audio.newSound(Gdx.files.internal("sound/click.wav"));
	}

	public static void LoadMenuResources() {
		buttonAtlas = new TextureAtlas("ui/button.pack");
		buttonSkin = new Skin();
		buttonSkin.addRegions(buttonAtlas);
		titleFont = new BitmapFont(Gdx.files.internal("font/font.fnt"));
		titleFont.setScale(3F);
		
		buttonDarkAtlas = new TextureAtlas("ui/buttonsDark.pack");
		buttonDarkSkin = new Skin();
		buttonDarkSkin.addRegions(buttonDarkAtlas);
	}

	public static void DisposeMenuResources() {
		buttonAtlas.dispose();
		buttonSkin.dispose();
		titleFont.dispose();
	}

	

	public static void saveTime(int level, float time) {
		if (getTime(level) == 0F || time < getTime(level)) {
			pref.putFloat("level" + level, time);
			pref.flush();
		}
		
		
	}

	public static float getTime(int level) {
		return pref.getFloat("level" + level, 0F);
	}

	public static void saveVolumeSetting(boolean sound) {
		MyGame.sound = sound;
		pref.putBoolean("sound", sound);
		pref.flush();
	}

	public static boolean getVolumeSetting() {
		return pref.getBoolean("sound", true);
	}
	public static void SaveNumberOfMapsCompleted(int amount){
		if (amount >= MyGame.COMPLETED_LEVELS) {
			MyGame.COMPLETED_LEVELS = amount;
			pref.putInteger("numberOfCompletedMaps", amount);
			pref.flush();
		}
	}
	public static int GetNumberOfMapsCompleted(){
		return pref.getInteger("numberOfCompletedMaps", 0);
	}

}
