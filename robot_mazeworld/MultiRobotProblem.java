package assignment_mazeworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

// Find a path for a single agent to get from a start location (xStart, yStart)
//  to a goal location (xGoal, yGoal)

public class MultiRobotProblem extends InformedSearchProblem {

	private static int actions[][] = {Maze.NORTH, Maze.EAST, Maze.SOUTH, Maze.WEST, Maze.STILL}; 
	
	private int[] xStart, yStart, xGoal, yGoal;

	private Maze maze;
	
	public MultiRobotProblem(Maze m, int[] sx, int[] sy, int[] gx, int[] gy) {
		startNode = new SimpleMazeNode(sx, sy, 0);
		xStart = sx;
		yStart = sy;
		xGoal = gx;
		yGoal = gy;
		
		maze = m;		
	}
	
	// node class used by searches.  Searches themselves are implemented
	//  in SearchProblem.
	public class SimpleMazeNode implements SearchNode {

		// location of the agent in the maze
		protected ArrayList<int[]> state; 
		
		// how far the current node is from the start.  Not strictly required
		//  for uninformed search, but useful information for debugging, 
		//  and for comparing paths
		private double cost;  
		private int totalrobot;
		public SimpleMazeNode(int[] x, int[] y, double c) {
			state = new ArrayList<int[]>(2);			
			this.state.add(0, x);
			this.state.add(1, y);		
			cost = c;
			totalrobot=state.get(0).length;
		}
		
		public int[] getX() {
			return state.get(0);
		}
		
		public int[] getY() {
			return state.get(1);
		}

		public ArrayList<SearchNode> getSuccessors() {

			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
			Queue<ArrayList<int[]>> sucqueue=new LinkedList<ArrayList<int[]>>();
			ArrayList<int[]> currentstate=new ArrayList<int[]>();//only state
			ArrayList<int[]> firststate=new ArrayList<int[]>();//contains the turn
			int totalturn=totalrobot;
			int[] turn={0};
			int currentturn=0;
			firststate.addAll(state);
			firststate.add(turn);
			sucqueue.add(firststate);
			while(!sucqueue.isEmpty()){
				currentstate=sucqueue.remove();//sucqueue contains the states after partial robots move
				currentturn=currentstate.get(2)[0];//it means the "currentturn"th robot has moved
				for (int[] action: actions) {//get success actions
					int[] xNew = currentstate.get(0).clone();
					int[] yNew = currentstate.get(1).clone();
					xNew[currentturn] = currentstate.get(0)[currentturn] + action[0];
					yNew[currentturn] = currentstate.get(1)[currentturn] + action[1]; 
					
					if(maze.isLegal(xNew, yNew,currentstate.get(0),currentstate.get(1))) {
						
						if(currentturn+1==totalturn){
							SearchNode succ = new SimpleMazeNode(xNew, yNew, getCost() + 1.0);
							successors.add(succ);
						}
						else{
							ArrayList<int[]>newstate=new ArrayList<int[]>();
							newstate.add(0, xNew);
							newstate.add(1, yNew);
							turn[0]=currentturn+1;
							newstate.add(2, turn);
							sucqueue.add(newstate);
						}
					}
				}				
			}
			return successors;
		}

		
		@Override
		public boolean goalTest() {	
			return Arrays.equals(state.get(0), xGoal) && Arrays.equals(state.get(1), yGoal);
		}


		// an equality test is required so that visited sets in searches
		// can check for containment of states
		@Override
		public boolean equals(Object other) {
			int[] x=state.get(0);
			int[] y=state.get(1);
			for(int i=0; i<x.length;i++){
				if(x[i]!=((SimpleMazeNode) other).state.get(0)[i]||
				   y[i]!=((SimpleMazeNode) other).state.get(1)[i])
					return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			int hashcode=0;
			for(int hc=0;hc<totalrobot; hc++) 
				hashcode+=(state.get(0)[hc]* 10 + state.get(1)[hc]*1)*Math.pow(10, hc); 
			return hashcode;
		}

		@Override
		public String toString() {
			 String s=new String();
			 s="Maze state " ;
			 for(int i=0;i<totalrobot; i++) s+="["+state.get(0)[i]+","+state.get(1)[i]+"]";
			 s+= " depth " + getCost()+"\n";
			 return s;
		}

		@Override
		public double getCost() {
			return cost;
		}
		

		@Override
		public double heuristic() {
			// manhattan distance metric for simple maze with one agent:
			double dx=0;
			double dy=0;
			for(int i=0;i<totalrobot;i++){
				dx += Math.abs(xGoal[i] - state.get(0)[i]);
				dy += Math.abs(yGoal[i] - state.get(1)[i]);
			}
			
			return dx+dy;
		}

		@Override
		public int compareTo(SearchNode o) {
			return (int) Math.signum(priority() - o.priority());
		}
		
		@Override
		public double priority() {
			return heuristic() + getCost();
		}

	}

}
