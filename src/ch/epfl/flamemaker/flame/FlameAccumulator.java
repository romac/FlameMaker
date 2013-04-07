package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.util.Arrays;

public class FlameAccumulator
{

	private int[][] hitCount;
	private double[][] colorIndexSum;
	private int maxCount;

	private FlameAccumulator( int[][] hitCount, double[][] colorIndexSum )
	{
		this.hitCount = Arrays.copyOf2DArray( hitCount );
		this.colorIndexSum = Arrays.copyOf2DArray( colorIndexSum );
		this.maxCount = Arrays.maxOf2DArray( hitCount );
	}

	public int width()
	{
		return this.hitCount.length;
	}

	public int height()
	{
		return this.hitCount[ 0 ].length;
	}

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

		return Math.log( this.hitCount[ x ][ y ] + 1 ) / Math.log( this.maxCount + 1 );
	}
	
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
		
		if( this.hitCount[ x ][ y ] == 0.0 )
		{
			return background;
		}
		
		return palette.colorForIndex( this.colorIndexSum[ x ][ y ] / this.hitCount[ x ][ y ] )
					  .mixWith( background, this.intensity( x, y ) );
	}

	public static class Builder
	{
		private Rectangle frame;
		private int width;
		private int height;
		private int[][] hitCount;
		private double[][] colorIndexSum;
		private AffineTransformation transformation;

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
			
			AffineTransformation scaling = AffineTransformation.newScaling( ( double )this.width / this.frame.width(), ( double )this.height / this.frame.height() ); 
			AffineTransformation translation = AffineTransformation.newTranslation( -this.frame.left(), -this.frame.bottom() );
			
			this.transformation = scaling.composeWith( translation );
		}

		public void hit( Point p, double c )
		{
			if( !this.frame.contains( p ) )
			{
				return;
			}

			p = this.transformation.transformPoint( p );

			int x = ( int )Math.floor( p.x() );
			int y = ( int )Math.floor( p.y() );

			this.hitCount[ x ][ y ] += 1;
			this.colorIndexSum[ x ][ y ] += c; 
		}

		public FlameAccumulator build()
		{
			return new FlameAccumulator( this.hitCount, this.colorIndexSum );
		}
	}
}
