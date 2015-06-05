package com.mi222eh.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mi222eh.game.MyGame;
import com.mi222eh.game.entities.Door;
import com.mi222eh.game.entities.Enemy;
import com.mi222eh.game.entities.Player;
import com.mi222eh.game.handlers.Assets;
import com.mi222eh.game.handlers.MapHandler;

public class GameScreen implements Screen {

	private float lerp;
	private float angle;
	private float dx, dy;
	private Vector3 cursorPos, cameraPos;
	private FPSLogger fps;
	private OrthographicCamera hudcam;
	private SpriteBatch batch;
	private float time;
	private SpriteBatch worldBatch;
	private Player playerSound;
	private static MapHandler world;
	private MyGame game;
	private int level;
	private Stage stage;

	@Override
	public void dispose() {
		// I clean my game as much as i clean my room... Never
		world.dispose();
		worldBatch.dispose();
		Assets.DisposeMapResources();
		stage.dispose();
		batch.dispose();
		
		//Gives a "Hint" to the garbage collector
		System.gc();

	}

	public static void SetGameOverLight() {
		world.flashLight.setActive(false);
		world.glow.setDistance(200);
	}

	public GameScreen(MyGame myGame, int level) {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		this.level = level;
		lerp = 2F;
		game = myGame;
		Assets.LoadMapResources();

		world = new MapHandler(level);
		playerSound = world.createWorld();
		worldBatch = new SpriteBatch();
		time = 0;
		Box2D.init();
		
		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();

		hudcam = new OrthographicCamera();
		hudcam.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);

		game.camera.position.set(world.player.getBody().getPosition().x,
				world.player.getBody().getPosition().y, game.camera.position.z);

		game.camera.update();

		fps = new FPSLogger();
		
		

	}

	public static float CalcAngle(float deltaX, float deltaY) {
		return (float) Math.atan2(deltaY, deltaX);
	}

	public static float CalcDeltaValues(float to, float from) {
		return to - from;
	}

	public static float FromRadianToDegrees(float radian) {
		float degrees = (float) ((radian * 180) / Math.PI);
		return degrees;
	}
	public static float FromDegreesToRadians(float degrees){
		float radians = (float) (degrees * (Math.PI/180));
		return radians;
	}

	private void cursorToWorldPosition(float curX, float curY) {
		// Create a new vector for the mouse coords I just got
		Vector3 vecCursorPos = new Vector3(curX, curY, 0);
		game.camera.unproject(vecCursorPos);
		cursorPos = vecCursorPos;
	}

	private void UpdateCamerPosition(float delta) {
		cameraPos = game.camera.position;
		cameraPos.x += (world.player.getBody().getPosition().x - cameraPos.x)
				* lerp * delta;
		cameraPos.y += (world.player.getBody().getPosition().y - cameraPos.y)
				* lerp * delta;

	}
	private void mouseUpdate(){
		// Get cursor position
					cursorToWorldPosition(Gdx.input.getX(), Gdx.input.getY());

					// Time for some good old MATHS
					// Get the delta y and x
					dx = CalcDeltaValues(cursorPos.x, world.player.getBody()
							.getPosition().x);
					dy = CalcDeltaValues(cursorPos.y, world.player.getBody()
							.getPosition().y);

					// Calculate angle with tan
					angle = CalcAngle(dx, dy);

					// Prints the angle for fun
					// System.out.println(angle);

					// Sets the new angle!
					world.player.getBody().setTransform(
							world.player.getBody().getPosition(), angle);
	}
	
	private void updateEnemy(Body enemy){
		
		draw(enemy, Assets.EnemySprite);
	}
	private void draw(Body body, Sprite sprite){
		sprite.setPosition(
				body.getPosition().x - sprite.getWidth() / 2,
				body.getPosition().y - sprite.getHeight() / 2);
		sprite.setRotation(GameScreen.FromRadianToDegrees(body.getAngle()));
		sprite.draw(worldBatch);
	}
	private void draw(Body body){
		if (body.getUserData() != null && body.getUserData() instanceof Sprite) {
			Sprite sprite = (Sprite)body.getUserData();
			sprite.setPosition(
					body.getPosition().x - sprite.getWidth() / 2,
					body.getPosition().y - sprite.getHeight() / 2);
			sprite.setRotation(GameScreen.FromRadianToDegrees(body.getAngle()));
			sprite.draw(worldBatch);
		}
	}
	private void UpdateControls(float delta){
		/*
		if (Gdx.input.isKeyPressed(Keys.A)) {
			world.player.getBody().setLinearVelocity(-20000,
					world.player.getBody().getLinearVelocity().y);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			world.player.getBody().setLinearVelocity(20000,
					world.player.getBody().getLinearVelocity().y);
		}
		if (!Gdx.input.isKeyPressed(Keys.A)
				&& !Gdx.input.isKeyPressed(Keys.D)) {
			world.player.getBody().setLinearVelocity(0,
					world.player.getBody().getLinearVelocity().y);
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			world.player.getBody().setLinearVelocity(
					world.player.getBody().getLinearVelocity().x, 20000);
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			world.player.getBody().setLinearVelocity(
					world.player.getBody().getLinearVelocity().x, -20000);
		}
		if (!Gdx.input.isKeyPressed(Keys.W)
				&& !Gdx.input.isKeyPressed(Keys.S)) {
			world.player.getBody().setLinearVelocity(
					world.player.getBody().getLinearVelocity().x, 0);
		}
		*/
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			world.player.getBody().applyForceToCenter(-20000F, 0, true);
		}
		else if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			world.player.getBody().applyForceToCenter(20000F, 0, true);
		}
		else {
			world.player.getBody().setLinearVelocity(0,
					world.player.getBody().getLinearVelocity().y);
		}
		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
			world.player.getBody().applyForceToCenter(0, 20000F, true);
		}
		else if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
			world.player.getBody().applyForceToCenter(0, -20000F, true);
		}
	
		else{
			world.player.getBody().setLinearVelocity(
					world.player.getBody().getLinearVelocity().x, 0);
		}
		
		// Update player sound
		if ((Gdx.input.isKeyPressed(Keys.A)
				|| Gdx.input.isKeyPressed(Keys.D)
				|| Gdx.input.isKeyPressed(Keys.W) || Gdx.input
					.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN)) && MyGame.sound) {
			playerSound.update(delta);
		}
		mouseUpdate();
	}

	@Override
	public void render(float delta) {
		// ClearColor
		Gdx.gl.glClearColor(0, 0, 0, 1);
		// Clear
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Check if door is open
		if (world.cl.IsGoalOpen()) {
			world.goal.getBody().setUserData(Assets.GoalOpenSprite);
		}

		worldBatch.setProjectionMatrix(game.camera.combined);

		if (world.cl.dangerContact) {
			
			world.player.getBody().setLinearVelocity(0, 0);

		} else {
			time += delta;

			// Check controls
			UpdateControls(delta);
		}
		// Position camera on dude
			UpdateCamerPosition(delta);

		// -------------------RENDER------------------------//
		world.tmr.setView(game.camera);
		world.tmr.render();

		// Update camera
		game.camera.update();
		batch.setProjectionMatrix(hudcam.combined);

		// Render sprites
		worldBatch.begin();
		
		for (Body trap : world.traps) {
			draw(trap, Assets.TrapSprite);
		}
		for (Body lever : world.levers) {
			draw(lever);
		}
		draw(world.goal.getBody());
		for (Body hDoor : world.hDoors) {
			draw(hDoor);
		}
		for (Body vDoor : world.vDoors) {
			draw(vDoor);
		}
		for (Door door : world.doors) {
			door.update(delta);
		}
		for (Body body: world.hinges) {
			draw(body, Assets.HingeSprite);
		}
		for (Body vDoor : world.vDoors) {
			draw(vDoor, Assets.VDoorSprite);
		}
		for (Body hDoor : world.hDoors) {
			draw(hDoor, Assets.HDoorSprite);
		}
		for (Enemy enemy : world.enemies) {
			enemy.update(world.player.getBody().getPosition(), world.cl.dangerContact);
			updateEnemy(enemy.getEnemy());
		}
		draw(world.player.getBody(), Assets.PlayerSprite);

		worldBatch.end();

		// Render lights
		MapHandler.rayHandler.setCombinedMatrix(game.camera.combined);
		MapHandler.rayHandler.updateAndRender();
		

		int tmpTime = (int) ((time * 100));
		time = tmpTime / 100F;

		batch.begin();
		//Assets.font.draw(batch, String.valueOf(time), 10F, MyGame.HEIGHT - 10F);
		Assets.font.draw(batch, "Levers: " + world.cl.leversFlipped + "/"
				+ world.cl.numberOfLevers, 10F, MyGame.HEIGHT - 10F);
		if (world.cl.dangerContact) {
			Assets.GameOverSprite.draw(batch);
		}
		batch.end();

		// Do physics
		world.doPhysics(delta);

		fps.log();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.setScreen(new LevelScreen(game, level));
			dispose();
		}
		if (world.cl.finished) {
			Assets.SaveNumberOfMapsCompleted(level);
			time = time * 100;
			int timeTemp = (int) time;
			time = timeTemp / 100F;
			Assets.saveTime(level, time);
			game.setScreen(new LevelScreen(game, level));
			
			dispose();
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

}
