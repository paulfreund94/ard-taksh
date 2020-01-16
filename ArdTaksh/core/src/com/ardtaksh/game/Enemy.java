package com.ardtaksh.game;

import java.util.Random;

import com.ardtaksh.game.ArdTakshGame.Direction;
import com.ardtaksh.pathfinding.PathNode;
import com.badlogic.gdx.graphics.Texture;

public class Enemy extends Creature {
	public int damageOnCollision;
	public PathNode p;
	public PathNode s;
	public float delta;
	public Enemy(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture) {
		super(game, x, y, width, height, speed, texture);
		// TODO Auto-generated constructor stub
	}

	
	
	
	
	public boolean isHit(Entity e2){
		if(e2 instanceof Bullet){
			return ((Bullet)e2).playerBullet;
		}
		return false;
	}
	
	public void subtractHitpointsFromHit(Entity e2){
		hitpoints -= ((Bullet)e2).strength;
	}
	
	public void tileCollision(int tile, int tileX, int tileY, float newX, float newY, Direction direction) {
		super.tileCollision(tile, tileX, tileY, newX, newY, direction);
		if(direction == Direction.U || direction == Direction.D){
			if(newX > this.x){
				this.move(newX + delta * speed, newY);
			}else if(newX < this.x){
				this.move(newX - delta * speed, newY);
			}
		}else if(direction == Direction.L || direction == Direction.R){
			if(newY > this.y){
				this.move(newX + delta * speed, newY);
			}else if(newY < this.y){
				this.move(newX - delta * speed, newY);
			}
		}
	}
	
	public void update(float delta){
		
	}
	
	public void onNoHitPoints(){
		if(this.hitpoints <= 0){
			int value = (new Random()).nextInt(100);
			if(value < 20){
				game.entities.add(new HealthPickUp(game, centerX, centerY, 16, 16, speed, new Texture("health.png")));
			}else if(value < 50){
				game.entities.add(new ScorePickUp(game, centerX, centerY, 16, 16, speed, new Texture("coin.png"), 1));
			}
			this.texture.dispose();
			game.entities.remove(this);
		}
	}

}
