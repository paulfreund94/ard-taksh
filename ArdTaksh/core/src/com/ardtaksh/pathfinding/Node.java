package com.ardtaksh.pathfinding;

public class Node implements Comparable<Node>{
	public final int x;
	public final int y;

	public int sumOfCostFromStartAndEstimatedCostFromGoal;
	public Node predecessor;
	
	public Node(int x, int y, int goalX, int goalY, int costFromStart, Node predecessor){
		this.x = x;
		this.y = y;
		int deltaX = Math.abs(goalX - x);
		int deltaY = Math.abs(goalY - y);
		int estimatedCostFromGoal = Math.max(deltaX, deltaY);
		this.sumOfCostFromStartAndEstimatedCostFromGoal = costFromStart + estimatedCostFromGoal;
		this.predecessor = predecessor;
	}
	
	public Node(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Node arg) {
		if(this.sumOfCostFromStartAndEstimatedCostFromGoal > arg.sumOfCostFromStartAndEstimatedCostFromGoal){
			return 1;
		}
		if(this.sumOfCostFromStartAndEstimatedCostFromGoal < arg.sumOfCostFromStartAndEstimatedCostFromGoal){
			return -1;
		}
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Node){
			Node n = (Node)o;
			if(this.x == n.x && this.y == n.y){
				return true;
			}
		}
		return false;
	}
	
}
