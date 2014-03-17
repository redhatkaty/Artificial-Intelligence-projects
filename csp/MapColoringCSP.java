package assignment_csp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapColoringCSP extends ConstraintSatisfactionProblem{
	
	
	//constructors
	//change string to int, pass in the X,D,C
	public MapColoringCSP(String[] var, String[] val, Constraints cons){
		super(var.length,val.length, cons);		
		
	}
	
	
	
	public static void main(String[] args){
		String [] var={"WA","NT","Q","NSW","V","SA","T"};
		String [] val={"Red","Green","Blue"};
		//String [] cpairs={"WA","NT","WA","SA","NT","SA","NT","Q","SA","Q","SA","NSW","SA","V","Q","NSW","NSW","V"};
		int[] cpairs={0,1,0,5,1,5,1,2,2,5,2,3,3,5,3,4,4,5};
		HashMap<Integer,String> vList = new HashMap<Integer,String>();
		for(int i=0; i<var.length;i++)//±àºÅ´Ó0¿ªÊ¼
			vList.put(i,var[i]);
		for(int i=0; i<val.length;i++)
			vList.put(i,val[i]);
		List<int[]> pairs = new ArrayList<int[]>();
		for(int i=0; i<cpairs.length;i+=2){
			int [] pair=new int[]{cpairs[i],cpairs[i+1]};
			pairs.add(pair);
		}
		
		Constraints cons= new Constraints(pairs);
		MapColoringCSP mpCSP= new MapColoringCSP(var, val, cons);
	}
}
