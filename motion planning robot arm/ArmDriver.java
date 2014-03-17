package assignment_robots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class ArmDriver extends Application {
	// default window size
	protected int window_width = 600;
	protected int window_height = 400;
	
	public void addPolygon(Group g, Double[] points) {
		Polygon p = new Polygon();
	    p.getPoints().addAll(points);
	    
	    g.getChildren().add(p);
	}
	
	// plot a ArmRobot;
	public void plotArmRobot(Group g, ArmRobot arm, double[] config) {
		arm.set(config);
		double[][] current;
		Double[] to_add;
		Polygon p;
		for (int i = 1; i <= arm.getLinks(); i++) {
			current = arm.getLinkBox(i);
			
			
			to_add = new Double[2*current.length];
			for (int j = 0; j < current.length; j++) {
				System.out.println(current[j][0] + ", " + current[j][1]);
				to_add[2*j] = current[j][0];
				//to_add[2*j+1] = current[j][1];
				to_add[2*j+1] = window_height - current[j][1];
			}
			p = new Polygon();
			p.getPoints().addAll(to_add);
			p.setStroke(Color.BLUE);
			p.setFill(Color.LIGHTBLUE);
			g.getChildren().add(p);
		}
		
	}
	
	public void plotWorld(Group g, World w) {
		int len = w.getNumOfObstacles();
		double[][] current;
		Double[] to_add;
		Polygon p;
		for (int i = 0; i < len; i++) {
			current = w.getObstacle(i);
			to_add = new Double[2*current.length];
			for (int j = 0; j < current.length; j++) {
				to_add[2*j] = current[j][0];
				//to_add[2*j+1] = current[j][1];
				to_add[2*j+1] = window_height - current[j][1];
			}
			p = new Polygon();
			p.getPoints().addAll(to_add);
			g.getChildren().add(p);
		}
	}
	
	// The start function; will call the drawing;
	// You can run your PRM or RRT to find the path; 
	// call them in start; then plot the entire path using
	// interfaces provided;
	@Override
	public void start(Stage primaryStage) {
		
		
		// setting up javafx graphics environments;
		primaryStage.setTitle("CS 76 2D world");

		Group root = new Group();
		Scene scene = new Scene(root, window_width, window_height);

		primaryStage.setScene(scene);
		
		Group g = new Group();

		// setting up the world;
		
		// creating polygon as obstacles;
		

		double a[][] = {{360, 140}, {400, 200}, {450, 110}};
		Poly obstacle1 = new Poly(a);
		
		double b[][] = {{0, 0}, {300, 3}, {600,0}};

		Poly obstacle2 = new Poly(b);
		

		
		double c[][] = {{110, 100},{220, 220},{250, 100}};
		Poly obstacle3 = new Poly(c);
		
		// Declaring a world; 
		World w = new World();
		// Add obstacles to the world;
		w.addObstacle(obstacle1);
		w.addObstacle(obstacle2);
		w.addObstacle(obstacle3);
		
		plotWorld(g, w);
		
		ArmRobot arm = new ArmRobot(2);
		
		double[] config1 = {300, 30, 80, Math.PI, 80, 0.1};
		double[] config2 = {300, 30, 80, Math.PI/2, 80, .2};
		arm.set(config1);
		//PRMProblem PRM= new PRMProblem(w, arm, config1, config2);
		//HashMap<double[],List<double[]>> prm = PRM.prmGenerator();
		PRMPlanner PRM= new PRMPlanner(w, arm, config1, config2);
		HashMap<ArrayList<Double>,List<ArrayList<Double>>> prm = PRM.prmGenerator();
		//List<double[]> path= PRM.astarSearch(prm);
		List<ArrayList<Double>> path= PRM.astarSearch(prm);
//		arm.set(config2);
//		
//		// Plan path between two configurations;
//		ArmLocalPlanner ap = new ArmLocalPlanner();
//		
//		// get the time to move from config1 to config2;
//		double time = ap.moveInParallel(config1, config2);
//		System.out.println(time);
//		
//		arm.set(config2);
//		
//		boolean result;
//		result = w.armCollisionPath(arm, config1, config2);
//		System.out.println(result);
//		// plot robot arm
		for(ArrayList<Double> node:path){
			double[] q= new double[node.size()];
			for(int t=0;t<node.size();t++)
				q[t]=node.get(t);
			plotArmRobot(g, arm, q);
			scene.setRoot(g);
		    primaryStage.show();
		    
		}
//		plotArmRobot(g, arm, config2);
//		plotArmRobot(g, arm, config1);    
		
//	    scene.setRoot(g);
//	    primaryStage.show();
		

	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
