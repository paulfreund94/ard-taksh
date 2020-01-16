package com.ardtaksh.game;

import com.ardtaksh.game.ArdTakshGame.Direction;
import com.badlogic.gdx.graphics.Texture;

public class Ladder extends PickUp {

	public Ladder(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture) {
		super(game, x, y, width, height, speed, texture);
	}
	
	public void onPickUp(){
		game.levelNumber += 1;
		game.nextLevel();
	}

}
