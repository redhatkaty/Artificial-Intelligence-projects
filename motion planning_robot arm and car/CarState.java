package assignment_robots;

// this class declares the configuration of a car robot;
// standard set and get function;

public class CarState {
	protected double[] s;

	public CarState () {
		s = new double[3];
		s[0] = 0;
		s[1] = 0;
		s[2] = 0;
	}

	public CarState (double x, double y, double theta) {
		s = new double[3];
		s[0] = x;
		s[1] = y;
		s[2] = theta;
	}

	public void set(double x, double y, double theta) {
		s[0] = x;
		s[1] = y;
		s[2] = theta;
		
	}

	public double getX() {
		return s[0];
	}

	public double getY() {
		return s[1];
	}

	public double getTheta() {
		return s[2];
	}

	public double[] get() {
		return s;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str=s[0]+" "+s[1]+" "+s[2]+" ";
		return str;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return s[0]== ((CarState)obj).getX()&&
			   s[1]== ((CarState)obj).getY()&&
			   s[2]== ((CarState)obj).getTheta();
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		
		return (int) (s[0]*10+s[1]*1+s[2]*100);
	}
}
