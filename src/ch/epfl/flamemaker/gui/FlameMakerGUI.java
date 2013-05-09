package ch.epfl.flamemaker.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
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
		
		/**
		 * Window
		 */
        try
        {
            for( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
            {
                if( "Nimbus".equals( info.getName() ) )
                {
                    UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        } catch( Exception e ) {}
		
		JFrame frame = new JFrame( "Flame Maker" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setMinimumSize( new Dimension( 600, 500 ) );
		
		Container contentPane = frame.getContentPane(); 
		contentPane.setLayout( new BorderLayout() );
		
		/**
		 * Fractal preview
		 */
		
		FlameBuilderPreviewComponent preview = new FlameBuilderPreviewComponent(
			this.builder, this.bgColor, this.palette, this.frame, this.density
		);
		
		JPanel fractalPanel = new JPanel();
		fractalPanel.setBorder( BorderFactory.createTitledBorder( "Fractale" ) );
		// fractalPanel.add( preview );
		
		/**
		 * Affine transformations grid
		 */
		final AffineTransformationsComponent affineTransformationsGrid = new AffineTransformationsComponent( this.builder, this.frame );
		
		JPanel transformationsPanel = new JPanel();
		transformationsPanel.setBorder( BorderFactory.createTitledBorder( "Transformations affines" ) );
		transformationsPanel.add( affineTransformationsGrid );
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new GridLayout( 1, 2 ) );
		topPanel.add( transformationsPanel );
		topPanel.add( fractalPanel );
		
		/**
		 * Transformations list
		 */
		final TransformationsListModel transformModel = new TransformationsListModel();
		final JList transformList = new JList( transformModel );
		transformList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		transformList.setVisibleRowCount( 3 );
		transformList.setSelectedIndex( 0 );
		
		JScrollPane transformListPane = new JScrollPane( transformList );
		
		final JButton addTransformationButton = new JButton( "Ajouter" );
		final JButton removeTransformationButton = new JButton( "Supprimer" );
		
		JPanel transformListButtons = new JPanel( new GridLayout( 1, 2 ) );
		transformListButtons.add( removeTransformationButton );
		transformListButtons.add( addTransformationButton );
		
		JPanel editPanel = new JPanel( new BorderLayout() );
		editPanel.setBorder( BorderFactory.createTitledBorder( "Transformations " ) );
		// editPanel.setPreferredSize( new Dimension( 100, 100 ) );
		editPanel.add( transformListPane, BorderLayout.CENTER );
		editPanel.add( transformListButtons, BorderLayout.PAGE_END );
		
		/*
		 * Current transformation
		 */
		DecimalFormat decimalFormat = new DecimalFormat( "#0.##" );
		
		JLabel translationLabel = new JLabel( "Translation" );
		JFormattedTextField translationField = new JFormattedTextField( decimalFormat );
		translationField.setValue( 0.1 );
		translationField.setHorizontalAlignment( SwingConstants.RIGHT );
		JButton leftTranslationButton = new JButton( "←" );
		JButton rightTranslationButton = new JButton( "→" );
		JButton upTranslationButton = new JButton( "↑" );
		JButton downTranslationButton = new JButton( "↓" );
		
		JLabel rotationLabel = new JLabel( "Rotation" );
		JFormattedTextField rotationField = new JFormattedTextField();
		rotationField.setValue( 15 );
		rotationField.setHorizontalAlignment( SwingConstants.RIGHT );
		JButton leftRotationButton = new JButton( "⟲" );
		JButton rightRotationButton = new JButton( "⟳" );
		
		JLabel dilatationLabel = new JLabel( "Dilatation" );
		JFormattedTextField dilatationField = new JFormattedTextField( decimalFormat );
		dilatationField.setValue( 1.05 );
		dilatationField.setHorizontalAlignment( SwingConstants.RIGHT );
		JButton moreHorizontalDilatationButton = new JButton( "+ ↔" );
		JButton lessHorizontalDilatationButton = new JButton( "- ↔" );
		JButton moreVerticalDilatationButton = new JButton( "+ ↕" );
		JButton lessVerticalDilatationButton = new JButton( "- ↕" );
		
		JLabel transvectionLabel = new JLabel( "Transvection" );
		JFormattedTextField transvectionField = new JFormattedTextField( decimalFormat );
		transvectionField.setValue( 0.1 );
		transvectionField.setHorizontalAlignment( SwingConstants.RIGHT );
		JButton leftTransvectionButton = new JButton( "←" );
		JButton rightTransvectionButton = new JButton( "→" );
		JButton upTransvectionButton = new JButton( "↑" );
		JButton downTransvectionButton = new JButton( "↓" );
		
		JPanel editAffinePanel = new JPanel();
		// editAffinePanel.setPreferredSize( new Dimension( 500, 200 ) );
		GroupLayout editAffineLayout = new GroupLayout( editAffinePanel );
		// editAffineLayout.setAutoCreateGaps(true);
		// editAffineLayout.setAutoCreateContainerGaps(true);
		
		editAffineLayout.setHorizontalGroup(
			editAffineLayout.createSequentialGroup()
				.addGroup( editAffineLayout.createParallelGroup( GroupLayout.Alignment.TRAILING )
					.addComponent( translationLabel )
					.addComponent( rotationLabel )
					.addComponent( dilatationLabel )
					.addComponent( transvectionLabel )
				)
				.addGroup( editAffineLayout.createParallelGroup()
					.addComponent( translationField )
					.addComponent( rotationField )
					.addComponent( dilatationField )
					.addComponent( transvectionField )
				)
				.addGroup( editAffineLayout.createParallelGroup()
					.addComponent( leftTranslationButton )
					.addComponent( leftRotationButton )
					.addComponent( moreHorizontalDilatationButton )
					.addComponent( leftTransvectionButton )
				)
				.addGroup( editAffineLayout.createParallelGroup()
					.addComponent( rightTranslationButton )
					.addComponent( rightRotationButton )
					.addComponent( lessHorizontalDilatationButton )
					.addComponent( rightTransvectionButton )
				)
				.addGroup( editAffineLayout.createParallelGroup()
					.addComponent( upTranslationButton )
					.addComponent( moreVerticalDilatationButton )
					.addComponent( upTransvectionButton )
				)
				.addGroup( editAffineLayout.createParallelGroup()
					.addComponent( downTranslationButton )
					.addComponent( lessVerticalDilatationButton )
					.addComponent( downTransvectionButton )
				)
		);
		
		editAffineLayout.setVerticalGroup(
			editAffineLayout.createSequentialGroup()
				.addGroup( editAffineLayout.createParallelGroup( GroupLayout.Alignment.BASELINE )
					.addComponent( translationLabel )
					.addComponent( translationField )
					.addComponent( leftTranslationButton )
					.addComponent( rightTranslationButton )
					.addComponent( upTranslationButton )
					.addComponent( downTranslationButton )
				)
				.addGroup( editAffineLayout.createParallelGroup( GroupLayout.Alignment.BASELINE )
    				.addComponent( rotationLabel )
    				.addComponent( rotationField )
    				.addComponent( leftRotationButton )
    				.addComponent( rightRotationButton )
    			)
    			.addGroup( editAffineLayout.createParallelGroup( GroupLayout.Alignment.BASELINE )
    				.addComponent( dilatationLabel )
    				.addComponent( dilatationField )
    				.addComponent( moreHorizontalDilatationButton )
    				.addComponent( lessHorizontalDilatationButton )
    				.addComponent( moreVerticalDilatationButton )
    				.addComponent( lessVerticalDilatationButton )
    			)
    			.addGroup( editAffineLayout.createParallelGroup( GroupLayout.Alignment.BASELINE )
    				.addComponent( transvectionLabel )
    				.addComponent( transvectionField )
    				.addComponent( leftTransvectionButton )
    				.addComponent( rightTransvectionButton )
    				.addComponent( upTransvectionButton )
    				.addComponent( downTransvectionButton )
    			)
		);
		
		editAffinePanel.setLayout( editAffineLayout );

		JPanel editCurrentPanel = new JPanel();
		editCurrentPanel.setBorder( BorderFactory.createTitledBorder( "Transformation courante" ) );
		editCurrentPanel.setLayout( new BoxLayout( editCurrentPanel, BoxLayout.PAGE_AXIS ) );
		editCurrentPanel.add( editAffinePanel );
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout( new BoxLayout( bottomPanel, BoxLayout.LINE_AXIS ) );
		bottomPanel.add( editPanel );
		bottomPanel.add( editCurrentPanel );
		
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
	        return "Transformation n°" + ( index + 1 );
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
