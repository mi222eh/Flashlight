package com.mi222eh.game.handlers;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mi222eh.game.MyGame;
import com.mi222eh.game.entities.Lever;
import com.mi222eh.game.screens.GameScreen;

public class MyContactListener implements ContactListener {

	public boolean dangerContact = false;
	public boolean finished = false;
	public boolean playerCanDie = true;
	public boolean GoalOpen = false;
	public int numberOfLevers = 0;
	public int leversFlipped = 0;

	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		// TRAP
		if (fa.getUserData() != null && fa.getUserData().equals("trap") && fb.getUserData() != null && fb.getUserData().equals("player")) {
			if (playerCanDie) {
				dangerContact = true;
				if (MyGame.sound) {
					Assets.Dead.play(0.3F);
				}

				GameScreen.SetGameOverLight();
				if (fb.getUserData().equals("player")) {
					fb.getBody().setLinearVelocity(0, 0);
					fb.getBody().setAngularVelocity(90);
				}
			}
		}
		if (fb.getUserData() != null && fb.getUserData().equals("trap") && fa.getUserData() != null && fa.getUserData().equals("player")) {
			if (playerCanDie) {
				dangerContact = true;
				if (MyGame.sound) {
					Assets.Dead.play(0.3F);
				}
				GameScreen.SetGameOverLight();
				if (fa.getUserData().equals("player")) {
					fa.getBody().setLinearVelocity(0, 0);
					fa.getBody().setAngularVelocity(90);
				}
			}

		}
		if (IsGoalOpen()) {
			// GOAL
			if ((fa != null && fa.getUserData().equals("goal") && fb.getUserData() != null && fb.getUserData().equals("player")) || fb != null
					&& fb.getUserData().equals("goal") && fa.getUserData() != null && fa.getUserData().equals("player")) {
				finished = true;
				playerCanDie = false;
			}
		}
		// LEVER
		if ((fa.getUserData() != null && fa.getUserData() instanceof Lever)
				|| (fb.getUserData() != null && fb.getUserData() instanceof Lever)) {
			if (fa.getUserData() instanceof Lever) {
				Lever lever = (Lever) fa.getUserData();
				if (!lever.IsFlipped() && lever != null) {

					FlipLever();
					lever.Flip();
					fa.getBody().setUserData(Assets.FlippedLeverSprite);
					addLight(fa.getBody());
				}
			} else {
				Lever lever = (Lever) fb.getUserData();
				if (!lever.IsFlipped() && lever != null) {
					FlipLever();
					lever.Flip();
					fb.getBody().setUserData(Assets.FlippedLeverSprite);
					addLight(fb.getBody());
				}
			}
		}
	}

	private void addLight(Body body) {
		new PointLight(MapHandler.rayHandler, 100, new Color(0.84F, 0.73F,
				0.17F, 0.8F), 50, 0, 0).attachToBody(body);
	}

	private void FlipLever() {
		leversFlipped += 1;

		if (MyGame.sound) {
			if (IsGoalOpen()) {
				Assets.DoorOpen.play();
			} else {
				Assets.Lever.play();
			}

		}

	}

	public boolean IsGoalOpen() {
		return (leversFlipped >= numberOfLevers);
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
