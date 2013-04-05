package ch.epfl.flamemaker.color;

import ch.epfl.flamemaker.util.Interval;

public class Color
{
	
	public static Color BLACK = new Color( 0, 0, 0 );
	public static Color WHITE = new Color( 1, 1, 1 );
	public static Color RED   = new Color( 1, 0, 0 );
	public static Color GREEN = new Color( 0, 1, 0 );
	public static Color BLUE  = new Color( 0, 0, 1 );
	
	public static int sRGBEncode( double v, int max )
	{
		double c = ( v <= 0.0031308 )
				   ? 12.92 * v
				   : 1.055 * Math.pow( v, 1 / 2.4 ) - 0.055;
		
		return ( int )Math.floor( c * max );
	}
	
	private double r;
	private double g;
	private double b;
	
	Color( double r, double g, double b )
	{
		if( !Interval.contains( r, 0, 1 ) || !Interval.contains( g, 0, 1 ) || !Interval.contains( b, 0, 1 ) )
		{
			throw new IllegalArgumentException(
				"Color components must be between 0 and 1."
			);
		}
		
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public double red()
	{
		return this.r;
	}
	
	public double green()
	{
		return this.g;
	}
	
	public double blue()
	{
		return this.b;
	}
	
	public Color mixWith( Color that, double proportion )
	{
		if( !Interval.contains( proportion, 0, 1 ) )
		{
			throw new IllegalArgumentException( "proportion must be between 0 and 1." ); 
		}
		
		double p = proportion;
		double c = 1 - p;
		
		return new Color(
			p * this.r + c * that.r,
			p * this.g + c * that.g,
			p * this.b + c * that.b
		);
	}
	
	public int asPackedRGB()
	{
		int sR = Color.sRGBEncode(  this.r, 255 );
		int sG = Color.sRGBEncode(  this.g, 255 );
		int sB = Color.sRGBEncode(  this.b, 255 );
		
		return sR << 0 | sG << 16 | sB << 24;
	}
	
}