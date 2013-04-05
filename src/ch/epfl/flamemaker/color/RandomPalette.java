package ch.epfl.flamemaker.color;

import java.util.ArrayList;

public class RandomPalette implements Palette
{

	private InterpolatedPalette interpolatedPalette;
	
	public RandomPalette( int n )
	{
		if( n < 2 )
		{
			throw new IllegalArgumentException( "n must be greater or equal to 2." );
		}
		
		ArrayList<Color> colors = new ArrayList<Color>();
		
		for( int i = 0; i < n; i++ )
		{
			colors.add( Color.randomColor() );
		}
		
		this.interpolatedPalette = new InterpolatedPalette( colors );
	}
	
	@Override
	public Color colorForIndex( double index )
	{
		return this.interpolatedPalette.colorForIndex( index );
	}

}
