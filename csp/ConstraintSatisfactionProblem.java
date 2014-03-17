package assignment_csp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class ConstraintSatisfactionProblem {
	ArrayList<Integer> variable= new ArrayList<Integer>();
	ArrayList<Integer>  value= new ArrayList<Integer>();
	Constraints constraints = new Constraints();
	ArrayList<Integer>  assigns=new ArrayList<Integer>();
	
	
	public ConstraintSatisfactionProblem(int lvariable, int lvalue, Constraints cons) {
		// TODO Auto-generated constructor stub	
		for(int i=0;i<lvariable;i++)
			variable.add(i);
		for(int j=0;j<lvalue;j++)
			value.add(j);
		constraints=cons;//赋值了吗？
	}
	public int[] backTrackingSearch(){
		int[] assignment=new int[variable.size()];//初始化为0了吗？？
		for(int i=0;i<variable.size();i++)
			assignment[i]=-1;
		return backTrack(assignment, constraints);
	}
	private int[] backTrack(int[] assignment,Constraints curcon) {
		// TODO Auto-generated method stub
			
		if(isComplete(assignment))//assignment is complete
			return assignment;
		
		int var = selectedUnassignedVar(assignment,curcon);
		
		//what if the val=[]
		int[] val= orderDomainValues(var, assignment, curcon);
		
		for(int v: val){
			if(isConsistent(var, v, assignment, curcon)){
				assignment[var]=v;
				int[] infer = inference(var,v);
				if(infer.length!=0){
					//add infer in assignment
					for(int i=0;i<=infer.length;i++){
						assignment[]
					}
					//更新curcon里的表
					
					int[] result=backTrack(assignment);
					if(result!=null){
						
						return result;
					}
				}		
			}
			//remove var and infer
			
		}
		return null;
	}
	private int[] inference(int var, int v) {
		// TODO Auto-generated method stub
		
		return null;
	}
	private boolean AC_3(Constraints curcon){
		Queue<int[]> queue = new LinkedList<int[]>();
		curcon.cons
	}
	private boolean isConsistent(int var, int v, int[] assignment, Constraints curcon) {
		// TODO Auto-generated method stub
		ArrayList<Integer> neighbors = curcon.involes(var, variable.size());
		for(int n:neighbors){
			if(assignment[n]!=-1){
				if(assignment[n]==v)
					return false;
			}
		}
		return true;
	}
	private int[] orderDomainValues(int var, int[] assignment, Constraints curcon) {
		// TODO Auto-generated method stub
		return curcon.possiblevalue.get(var);
		
	}
	private int selectedUnassignedVar(int[] assignment, Constraints curcon) {
		// TODO Auto-generated method stub
		
		for(int i=0;i<assignment.length;i++){
			if(assignment[i]==-1){
				//curcon.possiblevalue.get(i)
				return i;
			}
		}
		return -1;
	}
	private boolean isComplete(int[] assignment) {
		// TODO Auto-generated method stub
		for(int i=0;i<assignment.length;i++)
			if(assignment[i]==-1)
				return false;
		return true;
	}
	
}
