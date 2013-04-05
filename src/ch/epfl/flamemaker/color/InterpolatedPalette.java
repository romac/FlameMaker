package ch.epfl.flamemaker.color;

import java.util.List;

public class InterpolatedPalette implements Palette
{
	
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
		double colorIndex = index * colors.size();
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
