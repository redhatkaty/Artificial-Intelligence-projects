package assignment_robots;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;


public class PRMProblem {
	public World world;
	public double[] IniConfig;
	public double[] goalConfig;
	public double[] currentconfig;
	ArmRobot arm;
	int K=15;//K nearest
	int N=100;//#of samples
	public PRMProblem(World W, ArmRobot a, double[] i, double[] g) {
		// TODO Auto-generated constructor stub
		world=W;
		arm=a;
		IniConfig=i.clone();
		goalConfig=g.clone();
	}
	public HashMap<double[],List<double[]>> prmGenerator() {
		HashMap<double[],List<double[]>> adjlist=new HashMap<double[], List<double[]>>(N);
		//Graph prmGraph=new Graph();
		
		ArmLocalPlanner ap = new ArmLocalPlanner();
		int linknum=arm.getLinks();
		currentconfig=new double[linknum*2+2];
		for(int l=0;l<IniConfig.length;l++)
			currentconfig[l]=IniConfig[l];
		
		//double angle=0.0;
		Random rng=new Random();
		List<double[]> samples=new ArrayList<>(N);
		List<double[]> samplessort=new ArrayList<>(N);
		int n=0;
		samples.add(IniConfig);
		samples.add(goalConfig);
		samplessort.add(IniConfig);
		samplessort.add(goalConfig);
		while(n<N){
			double[] config=new double[2*linknum+2];
			config[0]=IniConfig[0];
			config[1]=IniConfig[1];
			//creat configs
			for(int ii=2;ii<linknum*2+2;ii+=2){
				config[ii]=IniConfig[ii];
				config[ii+1]=rng.nextDouble() * Math.PI * 2;
			}
			ArmRobot a=new ArmRobot(2);
			a.set(config.clone());
			if(!world.armCollision(a)){
				samples.add(config);
				samplessort.add(config);
				n++;
			}				
		}
		
		//(i<N+2) && 
		int i=0;
		while((i<N+2) && (samples.size()!=0)){
			//PriorityQueue<double[]> KNN=new PriorityQueue<double[]>(K,new comparator());
			List<double[]> neighbors=new ArrayList<double[]>(N);
			double[] configs=new double[linknum*2+2];
			for(int l=0;l<linknum*2+2;l++)
				configs[l]=samples.get(0)[l];
			
			samples.remove(0);
			//adjlist.put(currentconfig,null);//或者取消，看能不能更新
			
			Collections.sort(samplessort, new comparator());
			//test
			double t= ap.moveInParallel(configs, samplessort.get(0))
			- ap.moveInParallel(configs, samplessort.get(1));
			
			int k=1; 
			while(k<N+2 && neighbors.size()<=K){
				double[] nb=samplessort.get(k).clone();
				if(!world.armCollisionPath(arm, configs, nb)){				
					// get the time to move from currenconfig to config;
					//double time = ap.moveInParallel(currentconfig, nb);
					neighbors.add(nb.clone());							
				}
				k++;
			}
			
			adjlist.put(configs.clone(), neighbors);
		}	
		return adjlist;
	}
	
//	public class Graph{
//		public HashMap<double[],PriorityQueue> adjlist=new HashMap<double[],PriorityQueue>();
//		public Graph(){
//			
//		}
//	}
	
	public class comparator implements java.util.Comparator<double[]>{
		@Override
		public int compare(double[] o1, double[] o2) {
			// TODO Auto-generated method stub
			ArmLocalPlanner ap = new ArmLocalPlanner();
			// get the time to move from currenconfig to config;
			double time1 = ap.moveInParallel(currentconfig, o1);
			double time2 = ap.moveInParallel(currentconfig, o2);
			return (int) Math.signum(time1 - time2);
		}
	}
	
	public List<double[]> astarSearch(HashMap<double[], List<double[]>> prm) {

		PriorityQueue<double[]> frontier = new PriorityQueue<double[]>();

		// map to store backchaining information
		HashMap<double[], double[]> explored = new HashMap<double[], double[]>();
		//HashSet<SearchNode> nodecost=new HashSet<SearchNode>();
		HashMap<double[],Double> nodecost=new HashMap<double[],Double>();
		// startNode must be set by the constructor of the particular
		// search problem, since a UUSearchNode is an interface and can't
		// be instantiated directly by the search
		
		explored.put(IniConfig, null); // startNode was not reached from any other node
		ArmLocalPlanner ap = new ArmLocalPlanner();
		// get the time to move from currenconfig to config;
		
		double totalcost=0;
		nodecost.put(IniConfig,totalcost);//nodecost store the explored nodes
		frontier.add(IniConfig);
		
		while (!frontier.isEmpty()) {
		
			double[] currentNode = frontier.remove();

			if (currentNode.equals(goalConfig)) {
				return backchain(currentNode, explored);
			}
			
			List<double[]> successors = prm.get(currentNode);

			// System.out.println("successors " + successors);
			
			for (double[] node : successors) {
				
				totalcost = ap.moveInParallel(IniConfig, node)
							+ap.moveInParallel(goalConfig, node);
				// if not visited
				if (!explored.containsKey(node)//能判断吗
						||(totalcost<nodecost.get(node))){
					explored.put(node, currentNode);
					nodecost.put(node, totalcost);
					frontier.add(node);
					
				}
					
			}
		}
		return null;
	}	
	protected List<double[]> backchain(double[] node,
			HashMap<double[], double[]> visited) {

		LinkedList<double[]> solution = new LinkedList<double[]>();

		// chain through the visited hashmap to find each previous node,
		// add to the solution
		while (node != null) {
			solution.addFirst(node);
			node = visited.get(node);
		}

		return solution;
	}
}
