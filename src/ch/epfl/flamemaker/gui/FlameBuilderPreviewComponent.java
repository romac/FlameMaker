package ch.epfl.flamemaker.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameAccumulator;
import ch.epfl.flamemaker.flame.ObservableFlameBuilder;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class FlameBuilderPreviewComponent extends JComponent implements ObservableFlameBuilder.Observer 
{

	private static final long serialVersionUID = 4246053641524095098L;
	private ObservableFlameBuilder builder;
	private Color bgColor;
	private Palette palette;
	private Rectangle frame;
	private int density;

	public FlameBuilderPreviewComponent( ObservableFlameBuilder builder, Color bgColor, Palette palette, Rectangle frame, int density )
	{
		this.builder = builder;
		this.bgColor = bgColor;
		this.palette = palette;
		this.frame = frame;
		this.density = density;
		
		this.builder.addObserver( this );
	}
	
	@Override
	protected void paintComponent( Graphics g0 )
	{
		Graphics2D g = ( Graphics2D )g0;
		
		double aspectRatio = ( double )this.getWidth() / ( double )this.getHeight();
		
		Rectangle expandedFrame = frame.expandToAspectRatio( aspectRatio );
		
		BufferedImage image = new BufferedImage( this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB );
		
		Flame flame = this.builder.build();
		FlameAccumulator accumulator = flame.compute( expandedFrame, this.getWidth(), this.getHeight(), this.density );
		
		for( int i = 0; i < accumulator.width(); i++ )
		{
			for( int j = 0; j < accumulator.height(); j++ )
			{
				Color color = accumulator.color( this.palette, this.bgColor, i, j );
				image.setRGB( i, accumulator.height() - j - 1, color.asPackedRGB() );
			}
		}
		
		g.drawImage( image, 0, 0, null );
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		Dimension parentSize = this.getParent().getSize();
		
		return new Dimension( ( int )parentSize.getWidth() - 20, ( int )parentSize.getHeight() - 50 );
		
	}

	
	@Override
    public void flameBuilderChanged()
    {
	    this.repaint();
    }
	
}
