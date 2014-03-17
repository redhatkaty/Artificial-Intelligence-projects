package assignment_csp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constraints {
	//constraints
	HashMap<Integer, List<int[]>> cons = new HashMap<Integer, List<int[]>>();
	ArrayList<int[]> possiblevalue =new ArrayList<int[]>();
	
	public Constraints() {
		// TODO Auto-generated constructor stub
	}
	public Constraints(List<int[]> pairs) {
		// TODO Auto-generated constructor stub
		for(int k=0;k<pairs.size();k++){
			List<int[]> valuepair = new ArrayList<int[]>();		
			int[] value=new int[]{0,1,2};
			for(int i=0;i<3; i++){
				for(int j=0;j<3; j++){				
					if(i!=j){
						int[] color= new int[]{i,j};
						valuepair.add(color);
					}	
				}
			}		
			//不需要穷举吧？只需要知道关系，然后有possiblevalue就够了？？
			cons.put(pairs.get(k).hashCode(), valuepair);
			possiblevalue.add(value);
		}
		
	}
	
	
	public boolean isSatisfied(){
		
		
		return false;
		
	}
	//implement one of your variable-ordering heuristics in the CSP
	//returns the neighbor of var
	public ArrayList<Integer> involes(int var, int vsize){
		int[] vars={};
		ArrayList<Integer> involes = new ArrayList<Integer>();
		
		for(int i=0;i<vsize;i++){
			if(i>var) 
				vars= new int[]{var,i};
			if(i<var)
				vars= new int[]{i,var};
			if (cons.containsKey(vars.hashCode()))
				involes.add(var);		
		}
		return involes;	
	}
	
}
