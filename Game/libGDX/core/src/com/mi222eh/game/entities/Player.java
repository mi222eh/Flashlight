package com.mi222eh.game.entities;

import java.util.Random;

import com.mi222eh.game.handlers.Assets;

public class Player {

	private float time;
	private Random rand;
	private float stepInterval;

	public Player() {

		rand = new Random();
		stepInterval = 0.6F;
		time = 0F;

	}

	public void update(float delta) {
		time += delta;

		if (time >= stepInterval) {

			switch (rand.nextInt(3)) {
			case 0:
				Assets.PlayerStep1.play(1F, 1, 0);
				break;
			case 1:
				Assets.PlayerStep2.play(1F, 1F, 0);
				break;
			case 2:
				Assets.PlayerStep3.play(1F, 1F, 0);
				break;
			}
			time = 0;
		}

	}
}
