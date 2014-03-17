package assignment;

import java.util.ArrayList;

public class ProbabilisticReasoning {
	
	int mazesize= 4;
	int matrixsize = mazesize*mazesize;
	Transition tran;
	ArrayList<Sensor> bigOs = new ArrayList<Sensor>();
	double sensorright=0.88;
	double sensorwrong=0.04;
	Maze maze;
	
	public ProbabilisticReasoning(Maze m){
		maze=m ;
		//Initialization of Observation matrix O, and put them into List bigOs
		for(int c=0;c<Maze.colorcode.length;c++){
			Sensor obs = new Sensor(matrixsize);
			for(int i=0;i<m.height;i++)
				for(int j=0;j<m.width;j++){
					int k=i*mazesize+j;
					if(m.colorcoded[i][j]==c)
						obs.matrix[k][k]=sensorright;
					else
						obs.matrix[k][k]=sensorwrong;
				}
			bigOs.add(obs);
		}
		//Initialization of Transition matrix tran
		tran = new Transition(matrixsize);	
		for(int i=0;i<mazesize;i++){
			for(int j=0;j<mazesize;j++){
				int[] neibour = new int[4];
				int ncount=0;
				//check neighbor[i][j-1]
				if(m.isLegal(j-1,i)){
					neibour[0]=i*mazesize+j-1;
					ncount++;
				}
				else
					neibour[0]=-1;
				//check neighbor[i-1][j]
				if(m.isLegal(j,i-1)){
					neibour[1]=(i-1)*mazesize+j;
					ncount++;
				}
				else
					neibour[1]=-1;
				//check neighbor[i][j+1]
				if(m.isLegal(j+1,i)){
					neibour[2]=i*mazesize+j+1;
					ncount++;
				}
				else
					neibour[2]=-1;
				//check neighbor[i+1][j]
				if(m.isLegal(j,i+1)){
					neibour[3]=(i+1)*mazesize+j;
					ncount++;
				}
				else
					neibour[3]=-1;
				//count the neighbors and assign different value to the original square
				if(ncount==1)
					tran.matrix[i*mazesize+j][i*mazesize+j]=0.75;
				else if(ncount==2)
					tran.matrix[i*mazesize+j][i*mazesize+j]=0.5;
				else if(ncount==3)
					tran.matrix[i*mazesize+j][i*mazesize+j]=0.25;
				else
					tran.matrix[i*mazesize+j][i*mazesize+j]=0;
				
				for(int t:neibour){
					if(t!=-1)
						tran.matrix[i*mazesize+j][t]=0.25;
				}
			}
		}
	}
	
	public double[][] ProbFiltering(int[] sense){			
		ArrayList<Matrix> O_T= new ArrayList<Matrix>();
		Matrix trantrsps= new Matrix(tran.size, tran.size);
		//transpose of transition matrix, trantrsps[i][j]=tran[j][i]		
		for(int i=0; i<tran.size;i++)
			for(int j=0;j<tran.size;j++)
				trantrsps.matrix[i][j]=tran.matrix[j][i];
		//calculate O * T transpose and store them in O_T
		for(Sensor obs: bigOs){
			Matrix ot = new Matrix(obs.matrix.length, tran.matrix[0].length);
			ot=Multiply(obs, trantrsps);
			O_T.add(ot);
		}
		//calculating f1, here first = P(X1) 
		Matrix first = new Matrix(matrixsize,1);
		for(int i=0; i<mazesize;i++)
			for(int j=0;j<mazesize;j++){
				if(maze.isLegal(j, i))
					first.matrix[i*mazesize+j][0]=(double)1/(matrixsize-maze.stars);
				else 
					first.matrix[i*mazesize+j][0]=0;
			}
//		Matrix PX1 = new Matrix(matrixsize,1);
//		PX1 = Multiply(trantrsps, first);
//		first.printResult(mazesize);
//		PX1.printResult(mazesize);
		Matrix f1 =Multiply(bigOs.get(sense[0]), first);
		normalize(f1.matrix);
		System.out.println("f1");
		f1.printResult(mazesize);

		Matrix f2 =new Matrix(matrixsize, 1);
		for(int s=1;s<sense.length;s++){
			f2 = Multiply(O_T.get(sense[s]),f1);
			normalize(f2.matrix);
			System.out.println("f 1:"+(s+1));
			f2.printResult(mazesize);
			f1=f2;
		}
		return f2.matrix;			
	}
	
	private void normalize(double[][] m) {
		// TODO Auto-generated method stub
		double sum=0;
		for(int i=0;i<m.length;i++)
			for(int j=0;j<m[0].length;j++)
				sum+=m[i][j];
		for(int i=0;i<m.length;i++)
			for(int j=0;j<m[0].length;j++)
				m[i][j]=(double)m[i][j]/sum;
	}

	//refer to: http://stackoverflow.com/questions/17623876/matrix-multiplication-using-arrays
	private Matrix Multiply(Matrix A, Matrix B){
			int aRows = A.matrix.length;
	        int aColumns = A.matrix[0].length;
	        int bRows = B.matrix.length;
	        int bColumns = B.matrix[0].length;
	        
	        Matrix C = new Matrix(aRows,bColumns);
	        	
	        if (aColumns != bRows) {
	            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
	        }

	        for (int i = 0; i < aRows; i++) { // aRow
	            for (int j = 0; j < bColumns; j++) { // bColumn
	                for (int k = 0; k < aColumns; k++) { // aColumn
	                    C.matrix[i][j] += A.matrix[i][k] * B.matrix[k][j];
	                }
	            }
	        }
	        return C;		
		}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Maze m = Maze.readFromFile("simple.maz","color.maz");
		ProbabilisticReasoning pr = new ProbabilisticReasoning(m);
		char[] sense = {'r','g','b','y'};
		int[] sensecode = new int[sense.length];
		int i=0;
		for(char c:sense){
			sensecode[i]=Maze.colorcode(c);
			i++;
		}
		double[][] result= pr.ProbFiltering(sensecode);
	}

}
