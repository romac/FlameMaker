package ch.epfl.flamemaker.color;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a color palette that interpolates between 2 or more colors.
 */
public class InterpolatedPalette implements Palette
{
	
	public static InterpolatedPalette RGB_PALETTE = new InterpolatedPalette(
		Arrays.asList( Color.RED, Color.GREEN, Color.BLUE )
	);
	
	private List<Color> colors;
	
	
	/**
	 * Create a new palette, which interpolates between the given colors.
	 * 
	 * @param colors A list of colors to interpolate between. 
	 */
	public InterpolatedPalette( List<Color> colors )
	{
		if( colors.size() < 2 )
		{
			throw new IllegalArgumentException( "The colors list must contain at least 2 colors." );
		}
		
		this.colors = colors;
	}
	
	/**
	 * Return the color associated with the given index,
	 * interpolated between this palette's colors.
	 *  
	 * @return A color
	 */
	@Override
	public Color colorForIndex( double index )
	{
		if( index < 0.0 || index > 1.0 )
		{
			throw new IllegalArgumentException( "index should be between 0 and 1." );
		}
		
		// Get a index between 0.0 and colors.size() - 1
		double colorIndex = index * ( colors.size() - 1 );
		
		// Get the previous color's index
		double flooredIndex = Math.floor( colorIndex );
		
		// Check if colorIndex was already a round number.
		if( colorIndex == flooredIndex )
		{
			// If so, we just return the color at that index.
			return this.colors.get( ( int )colorIndex );
		}
		
		// The proportion of this color is actually the difference
		// between the index and its rounded self, substracted to one.
		// Example:   colorIndex = 1.25
		// 			flooredIndex = 1.00
		//					   p = 0.75
		// Because if we're closed to Color #1, we should then add more of it to
		// the mix than the other.
		double p = 1 - ( colorIndex - flooredIndex ); 
		
		// Get the color at the floored index.
		Color first = colors.get( ( int )flooredIndex );
		// and the next one (which will always exists, since we've floored the index). 
		Color second = colors.get( ( int )flooredIndex + 1 );
		
		// Mix the two colors together
		return first.mixWith( second, p );
	}
	
}
