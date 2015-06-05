package com.mi222eh.game.handlers;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Array;
import com.mi222eh.game.MyGame;
import com.mi222eh.game.entities.Door;
import com.mi222eh.game.entities.Enemy;
import com.mi222eh.game.entities.Lever;
import com.mi222eh.game.entities.Player;
import com.mi222eh.game.screens.GameScreen;

public class MapHandler {

	public World world;
	public int mapNumber;
	public TiledMap tilemap;
	public OrthogonalTiledMapRenderer tmr;
	public float tileSize;
	public Fixture player, goal;
	public static RayHandler rayHandler;
	private float accumulator, timeStep;
	public MyContactListener cl;
	public Vector2 playerStartPos;
	public OrthographicCamera camera;
	public SpriteBatch worldBatch;
	public Vector3 cursorPos;
	public ConeLight flashLight;
	public PointLight glow;
	public Array<Enemy> enemies;
	public Array<Door> doors;
	public Array<Body> traps;
	public Array<Body> levers;
	public Array<Body> hDoors;
	public Array<Body> vDoors;
	public Array<Body> hinges;
	
	

	public void dispose() {
		worldBatch.dispose();
		rayHandler.dispose();
		tmr.dispose();
		tilemap.dispose();
		world.dispose();
		doors.clear();
	}

	public void doPhysics(float delta) {

		// Frametime, either the delta or o.25 seconds (if the system lags
		// behind)
		float frameTime = Math.min(delta, 0.25F);

		// Save the time that got passed
		accumulator += frameTime;
		// While the time is more than the step time of the world, step the
		// world
		while (accumulator >= timeStep) {
			world.step(timeStep, 6, 2);
			accumulator -= timeStep;
		}

	}

	public MapHandler(int mapNumber) {

		worldBatch = new SpriteBatch();
		camera = new OrthographicCamera();
		cl = new MyContactListener();
		world = new World(new Vector2(0, 0), true);
		rayHandler = new RayHandler(world);

		accumulator = 0;
		timeStep = 1 / 50F;
		world.setContactListener(cl);
		this.mapNumber = mapNumber;
		tilemap = new TmxMapLoader().load("map/map" + mapNumber + ".tmx");
		tmr = new OrthogonalTiledMapRenderer(tilemap);
		
		doors = new Array<Door>();
		enemies = new Array<Enemy>();
		traps = new Array<Body>();
		levers = new Array<Body>();
		hDoors = new Array<Body>();
		vDoors = new Array<Body>();
		hinges = new Array<Body>();
	}

	public Player createWorld() {
		BodyDef bdef;
		FixtureDef fdef;
		PolygonShape shape = new PolygonShape();
		CircleShape circle = new CircleShape();
		TiledMapTileLayer layer = (TiledMapTileLayer) tilemap.getLayers().get(
				"wall");
		tileSize = layer.getTileWidth();
		Body body;

		// Walls
		for (int row = 0; row < layer.getHeight(); row++) {
			for (int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);

				if (cell == null || cell.getTile() == null) {
					continue;
				}
				bdef = new BodyDef();
				fdef = new FixtureDef();
				shape = new PolygonShape();

				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5F) * tileSize, (row + 0.5F)
						* tileSize);
				
				bdef.awake = false;

				shape.setAsBox(tileSize / 2, tileSize / 2);

				fdef.shape = shape;
				fdef.filter.categoryBits = MyGame.BIT_WALL;
				fdef.filter.maskBits = MyGame.BIT_LIGHT | MyGame.BIT_PLAYER | MyGame.BIT_ENEMY
						| MyGame.BIT_WALL;
				body = world.createBody(bdef);
				body.setSleepingAllowed(true);
				body.createFixture(fdef).setUserData("wall");
			}
		}

		// Traps
		MapLayer LayerTrap = tilemap.getLayers().get("trap");
		for (MapObject mo : LayerTrap.getObjects()) {
			bdef = new BodyDef();
			fdef = new FixtureDef();
			float x = mo.getProperties().get("x", float.class);
			float y = mo.getProperties().get("y", float.class);

			bdef.position.set(x + 15, y + 16);

			shape.setAsBox(15, 16);
			fdef.shape = shape;
			fdef.isSensor = true;

			fdef.filter.categoryBits = MyGame.BIT_TRAP;
			fdef.filter.maskBits = MyGame.BIT_PLAYER | MyGame.BIT_TRAP;

			body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("trap");

			Assets.TrapSprite.setSize(15 * 2, 16 * 2);

			body.setUserData(Assets.TrapSprite);
			
			traps.add(body);
		}
		// Levers
		MapLayer LayerLever = tilemap.getLayers().get("lever");
		for (MapObject mo : LayerLever.getObjects()) {
			bdef = new BodyDef();
			fdef = new FixtureDef();

			float x = mo.getProperties().get("x", float.class);
			float y = mo.getProperties().get("y", float.class);
			
			bdef.position.set(x + 16, y + 8);

			shape.setAsBox(16, 8);

			fdef.shape = shape;
			fdef.isSensor = true;

			fdef.filter.categoryBits = MyGame.BIT_TRAP;
			fdef.filter.maskBits = MyGame.BIT_PLAYER | MyGame.BIT_TRAP;

			body = world.createBody(bdef);
			body.createFixture(fdef).setUserData(new Lever());

			body.setUserData(Assets.LeverSprite);

			cl.numberOfLevers += 1;
			
			levers.add(body);

		}

		// Goal
		MapLayer LayerGoal = tilemap.getLayers().get("goal");
		for (MapObject mo : LayerGoal.getObjects()) {
			bdef = new BodyDef();
			fdef = new FixtureDef();
			float x = mo.getProperties().get("x", float.class);
			float y = mo.getProperties().get("y", float.class);

			bdef.position.set(x + 16, y + 16);

			shape.setAsBox(16, 16);
			fdef.shape = shape;
			fdef.isSensor = true;
			
			

			fdef.filter.categoryBits = MyGame.BIT_TRAP;
			fdef.filter.maskBits = MyGame.BIT_PLAYER | MyGame.BIT_TRAP;

			body = world.createBody(bdef);
			goal = body.createFixture(fdef);
			goal.setUserData("goal");

			if (cl.IsGoalOpen()) {
				body.setUserData(Assets.GoalOpenSprite);
			} else {
				body.setUserData(Assets.GoalSprite);
			}

			PointLight light = new PointLight(rayHandler, 100, new Color(0.84F,
					0.73F, 0.17F, 0.8F), 100, body.getPosition().x,
					body.getPosition().y);
			light.attachToBody(body);
		}
		
		//V - Doors
		MapLayer LayerVDoor = tilemap.getLayers().get("vdoor");
		for (MapObject mo : LayerVDoor.getObjects()) {
			
			//Door
			bdef = new BodyDef();
			fdef = new FixtureDef();
			float x = mo.getProperties().get("x", float.class);
			float y = mo.getProperties().get("y", float.class);

			bdef.position.set(x + 8, y + 16);
			bdef.type = BodyType.DynamicBody;

			shape.setAsBox(8, 32);
			fdef.shape = shape;
			fdef.density = 0.1F;
			fdef.friction = 20F;
			fdef.restitution = 0F;

			fdef.filter.categoryBits = MyGame.BIT_DOOR;
			fdef.filter.maskBits = MyGame.BIT_PLAYER | MyGame.BIT_LIGHT | MyGame.BIT_DOOR | MyGame.BIT_ENEMY;

			Body bodyA = world.createBody(bdef);
			
			bodyA.setUserData(Assets.VDoorSprite);
			bodyA.createFixture(fdef).setUserData("door");
			
			//Hinge
			circle = new CircleShape();
			fdef = new FixtureDef();
			bdef = new BodyDef();
			
			bdef.position.set(bodyA.getPosition().x, bodyA.getPosition().y - 16);
			bdef.type = BodyType.StaticBody;
			
			circle.setRadius(8);

			fdef.shape = circle;
			fdef.filter.categoryBits = MyGame.BIT_DOOR;
			
			Body bodyB = world.createBody(bdef);
			bodyB.setUserData(Assets.HingeSprite);
			bodyB.createFixture(fdef).setUserData("hinge");
			
			RevoluteJointDef RJdef = new RevoluteJointDef();
			RJdef.bodyA = bodyA;
			RJdef.bodyB = bodyB;
			
			RJdef.collideConnected = false;
			
			RJdef.localAnchorB.set(0, 0);
			RJdef.localAnchorA.set(0, -32);
			RJdef.lowerAngle = GameScreen.FromDegreesToRadians(-65);
			RJdef.upperAngle = GameScreen.FromDegreesToRadians(65);
			RJdef.enableLimit = false;
			
			RJdef.enableMotor = true;
			
			world.createJoint(RJdef);
			
			hinges.add(bodyB);
			
			//Limit joint
			
			circle = new CircleShape();
			fdef = new FixtureDef();
			bdef = new BodyDef();
			
			bdef.position.set(bodyA.getPosition().x , bodyA.getPosition().y + 60);
			bdef.type = BodyType.StaticBody;
			
			circle.setRadius(8);
			
			fdef.shape = circle;
			
			fdef.filter.categoryBits = MyGame.BIT_DOOR;
			fdef.filter.maskBits = MyGame.BIT_PLAYER | MyGame.BIT_LIGHT | MyGame.BIT_DOOR ;
			
			Body bodyC = world.createBody(bdef);
			bodyC.createFixture(fdef).setUserData("puller");
			
			RopeJointDef joint = new RopeJointDef();
			
			joint.bodyA = bodyA;
			joint.bodyB = bodyC;
			
			joint.maxLength = 85;
			joint.collideConnected = false;
			
			joint.localAnchorB.set(0, 0);
			joint.localAnchorA.set(0, 32);
			
			world.createJoint(joint);
			
			//Another Limit joint
			
			DistanceJointDef jdef = new DistanceJointDef();
			
			jdef.bodyA = bodyA;
			jdef.bodyB = bodyC;
			
			jdef.localAnchorA.set(0, 32);
			
			jdef.length = 2;
			jdef.dampingRatio = 2f;
			jdef.frequencyHz = 0.1F;
			
			world.createJoint(jdef);
			
			doors.add(new Door(bodyA));
			
			vDoors.add(bodyA);
			
		}
		//H - Doors
		MapLayer LayerHDoor = tilemap.getLayers().get("hdoor");
		for (MapObject mo : LayerHDoor.getObjects()) {
			
			//Door
			bdef = new BodyDef();
			fdef = new FixtureDef();
			float x = mo.getProperties().get("x", float.class);
			float y = mo.getProperties().get("y", float.class);

			bdef.position.set(x + 16, y + 8);
			bdef.type = BodyType.DynamicBody;

			shape.setAsBox(32, 8);
			fdef.shape = shape;
			fdef.density = 0.1F;
			fdef.friction = 20F;
			fdef.restitution = 0F;

			fdef.filter.categoryBits = MyGame.BIT_DOOR;
			fdef.filter.maskBits = MyGame.BIT_PLAYER | MyGame.BIT_LIGHT | MyGame.BIT_DOOR | MyGame.BIT_ENEMY;

			Body bodyA = world.createBody(bdef);
			
			bodyA.setUserData(Assets.HDoorSprite);
			bodyA.createFixture(fdef).setUserData("door");
			
			//Hinge
			circle = new CircleShape();
			fdef = new FixtureDef();
			bdef = new BodyDef();
			
			bdef.position.set(bodyA.getPosition().x - 16, (bodyA.getPosition().y));
			bdef.type = BodyType.StaticBody;
			
			circle.setRadius(8);

			fdef.shape = circle;
			fdef.filter.categoryBits = MyGame.BIT_DOOR;
			
			Body bodyB = world.createBody(bdef);
			bodyB.setUserData(Assets.HingeSprite);
			bodyB.createFixture(fdef).setUserData("hinge");
			
			RevoluteJointDef RJdef = new RevoluteJointDef();
			RJdef.bodyA = bodyA;
			RJdef.bodyB = bodyB;
			
			RJdef.collideConnected = false;
			
			RJdef.localAnchorB.set(0, 0);
			RJdef.localAnchorA.set(-32F, 0);
			RJdef.lowerAngle = GameScreen.FromDegreesToRadians(-65);
			RJdef.upperAngle = GameScreen.FromDegreesToRadians(65);
			RJdef.enableLimit = false;
			
			RJdef.enableMotor = true;
			
			world.createJoint(RJdef);
			
			hinges.add(bodyB);
			
			//Limit joint
			
			circle = new CircleShape();
			fdef = new FixtureDef();
			bdef = new BodyDef();
			
			bdef.position.set(bodyA.getPosition().x + 60, bodyA.getPosition().y);
			bdef.type = BodyType.StaticBody;
			
			circle.setRadius(8);
			
			fdef.shape = circle;
			
			fdef.filter.categoryBits = MyGame.BIT_DOOR;
			
			Body bodyC = world.createBody(bdef);
			bodyC.createFixture(fdef).setUserData("puller");
			
			RopeJointDef joint = new RopeJointDef();
			
			joint.bodyA = bodyA;
			joint.bodyB = bodyC;
			
			joint.maxLength = 85;
			joint.collideConnected = false;
			
			joint.localAnchorB.set(0, 0);
			joint.localAnchorA.set(32, 0);
			
			world.createJoint(joint);
			
			//Another Limit joint
			
			DistanceJointDef jdef = new DistanceJointDef();
			
			jdef.bodyA = bodyA;
			jdef.bodyB = bodyC;
			
			jdef.localAnchorA.set(32, 0);
			
			jdef.length = 2;
			jdef.dampingRatio = 2f;
			jdef.frequencyHz = 0.1F;
			
			world.createJoint(jdef);
			
			doors.add(new Door(bodyA));
			
			hDoors.add(bodyA);
		}
		
		//Enemies
		MapLayer Layer = tilemap.getLayers().get("enemy");
		for (MapObject mo : Layer.getObjects()) {
			bdef = new BodyDef();
			fdef = new FixtureDef();
			circle = new CircleShape();
			
			float x = mo.getProperties().get("x", float.class);
			float y = mo.getProperties().get("y", float.class);

			bdef.type = BodyType.DynamicBody;
			bdef.position.set(x + 10,y + 10);

			circle.setRadius(10);

			fdef.shape = circle;

			fdef.density = 1F;
			fdef.friction = 0.5F;
			fdef.restitution = 0f;
			fdef.filter.categoryBits = MyGame.BIT_ENEMY;
			fdef.filter.maskBits = MyGame.BIT_PLAYER
					| MyGame.BIT_WALL | MyGame.BIT_DOOR | MyGame.BIT_ENEMY;
			Body bodyE = world.createBody(bdef);
			
			bodyE.createFixture(fdef).setUserData("trap");
			
			bodyE.setUserData(Assets.EnemySprite);
			
			bodyE.setSleepingAllowed(false);
			
			enemies.add(new Enemy(bodyE, x , y));
		}

		Player player = CreatePlayer();
		shape.dispose();
		circle.dispose();
		
		return player;
	}

	private Player CreatePlayer() {
		Player player = new Player();
		BodyDef bdef;
		FixtureDef fdef;
		Body body;
		CircleShape shape = new CircleShape();
		MapLayer Layer = tilemap.getLayers().get("player");

		// Player
		for (MapObject mo : Layer.getObjects()) {
			bdef = new BodyDef();
			fdef = new FixtureDef();

			bdef.type = BodyType.DynamicBody;
			bdef.position.set(mo.getProperties().get("x", float.class) + 10, mo
					.getProperties().get("y", float.class) + 10);
			playerStartPos = new Vector2(mo.getProperties().get("x",
					float.class) + 10,
					mo.getProperties().get("y", float.class) + 10);

			shape.setRadius(10);

			fdef.shape = shape;

			fdef.density = 0.1F;
			fdef.friction = 0.5F;
			fdef.restitution = 0f;
			fdef.filter.categoryBits = MyGame.BIT_PLAYER;
			fdef.filter.maskBits = MyGame.BIT_LIGHT | MyGame.BIT_PLAYER
					| MyGame.BIT_WALL | MyGame.BIT_TRAP | MyGame.BIT_DOOR | MyGame.BIT_ENEMY;
			body = world.createBody(bdef);
			body.setUserData(Assets.PlayerSprite);
			this.player = body.createFixture(fdef);
			this.player.setUserData("player");
		}
		CreateLights();

		return player;
	}

	private void CreateLights() {

		flashLight = new ConeLight(rayHandler, 64, new Color(0.84F, 0.73F,
				0.17F, 0.8F), 800, player.getBody().getPosition().x, player
				.getBody().getPosition().y, 0, 25);
		flashLight.attachToBody(player.getBody());
		flashLight.setSoftnessLength(20F);
		flashLight.setXray(false);

		glow = new PointLight(rayHandler, 100, new Color(0.84F, 0.73F, 0.17F,
				0.7F), 100, player.getBody().getPosition().x, player.getBody()
				.getPosition().y);
		glow.attachToBody(player.getBody());
		PointLight
				.setContactFilter(
						MyGame.BIT_LIGHT,
						(short) 0,
						(short) (MyGame.BIT_LIGHT | MyGame.BIT_PLAYER | MyGame.BIT_WALL| MyGame.BIT_DOOR));
		Light.setContactFilter(
				MyGame.BIT_LIGHT,
				(short) 0,
				(short) (MyGame.BIT_LIGHT | MyGame.BIT_PLAYER | MyGame.BIT_WALL | MyGame.BIT_DOOR));
	}

}
