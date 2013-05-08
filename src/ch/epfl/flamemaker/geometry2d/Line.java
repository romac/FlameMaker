package ch.epfl.flamemaker.geometry2d;

public class Line
{

	private Point tail;
	private Point head;
	
	public Line( Point tail, Point head )
	{
		this.tail = tail;
		this.head = head;
	}
	
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
