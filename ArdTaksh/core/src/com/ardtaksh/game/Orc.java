package com.ardtaksh.game;

import java.util.Random;

import com.ardtaksh.game.ArdTakshGame.Direction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Orc extends Enemy{
	boolean yAxis;
	boolean goingDownAxis;
	boolean playerInRange;
	Random r;
	float timeSinceLastBullet;
	public Orc(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture) {
		super(game, x, y, width, height, speed, texture);
		this.hitpoints = 2;
		timeSinceLastBullet = 0.7f;
		damageOnCollision = 1;
		r = new Random();
		// TODO Auto-generated constructor stub
	}
	
	public void update(float delta){
		super.update(delta);
		if((game.p.centerX - this.centerX)*(game.p.centerX - this.centerX) + (game.p.centerY - this.centerY)*(game.p.centerY - this.centerY) < 320*320){
			 if(playerInRange = false){
				 playerInRange = true;
				 goingDownAxis = r.nextBoolean();
				 yAxis = r.nextBoolean();
			 }
			 
			 //look at the player
			 this.angle = MathUtils.radiansToDegrees * MathUtils.atan2(game.p.centerY - this.centerY, game.p.centerX - this.centerX);
			 
			 timeSinceLastBullet += delta;
			 if(timeSinceLastBullet > 2.0f){
				 timeSinceLastBullet = 0;
				 //shoot at player
				 game.entities.add(new Bullet(game, centerX - 9, centerY - 9, 9, 9, 120.0f, new Texture("orcbullet.png"), new Vector3(game.p.centerX, game.p.centerY, 0), false, damageOnCollision));
			 }
			if(yAxis) {
				dx = 0;
				if(!goingDownAxis){
					dy = speed * delta;
				}else{
					dy = -speed * delta;
				}
					
			}else{
				dy = 0;
				if(!goingDownAxis){
					dx = speed * delta;
				}else{
					dx = -speed * delta;
				}
			}
			 return;
		}
		playerInRange = false;
		
		
	}
	
	
	
	public void tileCollision(int tile, int tileX, int tileY, float newX, float newY, Direction direction) {
		super.tileCollision(tile, tileX, tileY, newX, newY, direction);
		yAxis = r.nextBoolean();
		goingDownAxis = r.nextBoolean();
		
	}
	
	

}
