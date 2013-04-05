package ch.epfl.flamemaker.flame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class Flame
{
	
	public static double[] colorIndexes( int n )
	{
		double[] indexes = new double[ n ];
		
		for( int i = 0; i < n; i++ )
		{
			if( n <= 1 ) {
				indexes[ i ] = n;
				continue;
			}
			
			double denominator = Math.pow( 2, Math.ceil( Math.log( i ) / Math.log( 2 ) ) );
			double numerator   = 1 + ( 2 * ( i - ( denominator / 2 ) - 1 ) ); 
			
			indexes[ i ] = numerator / denominator;
		}
		
		return indexes;
	}
	
	private final List<FlameTransformation> transformations;

	Flame( List<FlameTransformation> transformations )
	{
		this.transformations = new ArrayList<FlameTransformation>( transformations );
	}

	
	// TODO: Refactor this method as well as IFS.compute() into an abstract class
	//       parametrized by an object implementing the Builder interface? 
	public FlameAccumulator compute( Rectangle frame, int width, int height, int density )
	{
		FlameAccumulator.Builder builder = new FlameAccumulator.Builder( frame, width, height );
		
		Point p = Point.ORIGIN;
		Random random = new Random( 2013 );

		int n = this.transformations.size();
		
		for( int j = 0; j < 20; j++ )
		{
			int i = random.nextInt( n );
			p = this.transformations.get( i ).transformPoint( p );
		}

		int iterations = density * width * height;
		
		for( int j = 0; j < iterations; j++ )
		{
			int i = random.nextInt( n );
			p = this.transformations.get( i ).transformPoint( p );
			builder.hit( p );
		}
		
		return builder.build();
	}

}
