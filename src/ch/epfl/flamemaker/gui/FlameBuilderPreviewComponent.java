package ch.epfl.flamemaker.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameAccumulator;
import ch.epfl.flamemaker.flame.ObservableFlameBuilder;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * A component that displays the fractal that is being built by the given Builder
 * and updates everytime the builder modifies the fractal.
 */
public class FlameBuilderPreviewComponent extends JComponent implements Observer 
{

	private static final long serialVersionUID = 4246053641524095098L;
	
	private ObservableFlameBuilder builder;
	private Color bgColor;
	private Palette palette;
	private Rectangle frame;
	private int density;

	/**
	 * Create a new component, displaying the fractal built by the builder.
	 * @param builder The builder that's building the fractal
	 * @param bgColor The background color
	 * @param palette The color palette to use
	 * @param frame   The frame in which the points will be computed in.
	 * @param density The density of points to compute
	 */
	public FlameBuilderPreviewComponent( ObservableFlameBuilder builder, Color bgColor, Palette palette, Rectangle frame, int density )
	{
		this.builder = builder;
		this.bgColor = bgColor;
		this.palette = palette;
		this.frame = frame;
		this.density = density;
		
		// Add ourselves as an observer of the builder to get notified when it changes.
		this.builder.addObserver( this );
	}
	
	/**
	 * Paint the whole component
	 */
	@Override
	protected void paintComponent( Graphics g0 )
	{
		Graphics2D g = ( Graphics2D )g0;
		
		// Get the component's aspect ratio
		double aspectRatio = ( double )this.getWidth() / ( double )this.getHeight();
		
		// Expand the given frame to match the component's aspect ratio
		Rectangle expandedFrame = frame.expandToAspectRatio( aspectRatio );
		
		// Create the buffered image that'll hold the fractal pixels
		BufferedImage image = new BufferedImage( this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB );
		
		// Build the fractal
		Flame flame = this.builder.build();
		
		// Compute all the points of the fractal we'll then draw.
		FlameAccumulator accumulator = flame.compute( expandedFrame, this.getWidth(), this.getHeight(), this.density );
		
		// Draw each point
		for( int i = 0; i < accumulator.width(); i++ )
		{
			for( int j = 0; j < accumulator.height(); j++ )
			{
				Color color = accumulator.color( this.palette, this.bgColor, i, j );
				image.setRGB( i, accumulator.height() - j - 1, color.asPackedRGB() );
			}
		}
		
		// Draw the image itself
		g.drawImage( image, 0, 0, null );
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
