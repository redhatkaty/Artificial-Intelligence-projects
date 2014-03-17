
// Xinqi Li(F001C31), start date 01/08/14
// Reference:THIS STUB, BY DEVIN BALKCOM.

package cannibals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;



public abstract class UUSearchProblem {
	
	// used to store performance information about search runs.
	//  these should be updated during the process of searches

	// see methods later in this class to update these values
	protected int nodesExplored;
	protected int maxMemory;

	protected UUSearchNode startNode;
	
	protected interface UUSearchNode {
		public ArrayList<UUSearchNode> getSuccessors();
		public boolean goalTest();
		public int getDepth();
	}

	// breadthFirstSearch:  return a list of connecting Nodes, or null
	// no parameters, since start and goal descriptions are problem-dependent.
	//  therefore, constructor of specific problems should set up start
	//  and goal conditions, etc.
	
	public List<UUSearchNode> breadthFirstSearch(){
		resetStats();
		// You will write this method
		int i;
		HashMap<UUSearchNode,UUSearchNode> explored=new HashMap<UUSearchNode,UUSearchNode>();//explored:tracking the explored nodes
		List<UUSearchNode> path=new ArrayList<UUSearchNode>();//the path to goal
		Queue<UUSearchNode> queue = new LinkedList<UUSearchProblem.UUSearchNode>();
		
		queue.add(this.startNode);//initializing, add start node in queue
		explored.put(startNode,null);
		incrementNodeCount();//update the node it explored
		updateMemory(explored.size());
		
		while(!queue.isEmpty()) {
			//dequeue the head of queue to be the current node
			UUSearchNode current_node = (UUSearchNode)queue.remove();
			
			if(current_node.goalTest()==true){
				path=backchain(current_node, explored);
			}
			else{
				//add unexplored successors to fringe
				ArrayList<UUSearchNode> successors= new ArrayList<UUSearchNode>();
				successors=current_node.getSuccessors();	
				for(i=0;i<successors.size();i++) {
					incrementNodeCount();//update the node it explored before check for explored ones
					if(explored.containsKey(successors.get(i))==false){
						explored.put(successors.get(i),current_node);
						queue.add(successors.get(i));
						updateMemory(explored.size());
						
					}	
				}
			}
		}
		return path;
	}
	
	// backchain should only be used by bfs, not the recursive dfs
	private List<UUSearchNode> backchain(UUSearchNode node,
			HashMap<UUSearchNode, UUSearchNode> visited) {
		// you will write this method
		List<UUSearchNode> path=new ArrayList<UUSearchNode>();
		
		while(node!=null){
			path.add(node);
			node=visited.get(node);
		}	
		Collections.reverse(path);
		return path;
	}

	public List<UUSearchNode> depthFirstMemoizingSearch(int maxDepth) {
		resetStats(); 
		UUSearchNode current_node=this.startNode;
		HashMap<UUSearchNode, Integer> explored=new HashMap<UUSearchNode,Integer>();//explored:tracking the explored nodes
		explored.put(current_node, 0);
		updateMemory(explored.size());
		incrementNodeCount();
		List<UUSearchNode> path=dfsrm(current_node, explored, 0, maxDepth);//the path to goal
		return path;	
	}

	// recursive memoizing dfs. Private, because it has the extra
	// parameters needed for recursion.  
	private List<UUSearchNode> dfsrm(UUSearchNode currentNode, HashMap<UUSearchNode, Integer> visited, 
			int depth, int maxDepth) {
		List<UUSearchNode> current_path=new ArrayList<UUSearchNode>();
		
		// keep track of stats; these calls charge for the current node
		updateMemory(visited.size());
		int i;//count for the successors
		// you write this method.  Comments *must* clearly show the 
		//  "base case" and "recursive case" that any recursive function has.	
		//base case
		if(currentNode.goalTest()==true)
		{
			current_path.add(currentNode);
			return current_path;
		}
//		recursive case
		else {
			ArrayList<UUSearchNode> successors= new ArrayList<UUSearchNode>();
			successors=currentNode.getSuccessors();
		
				for(i=0;i<successors.size();i++) {
					incrementNodeCount();
					if( successors.get(i).getDepth()<=maxDepth //depth must smaller than maxDepth
							&&( visited.containsKey(successors.get(i))==false//the state cannot be visited 
							||successors.get(i).getDepth()<visited.get(successors.get(i))))//or if the depth is smaller than the visited one
					{	//go ahead to search the successor(i)
						visited.put(successors.get(i),successors.get(i).getDepth());
						List<UUSearchNode> suc_path=new ArrayList<UUSearchNode>();
						suc_path=dfsrm(successors.get(i), visited, successors.get(i).getDepth(), maxDepth);
						if(!suc_path.isEmpty()){
							current_path.clear();
							
							current_path.add(currentNode);
							current_path.addAll(suc_path);//the path to goal
							return current_path;
						} 	
					}
				}
				return current_path;
		}	
	}
	
	// set up the iterative deepening search, and make use of dfspc
	public List<UUSearchNode> IDSearch(int maxDepth) {
		resetStats();
		// you write this method
		int md=0;
		
		//HashSet<UUSearchNode> explored=new HashSet<UUSearchNode>();//explored:tracking the explored nodes
		HashMap<UUSearchNode, Integer> explored=new HashMap<UUSearchNode, Integer>();
		explored.put(startNode, null);
		List<UUSearchNode> path=new ArrayList<UUSearchProblem.UUSearchNode>();
		updateMemory(explored.size());
		incrementNodeCount();
		
		for(md=0;path.isEmpty();md++){
			if(md>maxDepth)
				return path;
			
			explored.clear();	
			path=dfsrm(startNode, explored, 0, md);//the path to goal
		}		
		return path;
	}

	// set up the depth-first-search (path-checking version), 
	//  but call dfspc to do the real work
	public List<UUSearchNode> depthFirstPathCheckingSearch(int maxDepth) {
		resetStats();
		
		// I wrote this method for you.  Nothing to do.

		HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();
		incrementNodeCount();
		return dfsrpc(startNode, currentPath, 0, maxDepth);

	}

	// recursive path-checking dfs. Private, because it has the extra
	// parameters needed for recursion.
	private List<UUSearchNode> dfsrpc(UUSearchNode currentNode, HashSet<UUSearchNode> currentPath,
			int depth, int maxDepth) {

		// you write this method
		currentPath.add(currentNode);	
		// keep track of stats; these calls charge for the current node
		updateMemory(currentPath.size());
		
		int i;//count for the successors
		List<UUSearchNode> path=new ArrayList<UUSearchProblem.UUSearchNode>();
		//base case
		if(currentNode.goalTest()==true)
		{
			path.add(currentNode);
			return path;
		}
//		recursive case
		else {
			ArrayList<UUSearchNode> successors= new ArrayList<UUSearchNode>();
			successors=currentNode.getSuccessors();
			
				for(i=0;i<successors.size();i++) {
					incrementNodeCount();
					if( successors.get(i).getDepth()<=maxDepth //depth must smaller than maxDepth
							&& currentPath.contains(successors.get(i))==false)//the state cannot be visited 		
					{	//go ahead to search the successor(i)
						
						List<UUSearchNode> suc_path=new ArrayList<UUSearchNode>();
						suc_path=dfsrpc(successors.get(i), currentPath, successors.get(i).getDepth(), maxDepth);
						if(!suc_path.isEmpty()){
							//currentPath.clear();
							path.add(currentNode);
							path.addAll(suc_path);//the path to goal
							return path;
						}
						else
							currentPath.remove(successors.get(i));//becareful to the change of the currentpath, I add current node in every begin point of a fuction nomatter whether it has sub_path
					}
				}
				return path;
		}
	}
		
	

	protected void resetStats() {
		nodesExplored = 0;
		maxMemory = 0;
	}
	
	protected void printStats() {
		System.out.println("Nodes explored during last search:  " + nodesExplored);
		System.out.println("Maximum memory usage during last search " + maxMemory);
	}
	
	protected void updateMemory(int currentMemory) {
		maxMemory = Math.max(currentMemory, maxMemory);
	}
	
	protected void incrementNodeCount() {
		nodesExplored++;
	}

}
