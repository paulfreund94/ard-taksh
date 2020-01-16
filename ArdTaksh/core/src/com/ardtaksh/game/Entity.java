package com.ardtaksh.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.ardtaksh.game.ArdTakshGame.Direction;
import com.ardtaksh.pathfinding.PathNode;

public class Entity {
	public ArdTakshGame game;
	public float x;
	public float y;
	
	public float dx;
	public float dy;
	public int width;
	public int height;  
	public float speed;
	public Texture texture;
	
	public Entity(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture) {
		this.game = game;
		move(x, y);
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.texture = texture;
	}

	public void update(float delta) {

	}
	
	public void move(float newX, float newY) {
		x = newX;
		y = newY;
	}
	
	public void render() {
		
	}

	public void tileCollision(int tile, int tileX, int tileY, float newX, float newY, Direction direction) {
		System.out.println("tile collision at: " + tileX + " " + tileY);
		
		if(direction == Direction.U) {
			y = tileY * game.tileSize + game.tileSize;
		}
		else if(direction == Direction.D) {
			y = tileY * game.tileSize - height;
		}
		else if(direction == Direction.L) {
			x = tileX * game.tileSize + game.tileSize;
		}
		else if(direction == Direction.R) {
			x = tileX * game.tileSize - width;
		}		
	}

	public void entityCollision(Entity e2, float newX, float newY, Direction direction) {
		System.out.println(e2.getClass() + " " + this.getClass());
		
		move(newX, newY);
		// could also resolve entity collisions in the same we do tile collision resolution
	}
}

