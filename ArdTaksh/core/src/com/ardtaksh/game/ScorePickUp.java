package com.ardtaksh.game;

import com.badlogic.gdx.graphics.Texture;

public class ScorePickUp extends PickUp {
	int scoreIncrease;
	public ScorePickUp(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture, int scoreIncrease) {
		super(game, x, y, width, height, speed, texture);
		this.scoreIncrease += scoreIncrease;
		// TODO Auto-generated constructor stub
	}
	
	public void onPickUp(){
		game.score += scoreIncrease;
	}

}
