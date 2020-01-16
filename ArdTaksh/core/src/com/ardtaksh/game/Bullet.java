package com.ardtaksh.game;

import com.ardtaksh.game.ArdTakshGame.Direction;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Bullet extends CircularEntity{
	Vector2 velocity;
	Vector2 movement;
	boolean playerBullet;
	int strength;

	public Bullet(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture, Vector3 targetPosition, boolean bullet, int strength) {
		super(game, x, y, width, height, speed, texture);
		
		//get line on which bullet will travel
		Vector2 position = new Vector2(targetPosition.x, targetPosition.y);
		Vector2 direction = new Vector2();
		direction.set(position).sub(new Vector2(this.x, this.y)).nor();
		velocity = new Vector2(direction).scl(speed);
		movement = new Vector2();
		movement.set(velocity).scl((float) 0.035);
		this.x += movement.x;
		this.y += movement.y;
		this.playerBullet = bullet;
		this.strength = strength;
		
	}
	
	public void update(float delta) {
		movement.set(velocity).scl(delta);
		this.x += movement.x;
		this.y += movement.y;
	}
	
	public void tileCollision(int tile, int tileX, int tileY, float newX, float newY, Direction direction) {
		game.entities.remove(this);
		sprite.getTexture().dispose();
	}
	
	

}
