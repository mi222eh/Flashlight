package com.mi222eh.game.tests;

import org.junit.Assert;
import org.junit.Test;

import com.mi222eh.game.screens.GameScreen;

public class RadiansToDegreesTest {
	@Test
	public void test() {
		float radians = 1F;

		float degrees = GameScreen.FromRadianToDegrees(radians);

		Assert.assertEquals(57.2957795, degrees, 0.0001);
	}

}
