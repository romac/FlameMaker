package ch.epfl.flamemaker.color;

import java.util.Random;

/**
 * Represents a RGB color.
 */
public class Color
{
	
	public static final Color BLACK = new Color( 0, 0, 0 );
	public static final Color WHITE = new Color( 1, 1, 1 );
	public static final Color RED   = new Color( 1, 0, 0 );
	public static final Color GREEN = new Color( 0, 1, 0 );
	public static final Color BLUE  = new Color( 0, 0, 1 );
	
	private double r;
	private double g;
	private double b;
	
	/**
	 * Create an new color with the given RGB components.
	 * All components must be between 0.0 and 1.0.
	 * 
	 * @param r The red component.
	 * @param g The green component.
	 * @param b The blue component.
	 */
	Color( double r, double g, double b )
	{
		if( r < 0 || r > 1 || g < 0 || g > 1 || b < 0 || b > 1 )
		{
			throw new IllegalArgumentException(
				"Color components must be between 0 and 1."
			);
		}
		
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * @return The red component of this color.
	 */
	public double red()
	{
		return this.r;
	}
	
	/**
	 * @return The green component of this color.
	 */
	public double green()
	{
		return this.g;
	}
	
	/**
	 * @return The blue component of this color.
	 */
	public double blue()
	{
		return this.b;
	}
	
	/**
	 * Mix this color with another, in the given proportion.
	 * 
	 * @param that The other color to mix this one with.
	 * @param proportion The proportion of this (not that) color.
	 * @return A mixed color.
	 */
	public Color mixWith( Color that, double proportion )
	{
		if( proportion < 0 || proportion > 1 )
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
	
	/**
	 * Pack this color into an integer.
	 * 
	 * @return The packed color as an integer.
	 */
	public int asPackedRGB()
	{
		// Gamma-encode the colors components on 8 bits.
		int sR = Color.sRGBEncode(  this.r, 255 );
		int sG = Color.sRGBEncode(  this.g, 255 );
		int sB = Color.sRGBEncode(  this.b, 255 );
		
		// Pack the three components into a single integer, one after the other.
		return sR << 8 | sG << 16 | sB << 24;
	}
	
	
	/**
	 * Format this color.
	 * 
	 * @return A string representing this RGB color, with 255 as the max value.
	 */
	@Override
	public String toString()
	{
		return String.format(
			"( %d, %d, %d )",
			Color.sRGBEncode(  this.r, 255 ),
			Color.sRGBEncode(  this.g, 255 ),
			Color.sRGBEncode(  this.b, 255 )
		);
	}
	
	/**
	 * Return a random RGB color.
	 * 
	 * @return A random color.
	 */
	public static Color random()
	{
		Random random = new Random( 2013 );
		
		return new Color( random.nextDouble(), random.nextDouble(), random.nextDouble() );
	}
	
	/**
	 * Gamma-encode a real color value into an integer using the sRGB formula.
	 * 
	 * @param v The real color value.
	 * @param max The maximum integer value for the color.
	 * @return The gamma-encoded color value.
	 */
	public static int sRGBEncode( double v, int max )
	{
		double c = ( v <= 0.0031308 )
				   ? 12.92 * v
				   : 1.055 * Math.pow( v, 1 / 2.4 ) - 0.055;
		
		return ( int )Math.floor( c * max );
	}
	
}