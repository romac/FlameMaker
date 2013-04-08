package ch.epfl.flamemaker.ifs;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class IFSAccumulatorBuilder
{

	private Rectangle frame;
	private int width;
	private int height;
	private boolean[][] hitMap;
	private AffineTransformation transformation;

	IFSAccumulatorBuilder( Rectangle frame, int width, int height )
	{
		this.frame = frame;

		if( width < 1 )
		{
			throw new IllegalArgumentException( "width must be strictly positive." );
		}
		if( height < 1 )
		{
			throw new IllegalArgumentException( "height must be strictly positive." );
		}
		this.width = width;
		this.height = height;
		this.hitMap = new boolean[ this.width ][ this.height ];

		this.transformation = AffineTransformation.newScaling( this.width / this.frame.width(), this.height / this.frame.height() );
		this.transformation = this.transformation.composeWith( AffineTransformation.newTranslation( -this.frame.left(), -this.frame.bottom() ) );
	}

	public void hit( Point p )
	{
		if( !this.frame.contains( p ) )
		{
			return;
		}

		p = this.transformation.transformPoint( p );

		int x = ( int )Math.floor( p.x() );
		int y = ( int )Math.floor( p.y() );

		this.hitMap[ x ][ y ] = true;
	}

	public IFSAccumulator build()
	{
		return new IFSAccumulator( this.hitMap );
	}

}
