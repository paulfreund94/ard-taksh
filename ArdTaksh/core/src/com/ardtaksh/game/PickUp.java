package com.ardtaksh.game;

import com.ardtaksh.game.ArdTakshGame.Direction;
import com.badlogic.gdx.graphics.Texture;

public class PickUp extends Entity {

	public PickUp(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture) {
		super(game, x, y, width, height, speed, texture);
		// TODO Auto-generated constructor stub
	}
	
	public void onPickUp(){
		
	}
	
	public void entityCollision(Entity e2, float newX, float newY, Direction direction) {
		super.entityCollision(e2, newX, newY, direction);
		if(e2 instanceof Player){
			onPickUp();
			game.entities.remove(this);
		}
	}

}
