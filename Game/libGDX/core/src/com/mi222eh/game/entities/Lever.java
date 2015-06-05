package com.mi222eh.game.entities;

public class Lever {
	private boolean IsFlipped;

	public Lever() {
		IsFlipped = false;
	}

	public boolean IsFlipped() {
		return IsFlipped;
	}

	public void Flip() {
		IsFlipped = true;
	}
}
