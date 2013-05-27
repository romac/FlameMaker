package ch.epfl.flamemaker.gui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import ch.epfl.flamemaker.flame.ObservableFlameBuilder;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Arrow;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * A component that displays the {@link ch.epfl.flamemaker.geometry2d.AffineTransformation}s of the current flame
 * being built by the {@link ch.epfl.flamemaker.flame.Flame.Builder} on a grid,
 * and highlights the transformation currently selected.
 */
public class AffineTransformationsComponent extends JComponent implements Observer
{

    private static final long serialVersionUID = -4507224814162575365L;
	private ObservableFlameBuilder builder;
	private Rectangle frame;
	private int highlightedTransformationIndex = 0;
	
	private Graphics2D g;
	private Rectangle expandedFrame;
	private AffineTransformation transform;
    
	/**
	 * Create a new component displaying the transformations on a grid.
	 * @param builder
	 * @param frame
	 */
    public AffineTransformationsComponent( ObservableFlameBuilder builder, Rectangle frame )
    {
    	this.builder = builder;
    	this.frame = frame;
    	
    	// Set ourselves as an observer of the builder.
    	this.builder.addObserver( this );
    }
    
    /**
     * Paint the grid and the transformations.
     */
    @Override
	protected void paintComponent( Graphics g0 )
	{
		Graphics2D g = this.g = ( Graphics2D )g0;
		
		// Make things pretty by enabling anti-aliasing.
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		
		// Get the component's aspect ratio
		double aspectRatio = ( double )this.getWidth() / ( double )this.getHeight();
		
		// Expand the given frame to match the component's aspect ratio
		this.expandedFrame = this.frame.expandToAspectRatio( aspectRatio );
		
		// Prepare the transformation that will be applied on every points drawn on the grid
		// to be able to think and compute points like if they were in a 2D plane with the origin at (0,0).
		AffineTransformation scaling = AffineTransformation.newScaling( ( double )this.getWidth() / expandedFrame.width(), ( double )this.getHeight() / expandedFrame.height() ); 
		AffineTransformation translation = AffineTransformation.newTranslation( -expandedFrame.left() - 0.22, -expandedFrame.bottom() );
		this.transform = scaling.composeWith( translation );
		
		// Draw the underlying grid first.
		this.drawGrid();
		
		// Draw each transformation
		for( int i = 0; i < this.builder.transformationCount(); i++ )
		{
			AffineTransformation transformation = builder.affineTransformation( i );
			
			// Don't draw the highlighted one yet.
			if( i == this.highlightedTransformationIndex )
			{
				continue;
			}
			
			this.drawTransformation( transformation, false );
		}
		
		// We draw it afterwards because we want them to always be on top of the others.
		this.drawTransformation( builder.affineTransformation( this.highlightedTransformationIndex ), true );
	}
    
    /**
     * Draw the given transformation and paints it red if needed.
     * 
     * @param transformation The transformation to paint.
     * @param highlight Whether it should be highlighted or not.
     */
    private void drawTransformation( AffineTransformation transformation, boolean highlight )
    {
    	// Create the two intersecting arrows centerd at origin.
    	Arrow a1 = new Arrow( new Point( -1, 0 ), new Point( 1, 0 ) );
    	Arrow a2 = new Arrow( new Point( 0, -1 ), new Point( 0, 1 ) );
    	
    	this.g.setColor( highlight ? java.awt.Color.RED : java.awt.Color.BLACK );
    	
    	// Transform and draw each arrow
    	this.drawArrow( a1.transform( transformation ) );
    	this.drawArrow( a2.transform( transformation ) );
    }
    
    /**
     * Draw the underlying grid.
     */
    private void drawGrid()
    {
    	this.g.setColor( new java.awt.Color( 0.85f, 0.85f, 0.85f ) );
		
    	// Draw each vertical line every 1 unit.
		for( double i = 1.0; i < expandedFrame.width() + 1; i += 1.0 )
		{
			this.drawLine( i, -expandedFrame.height() / 2, i, expandedFrame.height() / 2 );
			this.drawLine( -i, -expandedFrame.height() / 2, -i, expandedFrame.height() / 2 );
		}
		
		// Draw each horizontal line every 1 unit.
		for( double i = 1.0; i < expandedFrame.height(); i += 1.0 )
		{
			this.drawLine( -expandedFrame.width() / 2, i, expandedFrame.width() / 2, i );
			this.drawLine( -expandedFrame.width() / 2, -i, expandedFrame.width() / 2, -i );
		}
		
		// Draw the center axis a bit bolder.
		this.g.setColor( java.awt.Color.WHITE );
		this.g.setStroke( new BasicStroke( 2 ) );
		this.drawLine( 0, -expandedFrame.height() / 2, 0, expandedFrame.height() / 2 );
		this.drawLine( -expandedFrame.width() / 2, 0, expandedFrame.width() / 2, 0 );
    }
    
    /**
     * Draw a line between two points.
     * This method takes care of scaling and translating the points
     * from the frame to the actual viewport before drawing the line.
     * 
     * @param p1 First point
     * @param p2 Second point
     */
    private void drawLine( Point p1, Point p2 )
    {
    	p1 = this.transform.transformPoint( p1 );
		p2 = this.transform.transformPoint( p2 );
		
		this.g.draw( new Line2D.Double( ( int )p1.x(), ( int )p1.y(), ( int )p2.x(), ( int )p2.y() ) );
    }
    
    /**
     * Draw a line on the grid.
     * 
     * @see AffineTransformationsComponent#drawLine(Point, Point)
     * 
     * @param x1 x coordinate of the first point of the line
     * @param y1 y coordinate of the first point of the line
     * @param x2 x coordinate of the second point of the line
     * @param y2 y coordinate of the second point of the line
     */
    private void drawLine( double x1, double y1, double x2, double y2 )
    {
    	this.drawLine( new Point( x1, y1 ), new Point( x2, y2 ) );
    }
    
    /**
     * Draw the given arrow on the grid.
     * 
     * @param arrow
     */
    private void drawArrow( Arrow arrow )
    {
    	Point tail = arrow.getTail();
    	Point head = arrow.getHead();
    	
    	this.drawLine( tail, head );
    	this.drawLine( head, arrow.getLeftSpike() );
    	this.drawLine( head, arrow.getRightSpike() );
    }
    
    /**
     * Return the size this component should have, which is its parent's one with margins.
     */
    @Override
	public Dimension getPreferredSize()
	{
		Dimension parentSize = this.getParent().getSize();
		
		return new Dimension( ( int )parentSize.getWidth() - 20, ( int )parentSize.getHeight() - 50 );
	}
    
    /**
     * @return the index of the currently highlighted transformation.
     */
	public int getHighlightedTransformationIndex()
    {
	    return highlightedTransformationIndex;
    }
	
	/**
	 * Set the transformation that should currently be highlighted, and toggle a repaint.
	 * 
	 * @param highlightedTransformationIndex
	 */
	public void setHighlightedTransformationIndex( int highlightedTransformationIndex )
    {
	    this.highlightedTransformationIndex = highlightedTransformationIndex;
	    this.repaint();
    }
	
	
	/**
	 * Called when the flame builder is modified, in order to redraw the fractal
	 * and thus reflect the changes in the builder.
	 * @param builder The builder that changed
	 * @param argument Unused
	 */
	@Override
    public void update( Observable builder, Object argument )
    {
		this.repaint();	    
	}

}
