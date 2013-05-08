package ch.epfl.flamemaker.gui;

import java.awt.*;

import javax.swing.*;
import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.geometry2d.Point;

public class FlameMakerGUI
{
	
	private Flame.Builder builder 	= new Flame.Builder( null );
	private Color bgColor 			= Color.BLACK;
	private Palette palette 		= InterpolatedPalette.RGB_PALETTE;
	private Rectangle frame 		= new Rectangle( new Point( -0.25, 0 ), 5, 4 );
	private int density 			= 50;
	
	public void start()
    {
		this.setUp();
		
		JFrame frame = new JFrame( "Flame Maker" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setMinimumSize( new Dimension( 600, 300 ) );
		
		Container contentPane = frame.getContentPane(); 
		contentPane.setLayout( new BorderLayout() );
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new GridLayout( 1, 2 ) );
		
		GridBagConstraints fillConstraints = new GridBagConstraints();
		fillConstraints.fill = GridBagConstraints.BOTH;
		
		JPanel transformationsPanel = new JPanel();
		transformationsPanel.setBorder( BorderFactory.createTitledBorder( "Transformations affines" ) );
		
		JPanel fractalPanel = new JPanel();
		fractalPanel.setBorder( BorderFactory.createTitledBorder( "Fractale" ) );
		
		topPanel.add( transformationsPanel, fillConstraints );
		topPanel.add( fractalPanel, fillConstraints );
		
		transformationsPanel.add( new AffineTransformationsComponent( this.builder, this.frame ) );
		
		FlameBuilderPreviewComponent preview = new FlameBuilderPreviewComponent(
			this.builder, this.bgColor, this.palette, this.frame, this.density
		);
		
		fractalPanel.add( preview );
		
		contentPane.add( topPanel, BorderLayout.CENTER );
		
		frame.pack();
		frame.setVisible( true );
    }
	
	private void setUp()
	{
		
		this.builder.addTransformation( new FlameTransformation(
            new AffineTransformation(
            	-0.4113504, -0.7124804, -0.4,
            	 0.7124795, -0.4113508,  0.8
            ),
            new double[] { 1, 0.1, 0, 0, 0, 0 }
        ) );
		
		this.builder.addTransformation( new FlameTransformation(
            new AffineTransformation(
                -0.3957339,  0, 		-1.6,
                 0, 		-0.3957337,  0.2
            ),
            new double[] { 0, 0, 0, 0, 0.8, 1 }
       ) );
		
		this.builder.addTransformation( new FlameTransformation(
            new AffineTransformation(
            	0.4810169, 0, 		  1,
                0, 		   0.4810169, 0.9
            ),
            new double[] { 1, 0, 0, 0, 0, 0 }
        ) );
	}
	
}
