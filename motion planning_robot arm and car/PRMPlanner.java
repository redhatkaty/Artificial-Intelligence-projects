package assignment_robots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class PRMPlanner {
	private World world;
	private ArrayList<Double> IniConfig;
	private ArrayList<Double> goalConfig;
	private ArrayList<Double> currentconfig;
	private ArmRobot arm;
	private int K=15;//K nearest
	private int N=300;//#of samples
	
	//The constructor
	public PRMPlanner(World W, ArmRobot a, double[] i, double[] g) {
		// TODO Auto-generated constructor stub
		world=W;
		arm=a;
		//reference http://stackoverflow.com/questions/5178854/convert-a-double-array-to-double-arraylist
		IniConfig= new ArrayList<Double>(Arrays.asList(this.convertd2D(i)));
		goalConfig= new ArrayList<Double>(Arrays.asList(this.convertd2D(g)));		
	}
	
	private Double[] convertd2D(double[] array){
		Double[] narray = new Double[array.length];
		for(int i = 0; i < array.length; i++){
			narray[i] = array[i];
		}
		return narray;
	}
	public HashMap<ArrayList<Double>,List<ArrayList<Double>>> prmGenerator() {
		long startTime = System.currentTimeMillis();
		
		HashMap<ArrayList<Double>,List<ArrayList<Double>>> adjlist=new HashMap<ArrayList<Double>, List<ArrayList<Double>>>(N);
		ArmLocalPlanner ap = new ArmLocalPlanner();
		int linknum=arm.getLinks();
		currentconfig=new ArrayList<Double>();
		for(int l=0;l<IniConfig.size();l++)
			currentconfig.add(IniConfig.get(l));
		Random rng=new Random();
		//will remove node while finding road
		List<ArrayList<Double>> samples=new ArrayList<ArrayList<Double>>(N);
		//use to sort and find the knn, will not change while finding road
		List<ArrayList<Double>> samplessort=new ArrayList<ArrayList<Double>>(N);
		int n=0;
		samples.add(IniConfig);
		samples.add(goalConfig);
		samplessort.add(IniConfig);
		samplessort.add(goalConfig);
		while(n<N){
			ArrayList<Double> config=new ArrayList<Double>();
			config.add(IniConfig.get(0));
			config.add(IniConfig.get(1));
			//creat configs
			for(int ii=2;ii<IniConfig.size();ii+=2){
				config.add(IniConfig.get(ii));
				config.add(rng.nextDouble() * Math.PI * 2);
			}
			ArmRobot a=new ArmRobot(2);
			double[] c= new double[linknum*2+2];
			for(int t=0;t<linknum*2+2;t++)
				c[t]=config.get(t);
			
			a.set(c);
			if(!world.armCollision(a)){
				samples.add(config);
				samplessort.add(config);
				n++;
			}				
		}
		
		int i=0;
		while((i<N+2) && (samples.size()!=0)){
			List<ArrayList<Double>> neighbors=new ArrayList<ArrayList<Double>>(N);
			ArrayList<Double> configs=new ArrayList<Double>();
			for(int l=0;l<samples.get(0).size();l++){
				configs.add(samples.get(0).get(l));
				currentconfig.set(l, samples.get(0).get(l));
			}
			samples.remove(0);
			Collections.sort(samplessort, new comparator());
			double[] g= new double[configs.size()];
			for(int t=0;t<configs.size();t++)
				g[t]=configs.get(t);
			int k=1; 
			while(k<N+2 && neighbors.size()<=K){
				ArrayList<Double> nb=new ArrayList<Double>();
				double[] p= new double[samplessort.get(k).size()];
				for(int t=0;t<samplessort.get(k).size();t++){
					nb.add(samplessort.get(k).get(t));
					p[t]=samplessort.get(k).get(t);
				}
				if(!world.armCollisionPath(arm, g, p))				
					neighbors.add(nb);							
				k++;
			}
			
			adjlist.put(configs, neighbors);
		}	
		System.out.println("--------");
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("K="+ K+" time use"+totalTime);
		return adjlist;
	}
	
	public class comparator implements java.util.Comparator<ArrayList<Double>>{
		@Override
		public int compare(ArrayList<Double> o1, ArrayList<Double> o2) {
			// TODO Auto-generated method stub
			ArmLocalPlanner ap = new ArmLocalPlanner();
			// get the time to move from currenconfig to config;
			double[] d= new double[o1.size()];
			double[] e= new double[o1.size()];
			double[] f= new double[o1.size()];
			for(int t=0;t<o1.size();t++){
				d[t]=currentconfig.get(t);
				e[t]=o1.get(t);
				f[t]=o2.get(t);
			}				
			double time1 = ap.moveInParallel(d, e);
			double time2 = ap.moveInParallel(d, f);
			return (int) Math.signum(time1 - time2);
		}
	}
	
	public List<ArrayList<Double>> astarSearch(HashMap<ArrayList<Double>,List<ArrayList<Double>>> prm) {

		PriorityQueue<ArrayList<Double>> frontier = new PriorityQueue<ArrayList<Double>>(N*N,new comparator2());

		// map to store backchaining information
		HashMap<ArrayList<Double>, ArrayList<Double>> explored = new HashMap<ArrayList<Double>, ArrayList<Double>>();
		//HashSet<SearchNode> nodecost=new HashSet<SearchNode>();
		HashMap<ArrayList<Double>,Double> nodecost=new HashMap<ArrayList<Double>,Double>();
		// startNode must be set by the constructor of the particular
		// search problem, since a UUSearchNode is an interface and can't
		// be instantiated directly by the search
		double[] h= new double[IniConfig.size()];
		double[] i= new double[IniConfig.size()];
		for(int t=0;t<IniConfig.size();t++){
			h[t]=IniConfig.get(t);
			i[t]=goalConfig.get(t);
		}
		explored.put(IniConfig, null); // startNode was not reached from any other node
		ArmLocalPlanner ap = new ArmLocalPlanner();
		// get the time to move from currenconfig to config;
		
		double totalcost=0;
		nodecost.put(IniConfig,totalcost);//nodecost store the explored nodes
		frontier.add(IniConfig);
		
		while (!frontier.isEmpty()) {
		
			ArrayList<Double> currentNode = new  ArrayList<Double>() ;
			currentNode=frontier.remove();
			if (currentNode.equals(goalConfig)) {
				return backchain(currentNode, explored);
			}
			
			List<ArrayList<Double>> successors = prm.get(currentNode);
			
			for (ArrayList<Double> node : successors) {
				double[] j= new double[IniConfig.size()];
				for(int t=0;t<node.size();t++)
					j[t]=node.get(t);
				totalcost = ap.moveInParallel(i, j);
				// if not visited
				if (!explored.containsKey(node)
						||(totalcost<nodecost.get(node))){
					explored.put(node, currentNode);
					nodecost.put(node, totalcost);
					frontier.add(node);	
				}		
			}
		}
		return null;
	}	
	public class comparator2 implements java.util.Comparator<ArrayList<Double>>{
		@Override
		public int compare(ArrayList<Double> o1, ArrayList<Double> o2) {
			// TODO Auto-generated method stub
			ArmLocalPlanner ap = new ArmLocalPlanner();
			// get the time to move from currenconfig to config;
			double[] d= new double[o1.size()];
			double[] e= new double[o1.size()];
			double[] f= new double[o1.size()];
			double[] j= new double[IniConfig.size()];
			
			for(int t=0;t<o1.size();t++){
				d[t]=IniConfig.get(t);
				e[t]=goalConfig.get(t);
				f[t]=o2.get(t);
				j[t]=o1.get(t);
			}				
			double totalcost1 = ap.moveInParallel(e, j);
			double totalcost2 =	ap.moveInParallel(e, f);
			return (int) Math.signum(totalcost1 - totalcost2);
		}
	}
	protected List<ArrayList<Double>> backchain(ArrayList<Double> node,
			HashMap<ArrayList<Double>, ArrayList<Double>> visited) {
		LinkedList<ArrayList<Double>> solution = new LinkedList<ArrayList<Double>>();
		while (node != null) {
			solution.addFirst(node);
			node = visited.get(node);
		}
		return solution;
	}
}
