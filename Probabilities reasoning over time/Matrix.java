package assignment;

public class Matrix {
	public double [][] matrix;
//	int size;
	public Matrix() {}
	public Matrix(int r, int c) {
		//this.size=s;
		matrix = new double[r][c];
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s="";
		for(int i=matrix.length-1;i>=0;i--){
			for(int j=0;j<matrix[0].length;j++)
				s+=matrix[i][j]+" ";
			s+="\n";
		}
		return s;
	}
	public void printResult(int mazesize){
		String s="----------------------------\n";
		for(int i=mazesize-1;i>=0;i--){
			s+="|";
			for(int j=0;j<mazesize;j++)
				//http://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
				s+=(double)Math.round(matrix[i*mazesize+j][0] * 10000) / 10000+"|";
			s+="\n----------------------------\n";
		}
		System.out.print(s);
	}
}
