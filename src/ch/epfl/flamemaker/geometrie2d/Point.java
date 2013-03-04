/*  *	Author:      Moi  *	Date:        Aujourd'hui  */
package ch.epfl.flamemaker.geometrie2d;

public class Point {
	private final double x;
	private final double y;

	Point (double a, double b){
		 x = a;
		 y = b;
	}
	public double x(){
		return x;
	}
	
	public double y (){
		return y;
	}
	public double r(){
		double d = Math.sqrt(x*x + y*y);
		return d;
	}
	public double theta(){
		double a = x();
		double b =  y();
		
		double theta = Math.atan2(a, b);
		return theta;
		
	}
	public String toString(){
		String s = null;
		double a = x();
		double b = y();
		 s = "(" + a + "," + b + ")";
		 return s;
	}
}
