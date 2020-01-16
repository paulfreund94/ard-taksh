package com.ardtaksh.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.ardtaksh.mapgeneration.MapToTiledMap;
import com.ardtaksh.mapgeneration.RandomCaveMap;
import com.ardtaksh.pathfinding.AStar;
import com.ardtaksh.pathfinding.PathNode;

public class ArdTakshGame extends ApplicationAdapter {
	SpriteBatch batch;
	AStar pathfinder;
	int screenWidth;
	int screenHeight;
	Texture bootTexture;
	boolean playerHasDash;
	boolean playerChangedTile;
    
	// 1 = block
	// 0 = empty
	// the x and y coordinate system is not what it seems
	// visually x goes down and y across
	// this will make more sense when you compare it to what is drawn
	int[][] map;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	int mapWidth = 30;
	int mapHeight = 30;
	int tileSize = 32;
	Texture rocks;
	Texture ground;
	ScorePickUp chest;
	OrthographicCamera camera;
	OrthographicCamera UICamera;
	Viewport view;
	SpriteBatch UIBatch;
	Player p;
	BitmapFont font;
	int score;
	boolean gameOver;
	int levelNumber;
	
	ArrayList<Entity> entities = new ArrayList<Entity>();
	
	enum Axis { X, Y };
	enum Direction { U, D, L, R };

  @Override
  public void create () {
	 
	  batch = new SpriteBatch();
	  
	  
	  ground = new Texture(Gdx.files.internal("ground.png"));

	  rocks = new Texture(Gdx.files.internal("rocks.png")); 
	  
	  screenWidth = Gdx.graphics.getWidth();
	  screenHeight = Gdx.graphics.getHeight();
	 
	  score = 0;
	  float w = Gdx.graphics.getWidth();
	  float h = Gdx.graphics.getHeight();
	  camera = new OrthographicCamera();
	  UIBatch = new SpriteBatch();
      camera.setToOrtho(false,480,480);
      camera.update();
      nextLevel();
      UICamera = new OrthographicCamera();
      UICamera.setToOrtho(false, 480, 480);

      font = new BitmapFont(Gdx.files.internal("pixel.fnt"), Gdx.files.internal("pixel.png"), false);
      bootTexture = new Texture("boot.png");
    
	 	  
	  // add some entities including a player
  }
  
  public void nextLevel(){
	  entities.clear();
	  RandomCaveMap r = new RandomCaveMap(mapWidth, mapHeight);
	  map = r.generateMap(12);
	  pathfinder = new AStar(map);
	  int[] playerCoords = r.placeObjectRandomly();
      p = new Player(this, (float)tileSize*playerCoords[0], (float)tileSize*playerCoords[1], 32, 32, 150.0f, new Texture("player.png"));
      int[] ladderCoords = r.placeObjectRandomly();
      Ladder l = new Ladder(this, (float)tileSize*ladderCoords[0], (float)tileSize*ladderCoords[1], 32, 32, 120.0f, new Texture("ladder.png"));
      entities.add(p);
      entities.add(l);
      
     
      int[] orcCoords = r.placeObjectRandomly();
      
      // place enemies
      
      for(int i = 0; i < 3 + levelNumber; i++){
    	  Orc c1 = new Orc(this, (float)tileSize*orcCoords[0], (float)tileSize*orcCoords[1], 32, 32, 80.0f, new Texture("orc.png"));
    	  orcCoords = r.placeObjectRandomly();
    	  entities.add(c1);
      }
      
      for(int i = 0; i < 3 + levelNumber; i++){
    	  Spider c = new Spider(this, (float)tileSize*orcCoords[0], (float)tileSize*orcCoords[1], 32, 32, 30.0f, new Texture("spider.png"));
    	  orcCoords = r.placeObjectRandomly();
    	  entities.add(c);
      }
      
      for(int i = 0; i < 3 + levelNumber; i++){
    	   Rat c = new Rat(this, (float)tileSize*orcCoords[0], (float)tileSize*orcCoords[1], 32, 32, 80.0f, new Texture("rat.png"));
    	   orcCoords = r.placeObjectRandomly();
    	   entities.add(c);
      }
   
      
      
      orcCoords = r.placeObjectRandomly();
      chest = new ScorePickUp(this,  (float)tileSize*orcCoords[0], (float)tileSize*orcCoords[1], 24,24, 80.0f, new Texture("chest.png"), 5);
      entities.add(chest);
      
      // get tiledMap
	  tiledMap = MapToTiledMap.tileCave(ground, rocks, map);
	  tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
	  playerHasDash = true;
  }
  
  public void moveEntity(Entity e, float newX, float newY) {
	  // just check x collisions keep y the same
	  moveEntityInAxis(e, Axis.X, newX, e.y);
	  // just check y collisions keep x the same
	  moveEntityInAxis(e, Axis.Y, e.x, newY);
  }
  
  public void moveEntityInAxis(Entity e, Axis axis, float newX, float newY) {
	  Direction direction;
	  
	  // determine axis direction
	  if(axis == Axis.Y) {
		  if(newY - e.y < 0) direction = Direction.U;
		  else direction = Direction.D;
	  }
	  else {
		  if(newX - e.x < 0) direction = Direction.L;
		  else direction = Direction.R;
	  }

	  if(!tileCollision(e, direction, newX, newY) && !entityCollision(e, direction, newX, newY)) {
		  // full move with no collision
		  
		  e.move(newX, newY);
	  }
	  // else collision with wither tile or entity occurred 
  }
  
  public boolean tileCollision(Entity e, Direction direction, float newX, float newY) {
	  boolean collision = false;

	  // determine affected tiles
	  int x1 = (int) Math.floor(Math.min(e.x, newX) / tileSize);
	  int y1 = (int) Math.floor(Math.min(e.y, newY) / tileSize);
	  int x2 = (int) Math.floor((Math.max(e.x, newX) + e.width - 0.1f) / tileSize);
	  int y2 = (int) Math.floor((Math.max(e.y, newY) + e.height - 0.1f) / tileSize);
	  
	  // todo: add boundary checks...

	  // tile checks
	  for(int x = x1; x <= x2; x++) {
		  for(int y = y1; y <= y2; y++) {
			  if(map[y][x] == 1) {
				  if(e instanceof CircularEntity){
					  // do circle aligned entity collision
					  CircularEntity c = (CircularEntity) e;
					  float deltaX = newX + (float)c.width/2 - Math.max(newX + (float)c.width/2, Math.min(newX + (float)c.width/2, tileSize * x + tileSize));
					  float deltaY = newY + (float)c.height/2 - Math.max(newY + (float)c.height/2, Math.min(newY + (float)c.height/2, tileSize * y + tileSize));
					  if((deltaX* deltaX + deltaY*deltaY) < (c.radius * c.radius)){
						  collision = true;
						  e.tileCollision(map[y][x], x, y, newX, newY, direction);
					  }
					  
				  } else{
					  collision = true;
					  e.tileCollision(map[y][x], x, y, newX, newY, direction);
				  }
				 
			  }
		  }
	  }
	  
	  return collision;
  }
  
  public boolean entityCollision(Entity e1, Direction direction, float newX, float newY) {
	  boolean collision = false;
	  
	  
	  for(int i = 0; i < entities.size(); i++) {
		  Entity e2 = entities.get(i);
		  
		  // we don't want to check for collisions between the same entity
		  if(e1 != e2) {
			  
			  if(e1 instanceof CircularEntity && e2 instanceof CircularEntity){
				  // circle-circle collision detection
				  CircularEntity c1 = (CircularEntity)e1;
				  CircularEntity c2 = (CircularEntity)e2;
				  float dx = c1.centerX - c2.centerX;
				  float dy = c1.centerY - c2.centerY;
				  float distsq = dx * dx + dy * dy;
				  if(distsq < (c1.radius + c2.radius) * (c1.radius + c2.radius)){
					  collision = true;
					  e1.entityCollision(e2, newX, newY, direction);
				  }
			  }else if(e1 instanceof CircularEntity){
				  // circle-rectangle collision detection
				  CircularEntity c = (CircularEntity)e1;
				  float deltaX = newX + (float)c.width/2 - Math.max(e2.x, Math.min(newX + (float)c.width/2, e2.x + e2.width));
				  float deltaY = newY + (float)c.height/2 - Math.max(e2.y, Math.min(newY + (float)c.height/2, e2.y + e2.height));
				  if((deltaX* deltaX + deltaY*deltaY) < (c.radius * c.radius)){
					  collision = true;
					  e1.entityCollision(e2, newX, newY, direction);
				  }
				  
				// axis aligned rectangle rectangle collision detection
			  }else if(newX < e2.x + e2.width && e2.x < newX + e1.width &&
				  newY < e2.y + e2.height && e2.y < newY + e1.height) {
				  collision = true;
				  
				  e1.entityCollision(e2, newX, newY, direction);
			  }
		  }
	  }
	  
	  return collision;
  }

  @Override
  public void render () {
	  
	  // update
	  // ---
	  
	  if(!gameOver){
		  float delta = Gdx.graphics.getDeltaTime();
		  
		  // update all entities
		  for(int i = entities.size() - 1; i >= 0; i--) {
			  Entity e = entities.get(i);
			  // update entity based on input/ai/physics etc
			  // this is where we determine the change in position
			  e.update(delta);
			  // now we try move the entity on the map and check for collisions
			  moveEntity(e, e.x + e.dx, e.y + e.dy);
		  }	
		  
	  }else{
		  if(Gdx.input.isKeyPressed(Keys.Y)){
			  // restart
			  score = 0;
			  gameOver = false;
			  levelNumber = 0;
			  nextLevel();
		  }else if(Gdx.input.isKeyPressed(Keys.N)){
			  Gdx.app.exit();
		  }
	  }
	  
	  
	  
	  // draw
	  // ---
	  
	  // to offset where your map and entities are drawn change the viewport
	  // see libgdx documentation
	  
	  Gdx.gl.glClearColor(0, 0, 0, 1);
	  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	  
	  UICamera.update();
	  camera.position.set(p.x, p.y, 0);
	  camera.update();
	  
	  

	  // draw tile map
	  tiledMapRenderer.setView(camera);
      tiledMapRenderer.render();
	  batch.setProjectionMatrix(camera.combined);
	  batch.begin();
    
	  // draw all entities
	  
	  for(int i = entities.size() - 1; i >= 0; i--) {
		  Entity e = entities.get(i);
		  if(e instanceof CircularEntity){
			  CircularEntity c = (CircularEntity) e;
			  Sprite s = c.sprite;
			  s.setX(c.x);
			  s.setY(c.y);
			  s.setRotation(c.angle);
			  s.draw(batch);
		  }else{
			  batch.draw(e.texture, e.x, e.y);
		  }
		  
	  }
	  
	  batch.end();
	  
	  // draw UI Sprite Stuff
	  UIBatch.setProjectionMatrix(UICamera.combined);
	  UIBatch.begin();
	  if(!gameOver){
		  font.draw(UIBatch, "score: " + score, 10, 470);
		  if(playerHasDash){
			  UIBatch.draw(bootTexture, 390, 440);
		  }
	  }else{
		  font.draw(UIBatch, "GAME OVER", 185, 340);
		  font.draw(UIBatch, "FINAL SCORE: ", 180, 300);
		  font.draw(UIBatch, Integer.toString(score), 225, 270);
		  font.draw(UIBatch, "RESTART? Y/N", 170, 230);
		  
	  }
	  
	  UIBatch.end();

	  //draw health bar
	  ShapeRenderer shapeRenderer = new ShapeRenderer();
	  shapeRenderer.setProjectionMatrix(UICamera.combined);
	  shapeRenderer.begin(ShapeType.Filled);
	  shapeRenderer.setColor(0.9f, 0.01f, 0, 1);
	  float healthBarWidth = (float)p.hitpoints/p.maxHitpoints*80f;
	  if(healthBarWidth < 0){
		  healthBarWidth = 0;
	  }
	  shapeRenderer.rect(390, 460, healthBarWidth, 10);
	  shapeRenderer.setProjectionMatrix(camera.combined);
	  shapeRenderer.end();
	  
  }
  
  public void drawPath(ShapeRenderer renderer, PathNode node){
	  if(node != null){
		  while(node.successor != null){
			  renderer.line(node.y * tileSize + 16, node.x * tileSize + 16, node.successor.y * tileSize + 16, node.successor.x * tileSize + 16);
			  node = node.successor;
		  }  
	  }
	  
	  
  }
}
