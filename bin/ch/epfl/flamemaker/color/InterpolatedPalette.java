package ch.epfl.flamemaker.color;

import java.util.Arrays;
import java.util.List;

public class InterpolatedPalette implements Palette
{
	
	public static InterpolatedPalette RGB_PALETTE = new InterpolatedPalette( Arrays.asList(
		Color.RED, Color.GREEN, Color.BLUE
	) );
	
	private List<Color> colors;
	
	public InterpolatedPalette( List<Color> colors )
	{
		if( colors.size() < 2 )
		{
			throw new IllegalArgumentException( "The colors list must contain at least 2 colors." );
		}
		
		this.colors = colors;
	}
	
	@Override
	public Color colorForIndex( double index )
	{
		if( index < 0.0 || index > 1.0 )
		{
			throw new IllegalArgumentException( "index should be between 0 and 1." );
		}
		
		double colorIndex = index * ( colors.size() - 1 );
		double flooredIndex = Math.floor( colorIndex );
		
		if( colorIndex == flooredIndex )
		{
			return this.colors.get( ( int )colorIndex );
		}
		
		double p = colorIndex - flooredIndex;
		
		Color first = colors.get( ( int )flooredIndex );
		Color second = colors.get( ( int )flooredIndex + 1 );
		
		return first.mixWith( second, p );
	}
	
}
