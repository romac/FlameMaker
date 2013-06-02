package ch.epfl.flamemaker.ifs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Represents an IFS transformation.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public class IFS
{
	private List<AffineTransformation> transformations;

	IFS( List<AffineTransformation> transformations )
	{
		this.transformations = new ArrayList<AffineTransformation>( transformations );
	}

	public IFSAccumulator compute( Rectangle frame, int width, int height, int density )
	{
		IFSAccumulatorBuilder builder = new IFSAccumulatorBuilder( frame,
		        width, height );

		Point p = new Point( 0, 0 );
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
