package com.ardtaksh.mapgeneration;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class MapToTiledMap {
	
	
	public static TiledMap tileCave (Texture ground, Texture rocks, int[][] map){
		  TiledMap tiledMap = new TiledMap();
		  MapLayers layers = tiledMap.getLayers();
			
		  TiledMapTileLayer layer1 = new TiledMapTileLayer(map.length, map[0].length, 32, 32);
		  StaticTiledMapTile floor1 = new StaticTiledMapTile(new TextureRegion(ground, 0, 0, 32, 32 ));
		  StaticTiledMapTile floor2 = new StaticTiledMapTile(new TextureRegion(ground, 32, 0, 32, 32));
		  Random ra = new Random();
		  for(int i = 0; i < map.length; i++){
			for(int z = 0; z < map[0].length; z++){
				Cell cell = new Cell();
				if(map[i][z] == 1){
						cell.setTile(getTile(i, z, map, rocks));
				}else if(ra.nextBoolean() == true){
					cell.setTile(floor1);
				}else{
					cell.setTile(floor2);
				}
				layer1.setCell(z, i, cell);
			}
		  }
		  layers.add(layer1);
		  return tiledMap;
	}
	
	private static StaticTiledMapTile getTile(int y, int x, int[][] map, Texture rocks){
		boolean rightGround = isGround(y, x + 1, map);
		boolean leftGround = isGround(y, x - 1, map);
		boolean upGround = isGround(y + 1, x, map);
		boolean downGround = isGround(y - 1, x, map);
		int numAdjZeros = getNumberOfZerosOneTileAway(y, x, map);
		
		return getTile(rocks, rightGround, leftGround, upGround, downGround, numAdjZeros);
	}
	
	private static boolean isGround(int y, int x, int[][]map){
		if(y >= map.length || y < 0 || x >= map[0].length || x < 0){
			return false;
		}
		if(map[y][x] == 0){
			return true;
		}
		return false;
	}
	
	private static int getNumberOfZerosOneTileAway(int y, int x, int[][] map){
		int count = 0;
		for(int row = y-1; row <= y+1; row++){
			if(row < 0 || row > map.length - 1){
				continue;
			}
			if(map[row][x] == 0){
				count++;
			}
		}
		for(int col = x-1; col <= x+1; col++){
				if(col < 0 || col > map[0].length - 1){
					continue;
				}
				if(map[y][col] == 0){
					count++;
				}
		}
		return count;
	}
	
	private static StaticTiledMapTile getTile(Texture rocks, boolean rightGround, boolean leftGround, boolean upGround, boolean downGround, int numberOfAdjacent0s){
		
		if(numberOfAdjacent0s == 4){
			// only one possibility
			return new StaticTiledMapTile( new TextureRegion(rocks, 32, 0, 32, 32 ));
		}
		if(numberOfAdjacent0s == 0){
			// same
			return new StaticTiledMapTile(new TextureRegion(rocks, 0, 32, 32, 32 ));
		}
		if(numberOfAdjacent0s == 2){
			if(rightGround && leftGround || upGround && downGround){
				float[] coords = {32f, 32f, 63f, 63f};
				if(upGround){
					return new StaticTiledMapTile(new TextureRegion(rocks, 32, 32, 32, 32));
				}else{
					return new StaticTiledMapTile(new TextureRegion(rocks, 128, 0, 32, 32));
				}
				
			}else{
				TextureRegion t = new TextureRegion(rocks, 64, 32, 32, 32);
				if(rightGround){
					t.flip(true, false);
				}
				if(downGround){
					t.flip(false, true);
				}
				return new StaticTiledMapTile(t);
			}
		}

		// either one adjacent ground tile or three
	    boolean oneAdjacentGround = (numberOfAdjacent0s == 1);
	    int[] coords;
	    int[] oneCoords;
	    int[] threeCoords;
	
	    if((rightGround || leftGround) && oneAdjacentGround || (!rightGround || !leftGround) && !oneAdjacentGround){
	    	int[] a = {96,0,32,32};
	    	int[] b = {96,32,32,32};
	    	oneCoords = a;
	    	threeCoords = b;
	    }else{
	    	int[] a = {0, 0, 32, 32};
	    	int[] b = {64, 0, 32, 32};
	    	oneCoords =a;
	    	threeCoords = b;
	    }
	    
	    coords = oneAdjacentGround ? oneCoords : threeCoords;
	    TextureRegion t = new TextureRegion(rocks, coords[0], coords[1], coords[2], coords[3]);
	    if(leftGround && oneAdjacentGround|| !leftGround && !oneAdjacentGround){
	    		t.flip(true, false);
	    }
	    if(downGround && oneAdjacentGround || !downGround && !oneAdjacentGround){
	    	t.flip(false, true);
	    }
	    return new StaticTiledMapTile(t);
	    
	}
	

	

}
