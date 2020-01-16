package com.ardtaksh.pathfinding;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class AStar {
	private int[][] map;
	
	
	public AStar(int[][] map){
		this.map = map;
	}
	
	public PathNode findPath(int startX, int startY, int goalX, int goalY){
		PriorityQueue<Node> openList = new PriorityQueue<Node>();
		Set<Node> closedList = new HashSet<Node>();
		Node startNode = new Node(startX, startY);
		openList.add(startNode);
		int iterations = 0;
		boolean success = false;
		Node currentNode = startNode;
		while(!openList.isEmpty()){
			iterations++;
			currentNode = openList.peek();
			if(currentNode.x == goalX && currentNode.y == goalY){
				success = true;
				break;
			}
			addAdjacentNodes(currentNode, openList, closedList, goalX, goalY, iterations);
			closedList.add(currentNode);
			openList.poll();
		}
		PathNode successor = new PathNode(currentNode.x, currentNode.y);
		if(success){
			
			
			while(currentNode.predecessor != null){
				PathNode predecessor = new PathNode(currentNode.predecessor.x, currentNode.predecessor.y);
				predecessor.successor = successor;
				successor = predecessor;
				currentNode = currentNode.predecessor;
			}
			return successor;
			
		}else{
			successor = new PathNode(startX, startY);
		}
		PathNode node = successor;
		return successor;
	}
	
	public void addAdjacentNodes(Node currentNode, PriorityQueue<Node> openList, Set<Node> closedList, int goalX, int goalY, int iterations){
		int x = currentNode.x;
		int y = currentNode.y;
	
		for(int row = currentNode.x - 1; row <= currentNode.x + 1; row++){
			if(row < 0 || row > map.length - 1){
				continue;
			}
			for(int col = currentNode.y - 1; col <= currentNode.y + 1; col++){
				if(col < 0 || col > map[0].length - 1 || row == x && col == y){
					continue;
				}
				if(map[row][col] != 1 && !nodeAlreadyCreated(row, col, openList, closedList)){
					openList.add(new Node(row, col, goalX, goalY, iterations, currentNode));
				}
			}
		}
	}
	
	
	public boolean nodeAlreadyCreated(int x, int y, PriorityQueue<Node> openList, Set<Node> closedList){
		Node n = new Node(x, y);
		if(openList.contains(n) || closedList.contains(n)){
			return true;
		}
		return false;
	}
}
