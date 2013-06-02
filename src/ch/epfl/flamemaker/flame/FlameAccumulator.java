package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.util.Arrays2D;

/**
 * An accumulator holdings a Flame fractal's points hit list
 * as well as their color indexes.
 * 
 * Such an accumulator must be built progressively
 * using the {@link FlameAccumulator.Builder} class. 
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public final class FlameAccumulator
{
	
	/**
	 * A 2-dimensional array holding the hit count for each point.
	 */
	private final int[][] hitCount;
	
	/**
	 * A 2-dimensional array holding the color index for each point.
	 */
	private final double[][] colorIndexSum;
	
	/**
	 * The intensity denominator.
	 */
	private final double intensityDenominator;

	/**
	 * Create a new accumulator.
	 * 
	 * @param hitCount
	 * @param colorIndexSum
	 */
	private FlameAccumulator( int[][] hitCount, double[][] colorIndexSum, double intensityDenominator )
	{
		this.hitCount = Arrays2D.copyOf2DArray( hitCount );
		this.colorIndexSum = Arrays2D.copyOf2DArray( colorIndexSum );
		this.intensityDenominator = intensityDenominator;
	}
	
	/**
	 * @return The height of the accumulator
	 */
	public int width()
	{
		return this.hitCount.length;
	}

	/**
	 * @return The width of the accumulator
	 */
	public int height()
	{
		return this.hitCount[ 0 ].length;
	}
	
	/**
	 * Compute the intensity at which the point at the given position has been hit.
	 * 
	 * @param x The x coordinate of the point.
	 * @param y The y coordinate of the point.
	 * @return The intensity at which the point has been hit.
	 */
	public double intensity( int x, int y )
	{
		if( x < 0 || x > this.width() - 1 )
		{
			throw new IndexOutOfBoundsException( "x (" + x  + ") is out of bounds." );
		}

		if( y < 0 || y > this.height() - 1 )
		{
			throw new IndexOutOfBoundsException( "y (" + y  + ") is out of bounds." );
		}

		return Math.log( this.hitCount[ x ][ y ] + 1 ) / this.intensityDenominator;
	}
	
	/**
	 * Compute the color of the point at the given position,
	 * using the given palette and background color.
	 * 
	 * @param palette The color palette to use
	 * @param background The background color
	 * @param x The x coordinate of the point.
	 * @param y The y coordinate of the point.
	 * @return The color of the point
	 */
	public Color color( Palette palette, Color background, int x, int y )
	{
		if( x < 0 || x > this.width() - 1 )
		{
			throw new IndexOutOfBoundsException( "x (" + x  + ") is out of bounds." );
		}

		if( y < 0 || y > this.height() - 1 )
		{
			throw new IndexOutOfBoundsException( "y (" + y  + ") is out of bounds." );
		}
		
		// If the point hasn't been hit, we just return the background color,
		// for efficiency and to avoid a division by zero in the next step.
		if( this.hitCount[ x ][ y ] == 0.0 )
		{
			return background;
		}
		
		double colorIndex = this.colorIndexSum[ x ][ y ] / this.hitCount[ x ][ y ];
		
		// We get the color at the index, and mix it with the background color,
		// proportionnaly to the point's intensity.
		return palette.colorForIndex( colorIndex ).mixWith( background, this.intensity( x, y ) );
	}
	
	/**
	 * Build a Flame accumulator progressively, using the {@link #hit( Point, double )} method.
	 */
	public static class Builder
	{
		private Rectangle frame;
		private int width;
		private int height;
		private int[][] hitCount;
		private double[][] colorIndexSum;
		private AffineTransformation transformation;
		
		private int maxHitCount = 0;

		public Builder( Rectangle frame, int width, int height )
		{
			if( width < 1 )
			{
				throw new IllegalArgumentException( "width should be strictly positive." );
			}

			if( height < 1 )
			{
				throw new IllegalArgumentException( "height should be strictly positive." );
			}

			this.frame = frame;
			this.width = width;
			this.height = height;
			this.hitCount = new int[ width ][ height ];
			this.colorIndexSum = new double[ width ][ height ];
			
			// Since hit points are contained in a frame whose origin is is not necessarily at ( 0, 0 ),
			// and which might now be as wide or high as the output image,
			// we need to translate and scale the point from their coordinates in the plan
			// to pixel coordinates in the bitmap.
			// We do this using an affine transformation that we'll use in {@link #hit( Point, double )}.
			AffineTransformation scaling = AffineTransformation.newScaling( ( double )this.width / this.frame.width(), ( double )this.height / this.frame.height() ); 
			AffineTransformation translation = AffineTransformation.newTranslation( -this.frame.left(), -this.frame.bottom() );
			
			this.transformation = scaling.composeWith( translation );
		}
		
		/**
		 * Hit the point p, with the given color index.
		 *
		 * @param p The point to hit.
		 * @param c The color index of that point.
		 */
		public void hit( Point p, double c )
		{
			if( !this.frame.contains( p ) )
			{
				return;
			}
			
			// See the comment in {@link #Builder( Rectangle, int, int )} for a detailed
			// explanation of why we need to transform that point.
			p = this.transformation.transformPoint( p );

			int x = ( int )Math.floor( p.x() );
			int y = ( int )Math.floor( p.y() );

			this.hitCount[ x ][ y ] += 1;
			this.colorIndexSum[ x ][ y ] += c;
			
			if( this.hitCount[ x ][ y ] > this.maxHitCount )
			{
				this.maxHitCount = this.hitCount[ x ][ y ];
			}
		}
		
		/**
		 * Build the Flame accumulator.
		 */
		public FlameAccumulator build()
		{
			FlameAccumulator acc = new FlameAccumulator( this.hitCount, this.colorIndexSum, Math.log( this.maxHitCount + 1 ) );
			
			return acc;
		}
	}
}
