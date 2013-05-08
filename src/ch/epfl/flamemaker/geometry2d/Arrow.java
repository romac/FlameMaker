package ch.epfl.flamemaker.geometry2d;

public class Arrow extends Line
{
	
	private static double SPIKE_ANGLE = Math.toRadians( 45 );
	
	private Point leftSpike;
	private Point rightSpike;
	
	public Arrow( Point tail, Point head )
	{
		super( tail, head );
	}
	
	public Arrow( Line line )
	{
		this( line.getTail(), line.getHead() );
	}
	
	public Arrow transform( AffineTransformation transformation )
	{
		return new Arrow(
			super.transform( transformation )
		);
	}
	
	@Override
	public void setHead( Point head )
	{
		super.setHead( head );
	}

	public Point getLeftSpike()
    {
		if( this.leftSpike == null )
		{
			double headX = this.getHead().x();
	        double headY = this.getHead().y();
	        
			double theta = Math.atan2( headY - this.getTail().y(), headX - this.getTail().x() );
	        double angle = theta + SPIKE_ANGLE;
	        
	        double x = headX - 0.1  * Math.cos( angle );  
	        double y = headY - 0.1 * Math.sin( angle );  
	        
	        this.leftSpike = new Point( x, y );
		}
		
		return this.leftSpike;
    }

	public Point getRightSpike()
    {
		if( this.rightSpike == null )
		{
    		double headX = this.getHead().x();
            double headY = this.getHead().y();
            
    		double theta = Math.atan2( headY - this.getTail().y(), headX - this.getTail().x() );
    		double angle = theta - SPIKE_ANGLE;
            
            double x = headX - 0.1  * Math.cos( angle );  
            double y = headY - 0.1 * Math.sin( angle );
            
            this.rightSpike = new Point( x, y );
		}
		
		return this.rightSpike;
    }
	
}
