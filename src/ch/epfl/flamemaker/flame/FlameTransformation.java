package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Transformation;

public class FlameTransformation implements Transformation
{

	private AffineTransformation affineTransformation;
	private double[] variationWeight;

	FlameTransformation( AffineTransformation affineTransformation, double[] variationWeight )
	{
		if( variationWeight.length != Variation.ALL_VARIATIONS.size() )
		{
			throw new IllegalArgumentException(
				"variationWeight should be of length " + Variation.ALL_VARIATIONS.size() + "."
			);
		}
		
		this.affineTransformation = affineTransformation;
		this.variationWeight = variationWeight.clone();
	}

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

}