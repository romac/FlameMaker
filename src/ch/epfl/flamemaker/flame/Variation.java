package ch.epfl.flamemaker.flame;

import java.util.Arrays;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Transformation;

/**
 * A variation is a commonly used, named, and indexed {@link Transformation}.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public abstract class Variation implements Transformation
{

	/**
	 * The most common variations, that'll be used by our program.
	 */
	public final static List<Variation> ALL_VARIATIONS = Arrays.asList(
		new Variation( 0, "Linear" )
		{
			@Override
			public Point transformPoint( Point p )
			{
				return p;
			}
		},
		new Variation( 1, "Sinusoidal" )
		{
			@Override
			public Point transformPoint( Point p )
			{
				return new Point( Math.sin( p.x() ), Math.sin( p.y() ) );
			}
		},
		new Variation( 2, "Spherical" )
		{
			@Override
			public Point transformPoint( Point p )
			{
				double r2 = p.r() * p.r();
				
				return new Point( p.x() / r2, p.y() / r2 );
			}
		},
		new Variation( 3, "Swirl" )
		{
			@Override
			public Point transformPoint( Point p )
			{
				double r2 = p.r() * p.r();
				
				return new Point(
					p.x() * Math.sin( r2 ) - p.y() * Math.cos( r2 ),
					p.x() * Math.cos( r2 ) + p.y() * Math.sin( r2 )
				);
			}
		},
		new Variation( 4, "Horseshoe" )
		{
			@Override
			public Point transformPoint( Point p )
			{
				return new Point(
					( ( p.x() - p.y() ) * ( p.x() + p.y() ) ) / p.r(),
					( 2 * p.x() * p.y() ) / p.r()
				);
			}
		},
		new Variation( 5, "Bubble" )
		{
			@Override
			public Point transformPoint( Point p )
			{
				double r2 = p.r() * p.r();
				
				return new Point(
					( 4 * p.x() ) / ( r2 + 4 ),
					( 4 * p.y() ) / ( r2 + 4 )
				);
			}
		}
	);
	
	/**
	 * The name of the variation
	 */
	private final String name;
	
	/**
	 * The index of the variation
	 */
	private final int index;

	/**
	 * Create a new variation with the given index and name
	 * 
	 * @param index An index
	 * @param name A name
	 */
	private Variation( int index, String name )
	{
		this.name = name;
		this.index = index;
	}
	
	/**
	 * @return the name of this variation 
	 */
	public String name()
	{
		return this.name;
	}
	
	/**
	 * @return the index of this variation
	 */
	public int index()
	{
		return this.index;
	}
	
	/**
	 * @see Transformation#transformPoint( Point )
	 */
	abstract public Point transformPoint( Point p );

}
