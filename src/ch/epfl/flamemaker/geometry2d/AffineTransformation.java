package ch.epfl.flamemaker.geometry2d;

/**
 * An affine transformation, defined by the first 6 coefficients
 * of its 3x3 matrix:
 * 
 * 					| a  b  c |
 * 					| d  e  f |
 * 					| 0  0  1 |
 *
 * The transformed point is computed from the multiplied with the point's components,
 * expressed as homogeneous coordinates, in a 3x1 matrix:
 * 
 * 					| a  b  c |   | p.x |
 * 					| a  b  c | * | p.y |
 * 					| a  b  c |   |  1  |
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public class AffineTransformation implements Transformation
{
	
	/**
	 * The identity transformation.
	 */
	public static AffineTransformation IDENTITY()
	{
		return new AffineTransformation(
		   1, 0, 0,
		   0, 1, 0
		);
	}
	
	/**
	 * The first 6 components of the matrix
	 */
	private double a, b, c,
				   d, e, f;
	
	/**
	 * Create a new affine transformation by it first 6 components.
     * 
     * | a  b  c |
	 * | d  e  f |
 	 * | 0  0  1 |
 	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 */
	public AffineTransformation( double a, double b, double c,
								 double d, double e, double f )
	{
		this.a = a; this.b = b; this.c = c;
		this.d = d; this.e = e; this.f = f;
	}
	
	/**
	 * Transform a point like detailed in {@link AffineTransformation}.
	 * 
	 * @see Transformation
	 */
	@Override
	public Point transformPoint( Point p )
	{
		return new Point(
			this.a * p.x() + this.b * p.y() + this.c,
			this.d * p.x() + this.e * p.y() + this.f
		);
	}
	
	/**
	 * Get the amount of horizontal translation.
	 */
	public double translationX()
	{
		return c;
	}
	
	/**
	 * Get the amount of vertical translation.
	 */
	public double translationY()
	{
		return f;
	}
	
	/**
	 * Compose this transformation with another one.
	 * 
	 * @param that The transformation to compose this one with.
	 * @return
	 */
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
	
	/**
	 * Create a translation.
	 * 
	 * @param dx
	 * @param dy
	 */
	public static AffineTransformation newTranslation( double dx, double dy )
	{
		 return new AffineTransformation(
		     1, 0, dx,
		     0, 1, dy
		 );
		
	}

	/**
	 * Create a rotation, by the given angle in radian.
	 * 
	 * @param theta
	 */
	public static AffineTransformation newRotation( double theta )
	{
		return new AffineTransformation(
		    Math.cos( theta ), -Math.sin( theta ), 0,
		    Math.sin( theta ),  Math.cos( theta ), 0
		);
	}
	
	/**
	 * Create a scaling.
	 * 
	 * @param sx
	 * @param sy
	 * @return
	 */
	public static AffineTransformation newScaling( double sx, double sy )
	{
		return new AffineTransformation(
		    sx, 0, 0,
		    0, sy, 0
		);
	}
	
	/**
	 * Create a horizontal transvection
	 * 
	 * @param sx
	 * @return
	 */
	public static AffineTransformation newShearX( double sx )
	{
		return new AffineTransformation(
		    1, sx, 0,
		    0, 1,  0
		);
	}
	
	/**
	 * Create a vetical transvection.
	 * 
	 * @param sy
	 * @return
	 */
	public static AffineTransformation newShearY( double sy )
	{
		return new AffineTransformation(
		    1,  0, 0,
		    sy, 1, 0
		);
	}
	
	
}
