package com.ardtaksh.game;

import com.ardtaksh.game.ArdTakshGame.Direction;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Creature extends CircularEntity{
	public int hitpoints;
	boolean tint;
	public float tintTime;
	public float timeSinceTint;
	public Creature(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture) {
		super(game, x, y, width, height, speed, texture);
		tintTime = 0.2f;
		// TODO Auto-generated constructor stub
	}
	
	public boolean isHit(Entity e2){
		return true;
	}
	
	public void onNoHitPoints(){
		
	}
	
	public void subtractHitpointsFromHit(Entity e2){
	}
	
	
	public void update(float delta){
		if(tint){
			
			timeSinceTint += delta;
			if(timeSinceTint > tintTime){
				tint = false;
				timeSinceTint = 0;
				sprite.setColor(Color.WHITE);
			}
		}
		
	}
	
	
	public void entityCollision(Entity e2, float newX, float newY, Direction direction) {
		super.entityCollision(e2, newX, newY, direction);
		if(isHit(e2)){
			tint = true;
			sprite.setColor(Color.RED);
			subtractHitpointsFromHit(e2);
			if(e2 instanceof Bullet){
				game.entities.remove(e2);
			}
			if(hitpoints < 1){
				onNoHitPoints();
			}
		}
	}
	

}
