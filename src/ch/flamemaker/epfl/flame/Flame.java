package ch.flamemaker.epfl.flame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class Flame
{

	private List<FlameTransformation> transformations;

	Flame( List<FlameTransformation> transformations )
	{
		this.transformations = new ArrayList<FlameTransformation>( transformations );
	}

	
	// TODO: Refactor this method as well as IFS.compute() into an abstract class
	//       parametrized by an object implementing the Builder interface? 
	public FlameAccumulator compute( Rectangle frame, int width, int height, int density )
	{
		FlameAccumulator.Builder builder = new FlameAccumulator.Builder( frame, width, height );
		
		Point p = new Point( 0, 0 );
		Random random = new Random();

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
