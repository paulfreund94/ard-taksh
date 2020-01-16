package com.ardtaksh.game;

import com.badlogic.gdx.graphics.Texture;

public class HealthPickUp extends PickUp {

	public HealthPickUp(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture) {
		super(game, x, y, width, height, speed, texture);
		// TODO Auto-generated constructor stub
	}
	
	public void onPickUp(){
		this.game.p.hitpoints += 2;
		if(game.p.hitpoints > 10){
			game.p.hitpoints = 10;
		}
	}

}
