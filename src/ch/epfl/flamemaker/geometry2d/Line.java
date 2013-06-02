package ch.epfl.flamemaker.geometry2d;

/**
 * Represents a 2D line.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public class Line
{

	/**
	 * The tail of the line
	 */
	private Point tail;
	
	/**
	 * The head of the line
	 */
	private Point head;
	
	/**
	 * Create a new line from one point to another
	 * @param tail
	 * @param head
	 */
	public Line( Point tail, Point head )
	{
		this.tail = tail;
		this.head = head;
	}
	
	/**
	 * Return a new line, transformed by the given transformation.
	 * 
	 * @param transformation the transformation to apply to this line
	 */
	public Line transform( AffineTransformation transformation )
	{
		return new Line(
			transformation.transformPoint( this.tail ),
			transformation.transformPoint( this.head )
		);
	}
	
	public Point getTail()
    {
	    return tail;
    }

	public void setTail( Point tail )
    {
	    this.tail = tail;
    }

	public Point getHead()
    {
	    return head;
    }

	public void setHead( Point head )
    {
	    this.head = head;
    }
	
}
