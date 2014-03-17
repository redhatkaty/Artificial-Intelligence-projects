package cannibals;

import java.util.ArrayList;
import java.util.Arrays;

import cannibals.UUSearchProblem.UUSearchNode;


// for the first part of the assignment, you might not extend UUSearchProblem,
//  since UUSearchProblem is incomplete until you finish it.

public class CannibalProblem extends UUSearchProblem {

	// the following are the only instance variables you should need.
	//  (some others might be inherited from UUSearchProblem, but worry
	//  about that later.)

	private int goalm, goalc, goalb;
	private int totalMissionaries, totalCannibals; 
	
	public CannibalProblem(int sm, int sc, int sb, int gm, int gc, int gb) {
		// I (djb) wrote the constructor; nothing for you to do here.

		startNode = new CannibalNode(sm, sc, 1, 0);
		goalm = gm;
		goalc = gc;
		goalb = gb;
		totalMissionaries = sm;
		totalCannibals = sc;
		
	}
	public static void main(String args[]) {
		CannibalProblem mcProblem = new CannibalProblem(3, 3, 1, 0, 0, 0);
		CannibalNode currentnode=mcProblem.new CannibalNode(2,2, 1, 6);
		ArrayList<UUSearchNode> successors= new ArrayList<UUSearchNode>();
		successors=currentnode.getSuccessors();
		System.out.println("successors of "+currentnode+"are"+successors);
	}

	// node class used by searches.  Searches themselves are implemented
	//  in UUSearchProblem.
	private class CannibalNode implements UUSearchNode {

		// do not change BOAT_SIZE without considering how it affect
		// getSuccessors. 
		
		private final static int BOAT_SIZE = 2;
	
		// how many missionaries, cannibals, and boats
		// are on the starting shore
		private int[] state; 
		
		// how far the current node is from the start.  Not strictly required
		//  for search, but useful information for debugging, and for comparing paths
		private int depth;  
		//*****current state
		public CannibalNode(int m, int c, int b, int d) {
			state = new int[3];
			this.state[0] = m;
			this.state[1] = c;
			this.state[2] = b;
			
			depth = d;

		}
		
		public ArrayList<UUSearchNode> getSuccessors() {

			// add actions (denoted by how many missionaries and cannibals to put
			// in the boat) to current state. ******legal states
			ArrayList<UUSearchNode> successors=new ArrayList<UUSearchProblem.UUSearchNode>();
			
			int m_current=state[0];
			int c_current=state[1]; 
			int b_current=state[2];
			int m_move;
			int c_move;
			int c_suc;
			int m_suc;
			int d_suc=depth+1;//successors depth is one more than the current state's.
			
			if(b_current==1)//minus
			{
				for(m_move=0;m_move<=BOAT_SIZE;m_move++)
					for(c_move=0;c_move<=BOAT_SIZE;c_move++)
					{
						if((m_move+c_move)!=0&&(m_move+c_move)<=BOAT_SIZE)
						{
							m_suc=m_current-m_move;
							c_suc=c_current-c_move;
							if(isSafeState(m_suc,c_suc))
							{
								CannibalNode suc= new CannibalNode(m_suc, c_suc, 0, d_suc);
								successors.add(suc);
//								for test use
								//System.out.println("depth:"+d_suc+"state:"+suc.state[0]+suc.state[1]+suc.state[2]);
								
							}
						}
					}
			}			
			else if(b_current==0)//add
			{
				for(m_move=0;m_move<=BOAT_SIZE;m_move++)//change the cannibal's amount at first
					for(c_move=0;c_move<=BOAT_SIZE;c_move++)
					{
						if((m_move+c_move)!=0&&(m_move+c_move)<=BOAT_SIZE)
						{
							m_suc=m_current+m_move;
							c_suc=c_current+c_move;
							if(isSafeState(m_suc,c_suc))
							{
								CannibalNode suc= new CannibalNode(m_suc, c_suc, 1, d_suc);
								successors.add(suc);
//								for test use
								//System.out.println("depth:"+d_suc+"state:"+suc.state[0]+suc.state[1]+suc.state[2]);
							}
						}
					}
			}
			return successors;
		}
			// You write this method.  Factoring is usually worthwhile.  In my
			//  implementation, I wrote an additional private method 'isSafeState',
			//  that I made use of in getSuccessors.  You may write any method
			//  you like in support of getSuccessors.
		/*
		 * isSafeState check whether the state is valid
		 **/
		public boolean isSafeState(int m,int c)
		{
			
			if(m>=0&&c>=0&&(m>=c||m==0)//be careful to the lower bound
					&&m<=totalMissionaries
					&&c<=totalCannibals
					&&(m==3||totalMissionaries-m>=totalCannibals-c))
				return true;
			else
				return false;
		}
		@Override
		public boolean goalTest() {
			// you write this method.  (It should be only one line long.)			
			if(state[0]==goalm&&state[1]==goalc&&state[2]==goalb)
				return true;
			else return false;
		}

		

		// an equality test is required so that visited lists in searches
		// can check for containment of states
		@Override
		public boolean equals(Object other) {
			return Arrays.equals(state, ((CannibalNode) other).state);
		}

		@Override
		public int hashCode() {
			return state[0] * 100 + state[1] * 10 + state[2];
		}

		@Override
		public String toString() {
			// you write this method
			String states=new String();
			states="["+state[0]+state[1]+state[2]+"]";
			return states;
		}

		/*
        You might need this method when you start writing 
        (and debugging) UUSearchProblem.
        */
		@Override
		public int getDepth() {
			return depth;//????the former state will get another depth #
		}
		
	}
	

}
