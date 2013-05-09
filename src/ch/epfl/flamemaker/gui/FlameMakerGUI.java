package ch.epfl.flamemaker.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.flame.ObservableFlameBuilder;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.geometry2d.Point;

public class FlameMakerGUI
{
	
	private ObservableFlameBuilder builder 	= new ObservableFlameBuilder( new Flame.Builder( null ) );
	private Color bgColor 			= Color.BLACK;
	private Palette palette 		= InterpolatedPalette.RGB_PALETTE;
	private Rectangle frame 		= new Rectangle( new Point( -0.25, 0 ), 5, 4 );
	private int density 			= 50;
	private int selectedTransformationIndex = 0;
	private ArrayList<TransformationSelectionObserver> selectedTransformationObservers = new ArrayList<TransformationSelectionObserver>();
	private AffineTransformationsComponent affineTransformationsGrid;
	
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
	
	public void start()
    {
		this.setUp();
		
		JFrame frame = new JFrame( "Flame Maker" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setMinimumSize( new Dimension( 600, 500 ) );
		
		Container contentPane = frame.getContentPane(); 
		contentPane.setLayout( new BorderLayout() );
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new GridLayout( 1, 2 ) );
		
		JPanel transformationsPanel = new JPanel();
		transformationsPanel.setBorder( BorderFactory.createTitledBorder( "Transformations affines" ) );
		
		JPanel fractalPanel = new JPanel();
		fractalPanel.setBorder( BorderFactory.createTitledBorder( "Fractale" ) );
		
		topPanel.add( transformationsPanel );
		topPanel.add( fractalPanel );
		
		this.affineTransformationsGrid = new AffineTransformationsComponent( this.builder, this.frame );
		transformationsPanel.add( this.affineTransformationsGrid );
		
		FlameBuilderPreviewComponent preview = new FlameBuilderPreviewComponent(
			this.builder, this.bgColor, this.palette, this.frame, this.density
		);
		
		fractalPanel.add( preview );
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder( BorderFactory.createTitledBorder( "Transformations " ) );
		bottomPanel.setLayout( new BoxLayout( bottomPanel, BoxLayout.LINE_AXIS ) );
		
		JPanel editPanel = new JPanel();
		editPanel.setLayout( new BorderLayout() );
		
		final TransformationsListModel transformModel = new TransformationsListModel();
		final JList transformList = new JList( transformModel );
		transformList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		transformList.setVisibleRowCount( 3 );
		transformList.setSelectedIndex( 0 );
		
		JScrollPane transformListPane = new JScrollPane( transformList );
		JPanel transformListButtons = new JPanel( new GridLayout( 1, 2 ) );
		
		final JButton addTransformationButton = new JButton( "Ajouter" );
		final JButton removeTransformationButton = new JButton( "Supprimer" );
		
		transformListButtons.add( removeTransformationButton );
		transformListButtons.add( addTransformationButton );
		
		editPanel.add( transformListPane, BorderLayout.CENTER );
		editPanel.add( transformListButtons, BorderLayout.PAGE_END );
		
		bottomPanel.add( editPanel );
		
		contentPane.add( topPanel, BorderLayout.CENTER );
		contentPane.add( bottomPanel, BorderLayout.PAGE_END );
		
		frame.pack();
		frame.setVisible( true );
		
		addTransformationButton.addActionListener( new ActionListener()
		{
			@Override
            public void actionPerformed( ActionEvent event )
            {
				transformModel.addTransformation();
				
				transformList.setSelectedIndex( transformModel.getSize() - 1 );
				transformList.ensureIndexIsVisible(transformList.getSelectedIndex());
				
				if( transformModel.getSize() > 1 )
				{
					removeTransformationButton.setEnabled( true );
				}
            }
		} );
		
		removeTransformationButton.addActionListener( new ActionListener()
		{
			@Override
            public void actionPerformed( ActionEvent event )
            {
				int index = transformList.getSelectedIndex();
				int lastIndex = transformModel.getSize() - 1;
				
				if( index < lastIndex )
				{
					transformList.setSelectedIndex( index + 1 );
				}
				else
				{
					transformList.setSelectedIndex( index - 1 );
				}
				
				transformModel.removeTransformation( index );
				
				if( transformModel.getSize() == 1 )
				{
					removeTransformationButton.setEnabled( false );
				}
            }
		} );
		
		this.addSelectedTransformationObserver( new TransformationSelectionObserver()
		{
			@Override
			public void selectionChanged()
			{
				affineTransformationsGrid.setHighlightedTransformationIndex( selectedTransformationIndex );
			}
		} );
		
		transformList.addListSelectionListener( new ListSelectionListener()
		{
			@Override
            public void valueChanged( ListSelectionEvent event )
            {
	            setSelectedTransformationIndex( ( ( JList )event.getSource() ).getSelectedIndex() );
            }
		} );
    }
	
	public void removeSelectedTransformationObserver( TransformationSelectionObserver observer )
    {
	    this.selectedTransformationObservers.remove( observer );
    }

	public void addSelectedTransformationObserver( TransformationSelectionObserver observer )
    {
		this.selectedTransformationObservers.add( observer );
    }
	
	private void fireSelectedTransformationChanged()
	{
		for( TransformationSelectionObserver observer : this.selectedTransformationObservers )
		{
			observer.selectionChanged();
		}
	}

	public int getSelectedTransformationIndex()
    {
	    return selectedTransformationIndex;
    }

	public void setSelectedTransformationIndex( int selectedTransformationIndex )
    {
	    this.selectedTransformationIndex = selectedTransformationIndex;
	    this.fireSelectedTransformationChanged();
    }

	protected class TransformationsListModel extends AbstractListModel
	{

		private static final long serialVersionUID = -8579595948096049981L;
		
		void addTransformation()
		{
			builder.addTransformation( FlameTransformation.IDENTITY() );
			
			int lastIndex = builder.transformationCount() - 1;
			this.fireIntervalAdded( this, lastIndex, lastIndex );
		}
		
		void removeTransformation( int index )
		{
			builder.removeTransformation( index );
			this.fireIntervalRemoved( this, index, index );
		}
		
		@Override
        public String getElementAt( int index )
        {
	        return "Transformation n¡" + ( index + 1 );
        }

		@Override
        public int getSize()
        {
	        return builder.transformationCount();
        }
		
	}
	
	public interface TransformationSelectionObserver
	{
		void selectionChanged();
	}
	
}
