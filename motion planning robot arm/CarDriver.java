package assignment_robots;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CarDriver extends Application {
	// default window size
	protected int window_width = 600;
	protected int window_height = 400;
	
	// Draw a polygon;
	public void addPolygon(Group g, Double[] points) {
		Polygon p = new Polygon();
	    p.getPoints().addAll(points);
	    
	    g.getChildren().add(p);
	}
	
	// plot a path
	public void plotpath(Group g, CarRobot car, CarState s) {
		//System.out.println(car);
		//System.out.println(s);
		car.set(s);
		double[][] current = car.getpath();
		Double[] to_add = new Double[2*current.length];
		for (int j = 0; j < current.length; j++) {
			System.out.println(current[j][0] + ", " + current[j][1]);
			to_add[2*j] = current[j][0];
			//to_add[2*j+1] = current[j][1];
			to_add[2*j+1] = window_height - current[j][1];
		}
		Polygon p = new Polygon();
		p.getPoints().addAll(to_add);
		
		p.setStroke(Color.BLUE);
		p.setFill(Color.BLUEVIOLET);
		g.getChildren().add(p);
	}
	// plot a car robot
	public void plotCarRobot(Group g, CarRobot car, CarState s) {
		//System.out.println(car);
		//System.out.println(s);
		car.set(s);
		double[][] current = car.get();
		Double[] to_add = new Double[2*current.length];
		for (int j = 0; j < current.length; j++) {
			System.out.println(current[j][0] + ", " + current[j][1]);
			to_add[2*j] = current[j][0];
			//to_add[2*j+1] = current[j][1];
			to_add[2*j+1] = window_height - current[j][1];
		}
		Polygon p = new Polygon();
		p.getPoints().addAll(to_add);
		
		p.setStroke(Color.RED);
		p.setFill(Color.PINK);
		g.getChildren().add(p);
	}	
	// plot the World with all the obstacles;
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
		
			
		primaryStage.setTitle("CS 76 2D world");
		Group root = new Group();
		Scene scene = new Scene(root, window_width, window_height);

		primaryStage.setScene(scene);
		
		Group g = new Group();
		
		double a[][] = {{0, 400}, {0.1, 400}, {0.1, 0}, {0, 0}};
		Poly obstacle1 = new Poly(a);
		
		double b[][] = {{0, 400}, {600, 400}, {600, 399.9}, {0, 399.9}};
		Poly obstacle2 = new Poly(b);
		
		double j[][] = {{0.1, 0.1}, {600, 0.1}, {600, 0}, {0.1, 0}};
		Poly obstacle8 = new Poly(j);
		
		double h[][] = { {599.9, 400},{600, 400}, {600, 0}, {599.9, 0}};
		Poly obstacle7 = new Poly(h);
		
		double c[][] = {{110, 220}, {250, 380}, {320, 220}};
		Poly obstacle3 = new Poly(c);
		
		double d[][] = {{380, 200}, {480, 200}, {480, 150}, {380, 150}};
		Poly obstacle4 = new Poly(d);

		double f[][] = {{0, 50}, {250, 50}, {250, 0}, {0, 0}};
		Poly obstacle6 = new Poly(f);
		
		double e[][] = {{300, 30}, {500, 30}, {500, 0}, {300, 0}};
		Poly obstacle5 = new Poly(e);
		
		
		
		// Declaring a world; 
		World w = new World();
		// Add obstacles to the world;
		w.addObstacle(obstacle1);
		w.addObstacle(obstacle2);
		w.addObstacle(obstacle3);
		w.addObstacle(obstacle4);
		w.addObstacle(obstacle5);
		w.addObstacle(obstacle6);	
		w.addObstacle(obstacle7);
		//w.addObstacle(obstacle8);
		plotWorld(g, w);
		
		CarRobot car = new CarRobot();
		
		CarState state1 = new CarState(270, 15, 0);
		CarState state2 = new CarState(400, 200, 0);
	    // Set CarState;
		car.set(state1);
		RRTPlanner rrt= new RRTPlanner(w, car, state1, state2, window_width, window_height);
		HashMap<CarState,ArrayList<CarState>> adjList = rrt.RRTGenerator();
//		boolean collided = w.carCollisionPath(car, state1, 0, 1.2);
//	    System.out.println(collided);
		
		List<CarState> visited= new ArrayList<CarState>();
		Iterator it = adjList.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        CarState node=new CarState();
	        List<CarState> adjs= new ArrayList<CarState>();
	        
	        node=(CarState)pairs.getKey();
	        //if(!visited.contains(node)){
	        	car.set(node);
		        plotpath(g, car, node);
	        	//plotCarRobot(g, car, node);
	        	scene.setRoot(g);
			    primaryStage.show();
		        visited.add(node);
	        //}
	        
		    adjs=(List<CarState>)pairs.getValue();
		    for(CarState aa: adjs){
//		    	Line line = new Line(node.getX(),node.getY(), aa.getX(),aa.getY());   
//		        line.setStrokeWidth(2); 
//		        line.setStroke(Color.BLACK); 
//		        g.getChildren().addAll(line);
		    	if(!visited.contains(aa)){
		        	car.set(aa);
			        plotpath(g, car, aa);
		        	//plotCarRobot(g, car, node);
			        
			        scene.setRoot(g);
				    primaryStage.show();
			        visited.add(aa);
		        }
		    }
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	    List<CarState> path = rrt.breadthFirstSearch(adjList);
	    for(CarState aa: path){
	    	
	        	car.set(aa);
		        plotCarRobot(g, car, aa);
		        
		        scene.setRoot(g);
			    primaryStage.show();
		        visited.add(aa);
	       
	    }
//	    List<CarState> explored= new ArrayList<CarState>();
//	    CarState next = new CarState();
//	    next.set(state1.getX(), state1.getY(), state1.getTheta());
//	    
//	    while(adjList.containsKey(next)
//	    		||next.equals(state2)){
//	    	
//	    	car.set(next);    	
//	        plotCarRobot(g, car, next);
//        	scene.setRoot(g);
//		    primaryStage.show();
//		    CarState ex= new CarState();
//		    ex.set(next.getX(), next.getY(), next.getTheta());
//		    explored.add(ex);
//		    
//		    next=adjList.get(next).get(0);
//		    int k=1;
//		  //next has been visited
//		    while(explored.contains(next)){
//		    	
//		    }
//		    		
//		    		adjList.get(ex).size()>k){//next has successor
//		    	 next=adjList.get(ex).get(k);
//		    	 k++;
//		    }
//		    
//	    }
	}
	public static void main(String[] args) {
		launch(args);
	}
}
