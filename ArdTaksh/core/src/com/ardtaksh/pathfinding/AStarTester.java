package com.ardtaksh.pathfinding;

import com.ardtaksh.mapgeneration.RandomCaveMap;

public class AStarTester {
	
	public static void main(String[] args){
		RandomCaveMap m = new RandomCaveMap(20, 20);
		int[][] map = m.generateMap(10);
		int[] coords1 = m.placeObjectRandomly();
		int[] coords2 = m.placeObjectRandomly();
		AStar pathfinder = new AStar(map);
		PathNode node = pathfinder.findPath(coords1[1], coords1[0], coords2[1], coords2[0]);
		while(node.successor != null){
		    map[node.successor.x][node.successor.y] = 3;
		    node = node.successor;
		}
		map[node.x][node.y] = 2;
		for(int i = 0; i < 20; i++){
			for(int y = 0; y < 20; y++){
				if(map[i][y] != 1){
					System.out.print(map[i][y] + " ");
				}else{
					System.out.print("# ");
				}
				
			}
			System.out.println();
		}
	}
	
}
