package ch.epfl.flamemaker.color;

import java.util.ArrayList;

/**
 * Represents a color palette that interpolates between
 * a given number of random colors. 
 */
public class RandomPalette implements Palette
{

	private InterpolatedPalette interpolatedPalette;
	
	/**
	 * Create a new random palette that interpolates between n random colors.
	 * 
	 * @param n The number of colors to interpolate between.
	 */
	public RandomPalette( int n )
	{
		if( n < 2 )
		{
			throw new IllegalArgumentException( "n must be greater or equal to 2." );
		}
		
		ArrayList<Color> colors = new ArrayList<Color>();
		
		for( int i = 0; i < n; i++ )
		{
			colors.add( Color.random() );
		}
		
		// Use an {@link InterpolatedPalette} to interpolate
		// between those random colors.
		this.interpolatedPalette = new InterpolatedPalette( colors );
	}
	
	/**
	 * @see InterpolatedPalette#colorForIndex( double )
	 */
	@Override
	public Color colorForIndex( double index )
	{
		return this.interpolatedPalette.colorForIndex( index );
	}

}
