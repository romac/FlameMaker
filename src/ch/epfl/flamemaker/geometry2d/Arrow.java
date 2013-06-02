package ch.epfl.flamemaker.geometry2d;

/**
 * Represents a 2D arrow.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public class Arrow extends Line
{
	
	/**
	 * The angle between the spikes and the body of the arrow.
	 */
	private static double SPIKE_ANGLE = Math.toRadians( 45 );
	
	private Point leftSpike;
	private Point rightSpike;
	
	/**
	 * Create a new arrow from one point to another.
	 * 
	 * @param tail
	 * @param head
	 */
	public Arrow( Point tail, Point head )
	{
		super( tail, head );
	}
	
	/**
	 * Create a new arrow on the given line.
	 * @param line
	 */
	public Arrow( Line line )
	{
		this( line.getTail(), line.getHead() );
	}
	
	/**
	 * Return a new arrow, transformed by the given transformation.
	 * 
	 * @param transformation the transformation to apply to this arrow
	 */
	public Arrow transform( AffineTransformation transformation )
	{
		return new Arrow(
			super.transform( transformation )
		);
	}
	
	/**
	 * Set the head of this arrow.
	 */
	@Override
	public void setHead( Point head )
	{
		super.setHead( head );
	}
	
	/**
	 * @return The point where the left spike ends.
	 */
	public Point getLeftSpike()
    {
		if( this.leftSpike == null )
		{
			double headX = this.getHead().x();
	        double headY = this.getHead().y();
	        
	        // Compute the angle of the arrow with Ox axis
			double theta = Math.atan2( headY - this.getTail().y(), headX - this.getTail().x() );
			
			// Add the angle of the spike w.r.t to the arrow
	        double angle = theta + SPIKE_ANGLE;
	        
	        // Compute the x coordinate of the spike 
	        double x = headX - 0.1 * Math.cos( angle );
	        
	        // Compute the y coordinate of the spike
	        double y = headY - 0.1 * Math.sin( angle );  
	        
	        this.leftSpike = new Point( x, y );
		}
		
		return this.leftSpike;
    }

	/**
	 * @return The point where the right spike ends.
	 */
	public Point getRightSpike()
    {
		if( this.rightSpike == null )
		{
    		double headX = this.getHead().x();
            double headY = this.getHead().y();
            
            // Compute the angle of the arrow with Ox axis
    		double theta = Math.atan2( headY - this.getTail().y(), headX - this.getTail().x() );
    		
    		// Add the angle of the spike w.r.t to the arrow
    		double angle = theta - SPIKE_ANGLE;
            
    		// Compute the x coordinate of the spike
            double x = headX - 0.1  * Math.cos( angle );  
            
            // Compute the y coordinate of the spike
            double y = headY - 0.1 * Math.sin( angle );
            
            this.rightSpike = new Point( x, y );
		}
		
		return this.rightSpike;
    }
	
}
