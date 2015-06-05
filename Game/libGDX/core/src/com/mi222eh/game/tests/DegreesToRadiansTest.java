package com.mi222eh.game.tests;

import org.junit.Assert;
import org.junit.Test;

import com.mi222eh.game.screens.GameScreen;

public class DegreesToRadiansTest {

	@Test
	public void test() {
		float degrees = 90;
		
		float result = GameScreen.FromDegreesToRadians(degrees);
		
		Assert.assertEquals(1.57079633, result, 0.000001F);
	}

}
