package com.ardtaksh.game;

import com.ardtaksh.game.ArdTakshGame.Direction;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class CircularEntity extends Entity {
	public float centerX;
	public float centerY;
	public float radius;
	public float angle;
	public Sprite sprite;
	public int tileCenterX;
	public int tileCenterY;
	
	public CircularEntity(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture) {
		super( game, x, y, width, height, speed, texture);
		move(x, y);
		radius = (float)width/2;
		this.angle = 0;
		this.sprite = new Sprite(texture);
	}
	
	public void move(float newX, float newY){
		super.move(newX, newY);
		centerX = x + (float)width/2;
		centerY = y + (float)height/2;
		tileCenterX = (int)(centerX/32);
		tileCenterY = (int)(centerY/32);
	}
	
	public void tileCollision(int tile, int tileX, int tileY, float newX, float newY, Direction direction) {
		
		if(direction == Direction.U) {
			y = tileY * game.tileSize + game.tileSize;
			centerY = y + (float)height/2;
		}
		else if(direction == Direction.D) {
			y = tileY * game.tileSize - height;
			centerY = y + (float)height/2;
		}
		else if(direction == Direction.L) {
			x = tileX * game.tileSize + game.tileSize;
			centerX = x + (float)width/2;
		}
		else if(direction == Direction.R) {
			x = tileX * game.tileSize - width;
			centerX = x + (float)width/2;
		}		
	}
}
