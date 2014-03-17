package assignment;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Maze {
	final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public int width;
	public int height;
	public static char[] colorcode={'r','g','b','y'};
	
	private char[][] grid;
	private char[][] color;
	public int[][] colorcoded;
	public int stars=0;
	
	
	public static Maze readFromFile(String filename, String colorfile) {
		Maze m = new Maze();

		try {
			List<String> lines = readFile(filename);
			List<String> colorlines = readFile(colorfile);			
			m.height = lines.size();

			int y = m.height-1;
			m.grid = new char[m.height][];
			
			for (String line : lines) {
				m.width = line.length();
				m.grid[y] = new char[m.width];				
				for (int x = 0; x < line.length(); x++) {
					m.grid[y][x] = line.charAt(x);
				}
				y--;
			}
			y = m.height-1;
			m.color = new char[m.height][];
			for (String line : colorlines) {
				m.width = line.length();
				m.color[ y ] = new char[m.width];
				for (int x = 0; x < line.length(); x++) {
					//  we read from file into array:
					m.color[y ][x] = line.charAt(x);
				}
				y--;
			}
			y = m.height-1;
			m.colorcoded = new int[m.height][];
			for (String line : colorlines) {
				m.width = line.length();
				m.colorcoded[y] = new int[m.width];
				for (int x = 0; x < line.length(); x++) {
					//  we read from file into array:
					m.colorcoded[y][x] = colorcode(line.charAt(x));
				}
				y--;
			}
			return m;
		} catch (IOException E) {
			return null;
		}
	}

	public static int colorcode(char ch) {
		// TODO Auto-generated method stub
		for(int i=0; i<4;i++){
			if(colorcode[i]==ch)
				return i;
		}
		return -1;
	}

	private static List<String> readFile(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		return Files.readAllLines(path, ENCODING);
	}

	public char getChar(int x, int y) {
		return grid[y][x];
	}
	
	// is the location x, y on the map, and also a legal floor tile (not a wall)?
	public boolean isLegal(int x, int y) {
		// on the map
		if(x >= 0 && x < width && y >= 0 && y < height) {
			// and it's a floor tile, not a wall tile:
			return getChar(x, y) == '.';
		}
		return false;
	}
	
	public String toString() {
		String s = "";
		for (int y = height-1; y>=0; y--) {
			for (int x = 0; x < width; x++) {
				s += color[y][x]+" ";
			}
			s += "\n";
		}
		return s;
	}
	
	public static void main(String args[]) {
		Maze m = Maze.readFromFile("simple.maz","color.maz");
		System.out.println(m);
		String s = "";
		for(int i=m.height-1;i>=0;i--){
			for(int j=0;j<m.width;j++)
				s+=m.colorcoded[i][j]+" ";
			s+="\n";
		}
		System.out.print(s);
		
	}

}
