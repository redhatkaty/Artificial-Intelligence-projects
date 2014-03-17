package assignment_mazeworld;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Maze {
	final static Charset ENCODING = StandardCharsets.UTF_8;

	// A few useful constants to describe actions
	public static int[] STILL = {0,0};//can keep still
	public static int[] NORTH = {0, 1};
	public static int[] EAST = {1, 0};
	public static int[] SOUTH = {0, -1};
	public static int[] WEST = {-1, 0};
	
	
	public int width;
	public int height;
	
	private char[][] grid;

	public static Maze readFromFile(String filename) {
		Maze m = new Maze();

		try {
			List<String> lines = readFile(filename);
			m.height = lines.size();

			int y = 0;
			m.grid = new char[m.height][];
			for (String line : lines) {
				m.width = line.length();
				m.grid[m.height - y - 1] = new char[m.width];
				for (int x = 0; x < line.length(); x++) {
					// (0, 0) should be bottom left, so flip y as 
					//  we read from file into array:
					m.grid[m.height - y - 1][x] = line.charAt(x);
				}
				y++;

				// System.out.println(line.length());
			}

			return m;
		} catch (IOException E) {
			return null;
		}
	}

	private static List<String> readFile(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		return Files.readAllLines(path, ENCODING);
	}

	public char getChar(int x, int y) {
		return grid[y][x];
	}
	
	// is the location x, y on the map, and also a legal floor tile (not a wall)?
	public boolean isLegal(int[] xnew, int[] ynew, int[] xold, int[] yold) {//不要试图修改值
		// on the map
		int totalrobot=xnew.length;
		for(int r=0;r<totalrobot;r++){
			// check if it's in wall
			if(xnew[r] < 0 || 
				xnew[r] >=width || 
				ynew[r] < 0 ||
				ynew[r]>=height||
				(getChar(xnew[r], ynew[r]) == '#')) 				
				return false;
			for(int otherr=r+1;otherr<totalrobot;otherr++){
				//check if collide with other robot in new state
				if(xnew[r]==xnew[otherr]&&ynew[r]==ynew[otherr])
					return false;
				//check if collide with other robot in old state
				if(xnew[r]==xold[otherr]&&ynew[r]==yold[otherr])
					return false;
			}				
		}
		return true;		
	}
	
	
	public String toString() {
		String s = "";
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				s += grid[y][x];
			}
			s += "\n";
		}
		return s;
	}

	public static void main(String args[]) {
		Maze m = Maze.readFromFile("simple.maz");
		System.out.println(m);
	}

}
