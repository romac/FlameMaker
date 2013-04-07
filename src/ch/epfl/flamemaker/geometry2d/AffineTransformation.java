package ch.epfl.flamemaker.geometry2d;

/**
 * An affine transformation, defined by the first 6 coefficients
 * of its matrix:
 * 					| a  b  c |
 * 					| d  e  f |
 * 					| 0  0  1 |
 *
 */
public class AffineTransformation implements Transformation
{
	
	private double a, b, c,
				   d, e, f;
	
	public AffineTransformation( double a, double b, double c,
								 double d, double e, double f )
	{
		this.a = a; this.b = b; this.c = c;
		this.d = d; this.e = e; this.f = f;
	}
	
	@Override
	public Point transformPoint( Point p )
	{
		return new Point(
			this.a * p.x() + this.b * p.y() + this.c,
			this.d * p.x() + this.e * p.y() + this.f
		);
	}
	
	public double translationX()
	{
		return c;
	}
	
	public double translationY()
	{
		return f;
	}
	
	public AffineTransformation composeWith( AffineTransformation that )
	{
		return new AffineTransformation(
            this.a * that.a + this.b * that.d, this.a * that.b + this.b * that.e, this.a * that.c + this.b * that.f + this.c,
			this.d * that.a + this.e * that.d, this.d * that.b + this.e * that.e, this.d * that.c + this.e * that.f + this.f
		); 
	}
	
	public AffineTransformation clone()
	{
		return new AffineTransformation( this.a, this.b, this.c,
										 this.d, this.e, this.f );
	}
	
	public static AffineTransformation newTranslation( double dx, double dy )
	{
		 return new AffineTransformation(
		     1, 0, dx,
		     0, 1, dy
		 );
		
	}
	public static AffineTransformation newRotation( double theta )
	{
		return new AffineTransformation(
		    Math.cos( theta ), -Math.sin( theta ), 0,
		    Math.sin( theta ),  Math.cos( theta ), 0
		);
	}
	
	public static AffineTransformation newScaling(double sx, double sy)
	{
		return new AffineTransformation(
		    sx, 0, 0,
		    0, sy, 0
		);
	}
	
	public static AffineTransformation newShearX( double sx )
	{
		return new AffineTransformation(
		    1, sx, 0,
		    0, 1,  0
		);
	}
	
	public static AffineTransformation newShearY( double sy )
	{
		return new AffineTransformation(
		    1,  0, 0,
		    sy, 1, 0
		);
	}
	
	public static AffineTransformation IDENTITY()
	{
		return new AffineTransformation(
		   1, 0, 0,
		   0, 1, 0
		);
	}
	
	
}
