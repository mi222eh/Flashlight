package com.mi222eh.game.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.mi222eh.game.MyGame;
import com.mi222eh.game.handlers.Assets;
import com.mi222eh.game.screens.GameScreen;

public class Door {
	
	private Body door;
	private float time;
	private float angle;
	
	public Door(Body door){
		this.door = door;
		time = 0;
		this.angle = GameScreen.FromRadianToDegrees(door.getAngle());
	}
	
	public void update(float delta){
		
		if (GameScreen.FromRadianToDegrees(door.getAngle()) != angle && (door.getAngularVelocity() > GameScreen.FromDegreesToRadians(20) || door.getAngularVelocity() < GameScreen.FromDegreesToRadians(-20))) {
			time += delta;
			if (time >= 0.3F) {
				if (MyGame.sound) {
					Assets.Door.play();
				}
				
				time = 0;
			}
		}
		angle = GameScreen.FromRadianToDegrees(door.getAngle());
		
		
		
	}

}
