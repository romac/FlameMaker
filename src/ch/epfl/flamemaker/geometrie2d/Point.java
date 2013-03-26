package ch.epfl.flamemaker.geometrie2d;

public class Point
{
	
	private double x;
	private double y;
	
	public static Point ORIGIN = new Point( 0, 0 );
	
	public Point( double x, double y )
	{
		this.x = x;
		this.y = y;
	}
	
	public double x()
	{
		return this.x;
	}
	
	public double y()
	{
		return this.y;
	}
	
	public double r()
	{
		return Math.sqrt( x * x + y * y );
	}
	
	public double theta()
	{
		return Math.atan2( this.x, this.y );
	}
	
	public String toString()
	{
		return "(" + this.x + " , " + this.y + ")";
	}
	
}
