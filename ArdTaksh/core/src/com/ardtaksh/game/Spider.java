package com.ardtaksh.game;

import java.util.Random;

import com.ardtaksh.game.ArdTakshGame.Direction;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Spider extends Enemy{
	float timeSinceLastBullets;
	boolean playerInRange;
	boolean goingLeft;
	boolean goingDown;
	Random r;
	public Spider(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture) {
		super(game, x, y, width, height, speed, texture);
		this.hitpoints = 3;
		r = new Random();
		timeSinceLastBullets = r.nextFloat()*2.0f;
		damageOnCollision = 1;
		
	}
	
	public void update(float delta){
		super.update(delta);
		if((game.p.centerX - this.centerX)*(game.p.centerX - this.centerX) + (game.p.centerY - this.centerY)*(game.p.centerY - this.centerY) < 320*320){
			 if(playerInRange = false){
				 playerInRange = true;
				 goingLeft = r.nextBoolean();
				 goingDown = r.nextBoolean();
			 }
			
			 timeSinceLastBullets += delta;
			 
			 
			 
			if(!goingDown) {
					dy = speed * delta;
			}
			if(goingDown) {
				dy = -speed * delta;
			}
			if(goingLeft) {
				dx = -speed * delta;
			}
			if(!goingLeft) {
				dx = speed * delta;
			}
			
			if(timeSinceLastBullets > 2.0f){
				 timeSinceLastBullets = 0;
				// shoot bullets
				 if(r.nextBoolean()){
					 
					 game.entities.add(new Bullet(game, centerX - 11, centerY - 11, 11, 11, 50.0f, new Texture("spiderbullet.png"), new Vector3(this.centerX, (game.mapHeight -1) * game.tileSize + 31, 0), false, 2));
					 game.entities.add(new Bullet(game, centerX - 11, centerY - 11, 11, 11, 50.0f, new Texture("spiderbullet.png"), new Vector3(this.centerX, 0, 0), false, 2)); 
				 }else{
					 game.entities.add(new Bullet(game, centerX - 11, centerY - 11, 11, 11, 50.0f, new Texture("spiderbullet.png"), new Vector3(0, this.centerY, 0), false,2));
					 game.entities.add(new Bullet(game, centerX - 11, centerY - 11, 11, 11, 50.0f, new Texture("spiderbullet.png"), new Vector3((game.mapWidth -1) * game.tileSize + 31, this.centerY, 0), false, 2));
				 }
				 
				
			 }
			
			 
			 this.angle = MathUtils.radiansToDegrees * MathUtils.atan2(this.centerY + dy/delta * 1200, this.centerX + dx/delta * 1200);
			 return;
		}
		playerInRange = false;
		
		
	}
	
	public void tileCollision(int tile, int tileX, int tileY, float newX, float newY, Direction direction) {
		super.tileCollision(tile, tileX, tileY, newX, newY, direction);
		goingLeft = r.nextBoolean();
		goingDown = r.nextBoolean();
		
	}
	
	

}
