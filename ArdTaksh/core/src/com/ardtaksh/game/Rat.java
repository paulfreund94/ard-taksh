package com.ardtaksh.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Rat extends Enemy {

	public Rat(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture) {
		super(game, x, y, width, height, speed, texture);
		this.damageOnCollision = 1;
		// TODO Auto-generated constructor stub
	}
	
	
	public void update(float delta){
		// look at the player
		super.update(delta);
		this.angle = MathUtils.radiansToDegrees * MathUtils.atan2(game.p.centerY - this.centerY, game.p.centerX - this.centerX);
		super.update(delta);
		/**
		if((game.p.centerX - this.centerX)*(game.p.centerX - this.centerX) + (game.p.centerY - this.centerY)*(game.p.centerY - this.centerY) < 320*320){
			// move towards the player
			Vector2 dyAndDx = getNextDyByDx();
			dy = Math.signum(dyAndDx.y) * delta * speed;
			dx = Math.signum(dyAndDx.x) * delta * speed;
		}
		**/
		if(game.playerChangedTile){
			p = game.pathfinder.findPath(tileCenterY, tileCenterX, game.p.tileCenterY, game.p.tileCenterX);
		}
		
		Vector2 dyAndDx = getNextDyByDx();
		dy = Math.signum(dyAndDx.y) * delta * speed;
		dx = Math.signum(dyAndDx.x) * delta * speed;
		
	}
	
	public Vector2 getNextDyByDx(){
		
		Vector2 position;
		if(p.successor != null){
			position = new Vector2(p.successor.y * game.tileSize + 16, p.successor.x * game.tileSize + 16);
			
		}else{
			position = new Vector2(game.p.centerX, game.p.centerY);
		}
		
		Vector2 direction = new Vector2();
		direction.set(position).sub(new Vector2(this.x, this.y)).nor();
		
		return direction;
	}

}
