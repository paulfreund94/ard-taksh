package com.ardtaksh.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.ardtaksh.game.CircularEntity;

public class Player extends Creature {
	
	public int maxHitpoints;
    public boolean dashing;
    public float timeSpendDashing;
    public float timeSinceLastDash;
    public float timeSinceLastBullet;
    public float bulletShootRate;
    boolean invulnerable;
    public float timeSinceInvulnerable;
	public Player(ArdTakshGame game, float x, float y, int width, int height, float speed, Texture texture) {
		super(game, x, y, width, height, speed, texture);
		this.radius = 14;
		this.hitpoints = 10;
		this.maxHitpoints = 10;
		this.dashing = false;
		this.timeSinceLastDash = 2.1f;
		bulletShootRate = 0.3f;
		this.timeSinceLastBullet = bulletShootRate;
		game.playerChangedTile = true;
	}

	public boolean isHit(Entity e2){
		if(e2 instanceof Bullet){
			return !((Bullet)e2).playerBullet;
		}else if(e2 instanceof Enemy){
			return true;
		}
		return false;
	}
	
	public void onNoHitPoints(){
		game.gameOver = true;
	}
	
	
	public void subtractHitpointsFromHit(Entity e2){
		if(!invulnerable){
			if(e2 instanceof Bullet){
				hitpoints -= ((Bullet)e2).strength;
			}else if(e2 instanceof Enemy){
				hitpoints -= ((Enemy)e2).damageOnCollision;
			}
			invulnerable = true;
		}
		
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		if(invulnerable){
			timeSinceInvulnerable += delta;
			if(timeSinceInvulnerable > tintTime){
				invulnerable = false;
				timeSinceInvulnerable = 0;
			}
		}

		dx = 0;
		dy = 0;
		
		// angle to face mouse
		Vector3 position = game.camera.project(new Vector3(centerX, centerY, (float)0));
		float xInput = Gdx.input.getX();
	    float yInput = (Gdx.graphics.getHeight() - Gdx.input.getY());
	    this.angle = MathUtils.radiansToDegrees * MathUtils.atan2(yInput - position.y, xInput - position.x);


		
	    if(!dashing){
	    	timeSinceLastDash += delta;
	    }
	    if(timeSinceLastDash > 2.0f){
	    	game.playerHasDash = true;
	    }
	    if(Gdx.input.isKeyPressed(Keys.SPACE) && !dashing && game.playerHasDash){
	    	game.playerHasDash = false;
	    	dashing = true;
	    	this.speed = 480.0f;
	    	timeSinceLastDash = 0;
	    }
	    if(dashing){
	    	timeSpendDashing += delta;
	    	if(timeSpendDashing > 0.2f){
	    		dashing = false;
	    		this.speed = 150.0f;
	    		timeSpendDashing = 0;
	    	}
	    }
	    
	    // move
		if(Gdx.input.isKeyPressed(Keys.W)) {
			dy = speed * delta;
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			dy = -speed * delta;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			dx = -speed * delta;
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			dx = speed * delta;
		}
		if(timeSinceLastBullet < bulletShootRate){
			timeSinceLastBullet+=delta;
		}
		if(Gdx.input.justTouched() && timeSinceLastBullet >= bulletShootRate){
			// shoot bullet
			timeSinceLastBullet = 0;
			Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			game.entities.add(new Bullet(game, centerX - 9, centerY - 9, 9, 9, 240.0f, new Texture("bullet.png"), game.camera.unproject(mousePosition), true, 1));
		}
		
		
		
	}
	
}
