package ch.epfl.flamemaker.flame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.util.Interval;

public class Flame
{

	private final List<FlameTransformation> transformations;

	Flame( List<FlameTransformation> transformations )
	{
		this.transformations = new ArrayList<FlameTransformation>( transformations );
	}

	public FlameAccumulator compute( Rectangle frame, int width, int height, int density )
	{
		FlameAccumulator.Builder builder = new FlameAccumulator.Builder( frame, width, height );

		Point p = Point.ORIGIN;
		Random random = new Random( 2013 );

		int n = this.transformations.size();
		double c = 0.0;

		double[] colorIndexes = Flame.colorIndexes( n );

		for( int j = 0; j < 20; j++ )
		{
			int i = random.nextInt( n );
			p = this.transformations.get( i ).transformPoint( p );
			c = ( colorIndexes[ j ] + c ) / 2.0;
		}

		int iterations = density * width * height;

		for( int j = 0; j < iterations; j++ )
		{
			int i = random.nextInt( n );
			p = this.transformations.get( i ).transformPoint( p );
			c = ( colorIndexes[ j ] + c ) / 2.0;

			builder.hit( p, c );
		}

		return builder.build();
	}

	public static double[] colorIndexes( int n )
	{
		if( !Interval.contains( n, 0, Integer.MAX_VALUE ) )
		{
			throw new IllegalArgumentException( "n must be greater or equal than 0." );
		}

		double[] indexes = new double[ n ];

		for( int i = 0; i < n; i++ )
		{
			if( n <= 1 )
			{
				indexes[ i ] = n;
				continue;
			}

			double denominator = Math.pow( 2, Math.ceil( Math.log( i ) / Math.log( 2 ) ) );
			double numerator = 1 + ( 2 * ( i - ( denominator / 2 ) - 1 ) );

			indexes[ i ] = numerator / denominator;
		}

		return indexes;
	}

	public static class Builder
	{

		private Flame flame;
		private final List<FlameTransformation.Builder> builders;

		public Builder( Flame flame )
		{
			this.flame = flame;
			this.builders = new ArrayList<FlameTransformation.Builder>();

			for( FlameTransformation transformation : flame.transformations )
			{
				this.builders.add( new FlameTransformation.Builder( transformation ) );
			}
		}

		public int transformationCount()
		{
			return this.builders.size();
		}

		public void addTransformation( FlameTransformation transformation )
		{
			this.builders.add( new FlameTransformation.Builder( transformation ) );
		}

		public AffineTransformation affineTransformation( int index )
		{
			if( index < 0 || index >= this.transformationCount() )
			{
				throw new IndexOutOfBoundsException( "Given index is out of bounds." );
			}

			return this.builders.get( index ).affineTransformation();
		}

		public void setAffineTransformation( int index, AffineTransformation transformation )
		{
			if( index < 0 || index >= this.transformationCount() )
			{
				throw new IndexOutOfBoundsException( "Given index is out of bounds." );
			}

			this.builders.get( index ).setAffineTransformation( transformation );
		}

		public double variationWeight( int index, Variation variation )
		{
			if( index < 0 || index >= this.transformationCount() )
			{
				throw new IndexOutOfBoundsException( "Given index is out of bounds." );
			}

			return this.builders.get( index ).variationWeight( variation );
		}

		public void setVariationWeight( int index, Variation variation, double weight )
		{
			if( index < 0 || index >= this.transformationCount() )
			{
				throw new IndexOutOfBoundsException( "Given index is out of bounds." );
			}

			this.builders.get( index ).setVariationWeight( variation, weight );
		}

		public void removeTransformation( int index )
		{
			if( index < 0 || index >= this.transformationCount() )
			{
				throw new IndexOutOfBoundsException( "Given index is out of bounds." );
			}
			
			this.builders.remove( index );
		}
		
		public Flame build()
		{
			List<FlameTransformation> transformations = new ArrayList<FlameTransformation>();
			
			for( FlameTransformation.Builder builder : this.builders )
			{
				transformations.add( builder.build() );
			}
			
			return new Flame( transformations );
		}

	}

}
