package com.mi222eh.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mi222eh.game.screens.GameScreen;

public class Enemy {
	
	private Body enemy;
	private float x;
	private float y;
	private float distance;
	private float degrees;
	private float cos;
	private float sin;
	private Vector2 sPoint;
	
	public Enemy(Body enemy, float x, float y){
		this.enemy = enemy;
		sPoint = new Vector2(x, y);
	}
	
	public Body getEnemy(){
		return enemy;
	}
	
	public void update(Vector2 playerPosition, boolean GameOver){
		
		x = playerPosition.x - enemy.getPosition().x;
		y = playerPosition.y - enemy.getPosition().y;
		distance = (float) Math.sqrt((x * x) + (y * y));
		
		
		if (!GameOver) {
			if (distance < 250) {
				degrees = GameScreen.FromRadianToDegrees(GameScreen.CalcAngle(x, y));
				
				cos = (float)Math.cos(GameScreen.CalcAngle(x, y));
				sin = (float)Math.sin(GameScreen.CalcAngle(x, y));
				
				enemy.setTransform(enemy.getPosition(), GameScreen.FromDegreesToRadians(degrees));
			    enemy.setLinearVelocity(68 * cos, 68 * sin);
			}
			else {
				if ((int)sPoint.x != (int)enemy.getPosition().x && (int)sPoint.y != (int)enemy.getPosition().y) {
					x = sPoint.x - enemy.getPosition().x;
					y = sPoint.y - enemy.getPosition().y;
					
					degrees = GameScreen.FromRadianToDegrees(GameScreen.CalcAngle(x, y));
					
					cos = (float)Math.cos(GameScreen.CalcAngle(x, y));
					sin = (float)Math.sin(GameScreen.CalcAngle(x, y));
					
					enemy.setTransform(enemy.getPosition(), GameScreen.FromDegreesToRadians(degrees));
				    enemy.setLinearVelocity(68 * cos, 68 * sin);
				}
				else {
					enemy.setLinearVelocity(0, 0);
					enemy.setAngularVelocity(0);
				}
				
			}
		}
		else {
			enemy.setLinearVelocity(0 ,0);
			enemy.setAngularVelocity(0);
		}
		
		
		
	}
	

}
