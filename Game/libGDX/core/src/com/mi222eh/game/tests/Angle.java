package com.mi222eh.game.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mi222eh.game.screens.GameScreen;

public class Angle {

	@Test
	public void testAngleCalc() {
		float angle = GameScreen.CalcAngle(6, 13);
		assertEquals(1.1383885512243588, angle, 0.00001);
	}
}
