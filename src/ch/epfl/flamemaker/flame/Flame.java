package ch.epfl.flamemaker.flame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Compute a Flame fractal, defined by a list of Flame transformations,
 * in the given frame.
 */
public class Flame
{

	private final List<FlameTransformation> transformations;
	
	/**
	 * Create a new Flame with the given transformations.
	 * 
	 * @param transformations The Flame transformations to apply.
	 */
	Flame( List<FlameTransformation> transformations )
	{
		this.transformations = new ArrayList<FlameTransformation>( transformations );
	}

	/**
	 * Compute a colored Flame fractal, in the given frame, with the given size, and of the given density.
	 * 
	 * @param frame The frame in which the fractal must be contained.
	 * @param width The width of the fractal.
	 * @param height The height of the fractal.
	 * @param density The density of points.
	 * 
	 * @return An accumulator, holding the points hit list,
	 * 		   as well as the points' color indexes.
	 */
	public FlameAccumulator compute( Rectangle frame, int width, int height, int density )
	{
		FlameAccumulator.Builder builder = new FlameAccumulator.Builder( frame, width, height );

		Point p = Point.ORIGIN;
		double c = 0.0;
		Random random = new Random( 2013 );

		int n = this.transformations.size();

		double[] colorIndexes = Flame.colorIndexes( n );
		
		// Compute 20 points that we won't draw, to kick the computation off.
		for( int j = 0; j < 20; j++ )
		{
			int i = random.nextInt( n );
			p = this.transformations.get( i ).transformPoint( p );
			c = ( colorIndexes[ i ] + c ) / 2.0;
		}
		
		int numberOfPoints = density * width * height;

		for( int j = 0; j < numberOfPoints; j++ )
		{
			int i = random.nextInt( n );
			p = this.transformations.get( i ).transformPoint( p );
			c = ( colorIndexes[ i ] + c ) / 2.0;

			builder.hit( p, c );
		}

		return builder.build();
	}
	
	/**
	 * Compute the color indexes for each transformation, by their index.
	 * 
	 * @param n The number of color indexes to compute.
	 * @return An array of n color indexes.
	 */
	public static double[] colorIndexes( int n )
	{
		if( n <= 0 )
		{
			throw new IllegalArgumentException( "n must be greater or equal than 0." );
		}

		double[] indexes = new double[ n ];

		for( int i = 0; i < n; i++ )
		{
			// The color indexes for the first two transformations are, respectively, 0 and 1.
			if( i <= 1 )
			{
				indexes[ i ] = i;
				
				continue;
			}
			
			double denominator = Math.pow( 2, Math.ceil( Math.log( i ) / Math.log( 2 ) ) );
			double numerator = 1 + ( 2 * ( i - ( denominator / 2 ) - 1 ) );
			
			indexes[ i ] = numerator / denominator;
		}
		
		return indexes;
	}
	
	/**
	 * Build a Flame fractal progressively.
	 */
	public static class Builder
	{

		private final List<FlameTransformation.Builder> builders;

		/**
		 * Create a new builder that will hold the same transformations
		 * as the flame specified.
		 * 
		 * @param flame A flame
		 */
		public Builder( Flame flame )
		{
			this.builders = new ArrayList<FlameTransformation.Builder>();
			
			if( flame == null )
			{
				return;
			}
			
			// Create a transformation builder for each transformation in the given flame
			// so we can modify them afterwards.
			for( FlameTransformation transformation : flame.transformations )
			{
				this.builders.add( new FlameTransformation.Builder( transformation ) );
			}
		}

		/**
		 * Return the number of transformations holded by this builder
		 * 
		 * @return the number of transformations holded by this builder
		 */
		public int transformationCount()
		{
			return this.builders.size();
		}

		/**
		 * Add a transformation to the builder, wrapped in a FlameTransformation.Builder.
		 * 
		 * @param transformation A transformation to add to the builder
		 */
		public void addTransformation( FlameTransformation transformation )
		{
			this.builders.add( new FlameTransformation.Builder( transformation ) );
		}

		/**
		 * Get the affine transformation at the given index.
		 *  
		 * @param index The index of the transformation to get
		 * @return the affine transformation at the given index.
		 */
		public AffineTransformation affineTransformation( int index )
		{
			if( index < 0 || index >= this.transformationCount() )
			{
				throw new IndexOutOfBoundsException( "Given index is out of bounds." );
			}

			return this.builders.get( index ).affineTransformation();
		}
		
		/**
		 * Set the affine transformation at the given index.
		 * 
		 * @param index
		 * @param transformation
		 */
		public void setAffineTransformation( int index, AffineTransformation transformation )
		{
			if( index < 0 || index >= this.transformationCount() )
			{
				throw new IndexOutOfBoundsException( "Given index is out of bounds." );
			}

			this.builders.get( index ).setAffineTransformation( transformation );
		}
		
		/**
		 * Get the weight of the specified variation of the transformation at the given index.
		 * 
		 * @param index The index of the transformation
		 * @param variation The variation to get the weight from
		 * @return The weight of the variation
		 */
		public double variationWeight( int index, Variation variation )
		{
			if( index < 0 || index >= this.transformationCount() )
			{
				throw new IndexOutOfBoundsException( "Given index is out of bounds." );
			}

			return this.builders.get( index ).variationWeight( variation );
		}
		
		/**
		 * Set the weight of the specified variation of the transformation at the given index.
		 * @param index
		 * @param variation
		 * @param weight
		 */
		public void setVariationWeight( int index, Variation variation, double weight )
		{
			if( index < 0 || index >= this.transformationCount() )
			{
				throw new IndexOutOfBoundsException( "Given index is out of bounds." );
			}

			this.builders.get( index ).setVariationWeight( variation, weight );
		}

		/**
		 * Remove the transformation at the given index.
		 * 
		 * @param index
		 */
		public void removeTransformation( int index )
		{
			if( index < 0 || index >= this.transformationCount() )
			{
				throw new IndexOutOfBoundsException( "Given index is out of bounds." );
			}
			
			this.builders.remove( index );
		}
		
		/**
		 * Build the flame by building all the flame transformations and
		 * passing them to Flame's constructor.
		 * 
		 * @return A new Flame object.
		 */
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
