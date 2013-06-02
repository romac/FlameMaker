package ch.epfl.flamemaker.geometry2d;

/**
 * Represents a transformation to apply on a point.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public interface Transformation
{

	/**
	 * Transform the given point.
	 * 
	 * @param p The point to transform.
	 * @return A transformed point.
	 */
	public Point transformPoint( Point p );

}
