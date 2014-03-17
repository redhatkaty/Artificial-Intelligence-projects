package assignment_mazeworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import assignment_mazeworld.SearchProblem.SearchNode;
/**
 * 
 * @author KATYLI
 *
 */
public class InformedSearchProblem extends SearchProblem {
	
	/**
	 * @param 
	 * @return
	 */
	public List<SearchNode> astarSearch() {
		resetStats();

		PriorityQueue<SearchNode> frontier = new PriorityQueue<SearchNode>();

		// map to store backchaining information
		HashMap<SearchNode, SearchNode> explored = new HashMap<SearchNode, SearchNode>();
		//HashSet<SearchNode> nodecost=new HashSet<SearchNode>();
		HashMap<SearchNode,Double> nodecost=new HashMap<SearchNode,Double>();
		// startNode must be set by the constructor of the particular
		// search problem, since a UUSearchNode is an interface and can't
		// be instantiated directly by the search

		explored.put(startNode, null); // startNode was not reached from any other node
		nodecost.put(startNode,startNode.getCost()+startNode.heuristic());//nodecost store the explored nodes
		frontier.add(startNode);
		
		while (!frontier.isEmpty()) {
			incrementNodeCount();

			updateMemory(frontier.size() + explored.size());
		
			SearchNode currentNode = frontier.remove();

			if (currentNode.goalTest()) {
				return backchain(currentNode, explored);
			}
			
			ArrayList<SearchNode> successors = currentNode.getSuccessors();

			// System.out.println("successors " + successors);
			
			for (SearchNode node : successors) {
				// if not visited
				if (!explored.containsKey(node)
						||(node.getCost()+node.heuristic()<nodecost.get(node))){
					explored.put(node, currentNode);
					nodecost.put(node, node.getCost()+node.heuristic());
					frontier.add(node);
					
				}
					
			}
		}
		return null;
	}	
}
