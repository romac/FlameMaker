package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Transformation;

/**
 * A flame transformation, which is actually a weighted application of
 * {@link Variation}, each composed with the given
 * {@link ch.epfl.flamemaker.geometry2d.AffineTransformation}.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public class FlameTransformation implements Transformation
{
	
	private AffineTransformation affineTransformation;
	private double[] variationWeight;

	/**
	 * @return The identity transformation
	 */
	public static FlameTransformation IDENTITY()
	{
		return new FlameTransformation(
			AffineTransformation.IDENTITY(),
			new double[] { 1, 0, 0, 0, 0, 0 }
		);
	}
	
	/**
	 * Create a new flame transformation with the given affine transformation and variations weights.
	 * 
	 * @param affineTransformation The affine transformation,
	 * @param variationWeights The weights of the variations,
	 * 						   in the same order as they are defined in {@link Variation}.
	 */
	public FlameTransformation( AffineTransformation affineTransformation, double[] variationWeights )
	{
		if( variationWeights.length != Variation.ALL_VARIATIONS.size() )
		{
			throw new IllegalArgumentException(
				"variationWeight should be of length " + Variation.ALL_VARIATIONS.size() + "."
			);
		}
		
		this.affineTransformation = affineTransformation;
		this.variationWeight = variationWeights.clone();
	}

	/**
	 * Transform the given point by applying every known {@link Variation}
	 * to it, weighted by their weight, and composed with this transformation's
	 * affine transformation.
	 * 
	 * @see Transformation#transformPoint( Point )
	 */
	@Override
	public Point transformPoint( Point p )
	{
		Point r = Point.ORIGIN;
		
		for( int j = 0; j < Variation.ALL_VARIATIONS.size(); j++ )
		{
			double weight = this.variationWeight[ j ];
			
			// Don't apply this transformation if its weight is zero.
			if( weight == 0.0 )
			{
				continue;
			}
			
			Point g = this.affineTransformation.transformPoint( p );
			Point v = Variation.ALL_VARIATIONS.get( j ).transformPoint( g );
			Point f = v.scale( weight );
			
			r = r.add( f );
		}
		
		return r;
	}
	
	/**
	 * Clone this transformation
	 */
	@Override
	public FlameTransformation clone()
	{
		return new FlameTransformation( this.affineTransformation, this.variationWeight );
	}
	
	/**
	 * Build a Flame transformation progressively.
	 */
	public static class Builder
	{
		
		/**
		 * The Flame transformation we'll use as a basis.
		 */
		private FlameTransformation transformation;
		
		public Builder( FlameTransformation transformation )
		{
			if( transformation == null )
			{
				throw new IllegalArgumentException( "transformation must not be null" );
			}
			
			this.transformation = ( FlameTransformation )transformation.clone();
		}
		
		/**
		 * @return the flame transformation's affine transformation.
		 */
		public AffineTransformation affineTransformation()
		{
			return this.transformation.affineTransformation;
		}

		/**
		 * Set the affine transformation of the Flame transformation
		 * @param affineTransformation
		 */
		public void setAffineTransformation( AffineTransformation affineTransformation )
		{
			this.transformation.affineTransformation = affineTransformation.clone();
		}

		/**
		 * Get the weight of the specified variation
		 * @param variation
		 * @return
		 */
		public double variationWeight( Variation variation )
		{
			return this.transformation.variationWeight[ variation.index() ];
		}

		/**
		 * Set the weight of the specified variation
		 * @param variation
		 * @param weight
		 */
		public void setVariationWeight( Variation variation, double weight )
		{
			this.transformation.variationWeight[ variation.index() ] = weight;
		}
		
		/**
		 * Build the transformation
		 * 
		 * @return the Flame transformation just built
		 */
		public FlameTransformation build()
		{
			return this.transformation;
		}
		
	}

}
