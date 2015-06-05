package com.mi222eh.game.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mi222eh.game.screens.GameScreen;

public class DeltaValues {

	@Test
	public void test() {
		float result = GameScreen.CalcDeltaValues(16, 8);
		assertEquals(8F, result, 0);
	}

}
