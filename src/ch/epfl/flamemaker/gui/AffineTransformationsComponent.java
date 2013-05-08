package ch.epfl.flamemaker.gui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameAccumulator;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Arrow;
import ch.epfl.flamemaker.geometry2d.Line;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class AffineTransformationsComponent extends JComponent
{

    private static final long serialVersionUID = -4507224814162575365L;
	private Flame.Builder builder;
	private Rectangle frame;
	private int highlightedTransformationIndex = 0;
	
	private Graphics2D g;
	private Rectangle expandedFrame;
	private AffineTransformation transform;
    
    public AffineTransformationsComponent( Flame.Builder builder, Rectangle frame )
    {
    	this.builder = builder;
    	this.frame = frame;
    }
    
    @Override
	protected void paintComponent( Graphics g0 )
	{
		Graphics2D g = this.g = ( Graphics2D )g0;
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		
		double aspectRatio = ( double )this.getWidth() / ( double )this.getHeight();
		this.expandedFrame = this.frame.expandToAspectRatio( aspectRatio );
		
		AffineTransformation scaling = AffineTransformation.newScaling( ( double )this.getWidth() / expandedFrame.width(), ( double )this.getHeight() / expandedFrame.height() ); 
		AffineTransformation translation = AffineTransformation.newTranslation( -expandedFrame.left() - 0.22, -expandedFrame.bottom() );
		this.transform = scaling.composeWith( translation );
		
		this.drawGrid();
		
		for( int i = 0; i < this.builder.transformationCount(); i++ )
		{
			AffineTransformation transformation = builder.affineTransformation( i );
			
			if( i == this.highlightedTransformationIndex )
			{
				continue;
			}
			
			this.drawTransformation( transformation, false );
		}
		
		this.drawTransformation( builder.affineTransformation( this.highlightedTransformationIndex ), true );
	}
    
    private void drawTransformation( AffineTransformation transformation, boolean highlight )
    {
    	Arrow a1 = new Arrow( new Point( -1, 0 ), new Point( 1, 0 ) );
    	Arrow a2 = new Arrow( new Point( 0, -1 ), new Point( 0, 1 ) );
    	
    	this.g.setColor( highlight ? java.awt.Color.RED : java.awt.Color.BLACK );
    	this.drawArrow( a1.transform( transformation ) );
    	this.drawArrow( a2.transform( transformation ) );
    }
    
    private void drawGrid()
    {
    	this.g.setColor( new java.awt.Color( 0.8f, 0.8f, 0.8f ) );
		
		for( double i = 1.0; i < expandedFrame.width() + 1; i += 1.0 )
		{
			this.drawLine( i, -expandedFrame.height() / 2, i, expandedFrame.height() / 2 );
			this.drawLine( -i, -expandedFrame.height() / 2, -i, expandedFrame.height() / 2 );
		}
		
		for( double i = 1.0; i < expandedFrame.height(); i += 1.0 )
		{
			this.drawLine( -expandedFrame.width() / 2, i, expandedFrame.width() / 2, i );
			this.drawLine( -expandedFrame.width() / 2, -i, expandedFrame.width() / 2, -i );
		}
		
		this.g.setColor( java.awt.Color.WHITE );
		this.g.setStroke( new BasicStroke( 2 ) );
		this.drawLine( 0, -expandedFrame.height() / 2, 0, expandedFrame.height() / 2 );
		this.drawLine( -expandedFrame.width() / 2, 0, expandedFrame.width() / 2, 0 );
    }
    
    private void drawLine( Point p1, Point p2 )
    {
    	p1 = this.transform.transformPoint( p1 );
		p2 = this.transform.transformPoint( p2 );
		
		this.g.draw( new Line2D.Double( ( int )p1.x(), ( int )p1.y(), ( int )p2.x(), ( int )p2.y() ) );
    }
    
    private void drawLine( double x1, double y1, double x2, double y2 )
    {
    	this.drawLine( new Point( x1, y1 ), new Point( x2, y2 ) );
    }
    
    private void drawArrow( Arrow arrow )
    {
    	Point tail = arrow.getTail();
    	Point head = arrow.getHead();
    	
    	this.drawLine( tail, head );
    	this.drawLine( head, arrow.getLeftSpike() );
    	this.drawLine( head, arrow.getRightSpike() );
    }
    
    @Override
	public Dimension getPreferredSize()
	{
		Dimension parentSize = this.getParent().getSize();
		
		if( parentSize.getWidth() < 200 || parentSize.getHeight() < 100 )
		{
			return new Dimension( 400, 400 );
		}
		
		return new Dimension( (int)parentSize.getWidth() - 20, (int)parentSize.getHeight() - 35 );
	}

	public int getHighlightedTransformationIndex()
    {
	    return highlightedTransformationIndex;
    }

	public void setHighlightedTransformationIndex( int highlightedTransformationIndex )
    {
	    this.highlightedTransformationIndex = highlightedTransformationIndex;
	    this.repaint();
    }
    
}
