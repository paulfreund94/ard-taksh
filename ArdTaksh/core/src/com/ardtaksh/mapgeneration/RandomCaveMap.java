package com.ardtaksh.mapgeneration;

import java.util.ArrayList;
import java.util.Random;

/**
 * Implementation of algorithm found at the following link:
 * http://www.roguebasin.com/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels
 * @author Paul
 *
 */
public class RandomCaveMap {
	private int[][] map;
	public final int width;
	public final int height;
	Random r;
	
	public RandomCaveMap(int width, int height){
		this.width = width;
		this.height = height;
		map = new int[height][width];
	}
	
	
	
	public int[][] generateMap(int numberOfIterations){
		seedMap();
		for(int i = 0; i < numberOfIterations; i++){
			if(i < numberOfIterations - 3){
				nextMapIteration(false);
				
			}else{
				nextMapIteration(true);
			}
		}
		
		ArrayList<Integer> floodFillNumbers = new ArrayList<Integer>();
		
		int floodFillNumber = 1;
		for(int y = 0; y < map.length; y++){
			for(int x =0; x < map[0].length; x++){
				if(map[y][x] == 0){
					floodFillNumber++;
					floodFillNumbers.add(floodFillNumber);
					floodFill(map, x, y, floodFillNumber, 0);
				}
			}
		}
		
		
		Integer[] floodFillNumbersArr = new Integer[floodFillNumbers.size()];
		floodFillNumbers.toArray(floodFillNumbersArr);
		
		int maxNo = 0;
		int largestFloodFill = 0;
		for(int e : floodFillNumbersArr){
			int count = 0;
			for(int y = 0; y < map.length; y++){
				for(int x =0; x < map[0].length; x++){
					if(map[y][x] == e){
						count++;
					}
				}
			}
			if(count > maxNo){
				maxNo = count;
				largestFloodFill = e;
			}
		}
		printMap();
		for(int y = 0; y < map.length; y++){
			for(int x =0; x < map[0].length; x++){
				if(!(map[y][x] == largestFloodFill || map[y][x] == 1)){
					floodFill(map, x, y, 1, map[y][x]);
				}
			}
		}
		
		for(int y = 0; y < map.length; y++){
			for(int x =0; x < map[0].length; x++){
				if(map[y][x] != 1){
					map[y][x] = 0;
				}
			}
		}
		
		return map;
	}
	
	public void floodFill(int[][] map, int x, int y, int floodFillNumber, int targetNumber){
		if(map[y][x] != targetNumber || x < 0 || x >= map[0].length || y >= map.length || y <0){
			return;
		}
		if(floodFillNumber == targetNumber){
			return;
		}
		map[y][x] = floodFillNumber;

		floodFill(map, x-1, y, floodFillNumber, targetNumber);
		floodFill(map, x+1, y, floodFillNumber, targetNumber);
		floodFill(map, x, y+1, floodFillNumber, targetNumber);
		floodFill(map, x, y-1, floodFillNumber, targetNumber);
		
	}
	
	public void printMap(){
		for(int i = 0; i < height; i++){
			for(int z = 0; z < width; z++){
				if(map[i][z] == 1){
					System.out.print("#");
				}else if (map[i][z] == 0){
					System.out.print(".");
				}else{
					System.out.print(map[i][z]);
				}
				
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public int[] placeObjectRandomly(){
		r = new Random();
		int y = r.nextInt(height);
		int x = r.nextInt(width);
		int[] returnArr = new int[2];
		
		
		// try all points to the right and down of x, y
		for(int i = y; i < height; i++){
			for(int z = x; z < width; z++){
				if(map[i][z] == 0){
					return placeObject(i, z, returnArr);
				}
			}
		}
		
		//left and down
		for(int i = y; i < height; i++){
			for(int z = x - 1; z >= 0; z--){
				if(map[i][z] == 0){
					return placeObject(i, z, returnArr);
				}
			}
		}
		
		// up and right
		for(int i = y - 1; i >= 0; i--){
			for(int z = x; z < width; z++){
				if(map[i][z] == 0){
					return placeObject(i, z, returnArr);
				}
			}
		}
		
		// up and left
		for(int i = y - 1; i >= 0; i--){
			for(int z = x - 1; z >= 0; z--){
				if(map[i][z] == 0){
					return placeObject(i, z, returnArr);
				}
			}
		}
		
		System.out.println("whoops");
		return null;
	}
	
	private int[] placeObject(int i, int z, int[] returnArr){
		map[i][z] = 2;
		returnArr[0] = z;
		returnArr[1] = i;
		return returnArr;
	}
	
	public void seedMap(){
		r = new Random();
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(x == 0 || y == 0 || x == width -1 || y == height - 1){
					map[y][x] = 1;
				}else if (r.nextInt(100) < 39){
					map[y][x] = 1;
				}	
			}
		}
	}
	
	
	private void nextMapIteration(boolean phase2){
		int[][] tempMap = new int[height][width];
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(x == 0 || y ==0 || y == height - 1 || x == width -1){
					tempMap[y][x] = 1;
					continue;
				}
				if(numberOf1sNTilesAway(x, y, 1) >= 5 || !phase2 && numberOf1sNTilesAway(x, y, 2) <= 2){
					tempMap[y][x] = 1;
				}
			}
		}
		map = tempMap;
	}
	
	private int numberOf1sNTilesAway(int x, int y, int n){
		int leftMostColumn = x - n;
		int rightMostColumn = x + n;
		int upMostRow = y + n;
		int downMostRow = y - n;
		
		int count = 0;
		for(int row = downMostRow; row <= upMostRow; row++){
			if(row < 0 || row > height - 1){
				continue;
			}
			for(int col = leftMostColumn; col <= rightMostColumn; col++){
				if(col < 0 || col > width - 1){
					continue;
				}
				if(map[row][col] == 1){
					count++;
				}
			}
		}
		return count;
	}
	
}
