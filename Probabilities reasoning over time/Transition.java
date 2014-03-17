package assignment;

public class Transition extends Matrix{
	public int size;
	public Transition(int s) {
		this.size=s;
		matrix = new double[size][size];
		for(int i=0;i<size;i++)
			for(int j=0;j<size;j++)
				matrix[i][j]=0;
	}
}
