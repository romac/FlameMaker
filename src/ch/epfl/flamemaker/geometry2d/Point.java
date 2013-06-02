package ch.epfl.flamemaker.geometry2d;

/**
 * Represent a point in a 2D coordinates system.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
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
	
	public Point scale( double factor )
	{
		return new Point( this.x * factor, this.y * factor );
	}
	
	public Point add( Point other )
	{
		return new Point( this.x + other.x, this.y + other.y );
	}
	
	public String toString()
	{
		return "( " + this.x + ", " + this.y + " )";
	}
	
}
