package assignment_robots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class RRTPlanner {
	public World world;
	public CarRobot car;
	public CarState start;
	public CarState goal;
	public int w_width;
	public int w_height;
	public CarState randstate = new CarState();
	CarState current_node=new CarState();
	
	public double step=2;
	public int K=2000;//K iteration
	
	public RRTPlanner(World W, CarRobot c, CarState s, CarState g, int w, int h) {
		world=W;
		car=c;
		start=s;
		goal=g;
		w_width=w;
		w_height=h;
	}
	
	public HashMap<CarState,ArrayList<CarState>> RRTGenerator(){
		HashMap<CarState,ArrayList<CarState>> adjlist = new HashMap<CarState,ArrayList<CarState>>();
		Random rng=new Random();//random number generator
		CarState state=new CarState();//get the random state before collision checking
		CarRobot carrobot=new CarRobot();
		//nodes is used to sort and find the nearest node.
		List<CarState> nodes = new ArrayList<CarState>();
		SteeredCar planner = new SteeredCar();
		
		nodes.add(start);
		nodes.add(goal);
		for(int iter=0; iter<K;){
			state.set(rng.nextDouble() *w_width, rng.nextDouble() *w_height, rng.nextDouble() * Math.PI * 2);
			carrobot.set(state);
			//check the random state is collision free
			if(!world.carCollision(carrobot)){
				CarState near=new CarState();
				CarState newstate=new CarState();
				
				randstate.set(state.getX(), state.getY(), state.getTheta());
				Collections.sort(nodes, new comparator());
				near.set(nodes.get(0).getX(), nodes.get(0).getY(), nodes.get(0).getTheta());
				
				int ctl=rng.nextInt(5);
				carrobot.set(near);
				//check the path collision
				if(!world.carCollisionPath(carrobot, near, ctl, step)){
					newstate= planner.move(near, ctl, step);
					
					carrobot.set(newstate);
					ArrayList<CarState> adjs = new ArrayList<CarState>(); 
					if(adjlist.containsKey(near)){	
						adjs = adjlist.get(near);
						adjs.add(newstate);
						adjlist.remove(near);
						System.out.println(near+"has more than one neighbor");
					}
					else
						adjs.add(newstate);
					adjlist.put(near, adjs);
					nodes.add(newstate);
					iter++;
				}
			}
		}
		return adjlist;
		
	}
	
	public class comparator implements java.util.Comparator<CarState>{
		@Override
		public int compare(CarState c1, CarState c2) {
			// TODO Auto-generated method stub
							
			double d1 = Math.sqrt(Math.pow(c1.getX()-randstate.getX(), 2)+Math.pow(c1.getY()-randstate.getY(), 2));
			double d2 = Math.sqrt(Math.pow(c2.getX()-randstate.getX(), 2)+Math.pow(c2.getY()-randstate.getY(), 2));
			return (int) Math.signum(d1 - d2);
		}
	}
	
	
	// breadthFirstSearch:  return a list of connecting Nodes, or null
		// no parameters, since start and goal descriptions are problem-dependent.
		//  therefore, constructor of specific problems should set up start
		//  and goal conditions, etc.
		
		public List<CarState> breadthFirstSearch(HashMap<CarState,ArrayList<CarState>> adjList){
			
			int i;
			HashMap<CarState,CarState> explored=new HashMap<CarState,CarState>();//explored:tracking the explored nodes
			List<CarState> path=new ArrayList<CarState>();//the path to goal
			Queue<CarState> queue = new LinkedList<CarState>();
			
			queue.add(start);//initializing, add start node in queue
			explored.put(start,null);
						
			while(!queue.isEmpty()) {
				//dequeue the head of queue to be the current node
				current_node = (CarState)queue.remove();
				
				if(current_node.equals(goal)){
					path=backchain(current_node, explored);
					System.out.println("get to the goal");
				}
				else{
					//add unexplored successors to fringe
					ArrayList<CarState> successors= new ArrayList<CarState>();
					successors=adjList.get(current_node);	
					if(successors!=null){
						for(i=0;i<successors.size();i++) {
							
							if(explored.containsKey(successors.get(i))==false){
								explored.put(successors.get(i),current_node);
								queue.add(successors.get(i));
							}	
						}
					}					
				}
			}
			path=backchain(current_node, explored);
			return path;
		}
		
		// backchain should only be used by bfs, not the recursive dfs
		private List<CarState> backchain(CarState node,
				HashMap<CarState, CarState> visited) {
			// you will write this method
			List<CarState> path=new ArrayList<CarState>();
			
			while(node!=null){
				path.add(node);
				node=visited.get(node);
			}	
			Collections.reverse(path);
			return path;
		}
}
