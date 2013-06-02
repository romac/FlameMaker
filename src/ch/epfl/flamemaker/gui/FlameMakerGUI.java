package ch.epfl.flamemaker.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.flame.ObservableFlameBuilder;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.geometry2d.Point;

/**
 * Builds the GUI of the program and wires everything together.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public class FlameMakerGUI
{
	
	/**
	 * The flame builder we'll use to build the fractal.
	 */
	private ObservableFlameBuilder builder 	= new ObservableFlameBuilder( new Flame.Builder( null ) );
	
	/**
	 * An list of observers that should be notified when the transformation selection changes.
	 * @see FlameMakerGUI#selectedTransformationIndex 
	 */
	private ArrayList<TransformationSelectionObserver> selectedTransformationObservers = new ArrayList<TransformationSelectionObserver>();
	
	/**
	 * The index of the selected transformation.
	 */
	private int selectedTransformationIndex = 0;
	
	private Color bgColor;
	private Palette palette;
	private Rectangle frame;
	private int density;
	
	/**
	 * Since we want to display something at startup, we'll initialize
	 * the program with a Sharkfin fractal with an RGB palette on a black background.
	 */
	private void setUp()
	{
		this.bgColor = Color.BLACK;
		this.palette = InterpolatedPalette.RGB_PALETTE;
		this.frame = new Rectangle( new Point( -0.25, 0 ), 5, 4 );
		this.density = 50;
		
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
	
	/**
	 * Start the program
	 */
	public void start()
    {
		// Set the Sharkfin fractal up
		this.setUp();
		
		/**
		 * Set a nice look-and-feel that matches OS X's interface.
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
		
        /**
         * The window itself.
         */
		JFrame frame = new JFrame( "Flame Maker" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setMinimumSize( new Dimension( 600, 500 ) );
		frame.setPreferredSize( new Dimension( 600, 500 ) );
		
		Container contentPane = frame.getContentPane(); 
		contentPane.setLayout( new BorderLayout() );
		
		/**
		 * The component that'll display the fractal
		 */
		FlameBuilderPreviewComponent preview = new FlameBuilderPreviewComponent(
			this.builder, this.bgColor, this.palette, this.frame, this.density
		);
		
		JPanel fractalPanel = new JPanel();
		fractalPanel.setBorder( BorderFactory.createTitledBorder( "Fractale" ) );
		fractalPanel.add( preview );
		
		/**
		 * The component that'll display the transformations on a grid 
		 */
		final AffineTransformationsComponent affineTransformationsGrid = new AffineTransformationsComponent( this.builder, this.frame );
		
		// Highlight the newly selected transformation when the selection changes.
		this.addSelectedTransformationObserver( new TransformationSelectionObserver()
		{
			@Override
			public void selectionChanged()
			{
				affineTransformationsGrid.setHighlightedTransformationIndex( selectedTransformationIndex );
			}
		} );
		
		JPanel transformationsPanel = new JPanel();
		transformationsPanel.setBorder( BorderFactory.createTitledBorder( "Transformations affines" ) );
		transformationsPanel.add( affineTransformationsGrid );
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new GridLayout( 1, 2 ) );
		topPanel.add( transformationsPanel );
		topPanel.add( fractalPanel );
		
		/**
		 * The transformations list
		 */
		final TransformationsListModel transformModel = new TransformationsListModel();
		final JList transformList = new JList( transformModel );
		transformList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		transformList.setVisibleRowCount( 3 );
		
		// We the selection changes, set the selected transformation index accordingly
		// to trigger the update of the grid and the text fields.
		transformList.addListSelectionListener( new ListSelectionListener()
		{
			@Override
            public void valueChanged( ListSelectionEvent event )
            {
	            setSelectedTransformationIndex( ( ( JList )event.getSource() ).getSelectedIndex() );
            }
		} );
		
		// Make it scrollable.
		JScrollPane transformListPane = new JScrollPane( transformList );
		
		final JButton addTransformationButton = new JButton( "Ajouter" );
		final JButton removeTransformationButton = new JButton( "Supprimer" );
		
		JPanel transformListButtons = new JPanel( new GridLayout( 1, 2 ) );
		transformListButtons.add( removeTransformationButton );
		transformListButtons.add( addTransformationButton );
		
		JPanel editPanel = new JPanel( new BorderLayout() );
		editPanel.setBorder( BorderFactory.createTitledBorder( "Transformations " ) );
		editPanel.add( transformListPane, BorderLayout.CENTER );
		editPanel.add( transformListButtons, BorderLayout.PAGE_END );
		
		// Add a new transformation to the list when we click ont the Add button
		addTransformationButton.addActionListener( new ActionListener()
		{
			@Override
            public void actionPerformed( ActionEvent event )
            {
				// Add a new transformation to the list model
				transformModel.addTransformation();
				
				// Select the new item just created
				transformList.setSelectedIndex( transformModel.getSize() - 1 );
				transformList.ensureIndexIsVisible( transformList.getSelectedIndex() );
				
				// Since there's now at least two transformations
				// we can re-enable the Remove button.
				removeTransformationButton.setEnabled( true );
            }
		} );
		
		// Remove the selected transformation from the list when we click ont the Remove button
		removeTransformationButton.addActionListener( new ActionListener()
		{
			@Override
            public void actionPerformed( ActionEvent event )
            {
				int index = transformList.getSelectedIndex();
				int lastIndex = transformModel.getSize() - 1;
				
				// If we're not going to remove the last item, select the next one 
				if( index < lastIndex )
				{
					transformList.setSelectedIndex( index + 1 );
				}
				else // or select the previous
				{
					transformList.setSelectedIndex( index - 1 );
				}
				
				transformModel.removeTransformation( index );
				
				// Disable the Remove button if there's only one transformation left.
				if( transformModel.getSize() == 1 )
				{
					removeTransformationButton.setEnabled( false );
				}
            }
		} );
		
		// A formatter that'll be used for most text fields.
		DecimalFormat decimalFormat = new DecimalFormat( "#0.0#" );
		
		/**
		 * The interface that'll let us modify the currently selected transformation.
		 */
		
		JLabel translationLabel = new JLabel( "Translation" );
		JFormattedTextField translationField = new JFormattedTextField( decimalFormat );
		translationField.setValue( 0.1 );
		translationField.setHorizontalAlignment( SwingConstants.RIGHT );
		JButton leftTranslationButton = new JButton( "←" );
		JButton rightTranslationButton = new JButton( "→" );
		JButton upTranslationButton = new JButton( "↑" );
		JButton downTranslationButton = new JButton( "↓" );
		
		leftTranslationButton.addActionListener( new TransformationButtonListener( translationField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newTranslation(
					-this.doubleValue(), 0
				);
			}
		} );
		rightTranslationButton.addActionListener( new TransformationButtonListener( translationField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newTranslation(
					this.doubleValue(), 0
				);
			}
		} );
		upTranslationButton.addActionListener( new TransformationButtonListener( translationField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newTranslation(
					0, -this.doubleValue()
				);
			}
		} );
		downTranslationButton.addActionListener( new TransformationButtonListener( translationField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newTranslation(
					0, this.doubleValue()
				);
			}
		} );
		
		JLabel rotationLabel = new JLabel( "Rotation" );
		JFormattedTextField rotationField = new JFormattedTextField();
		rotationField.setValue( 15 );
		rotationField.setHorizontalAlignment( SwingConstants.RIGHT );
		JButton leftRotationButton = new JButton( "⟲" );
		JButton rightRotationButton = new JButton( "⟳" );
		
		leftRotationButton.addActionListener( new TransformationButtonListener( rotationField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newRotation( -Math.toRadians( this.doubleValue() ) );
			}
		} );
		rightRotationButton.addActionListener( new TransformationButtonListener( rotationField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newRotation( Math.toRadians( this.doubleValue() ) );
			}
		} );
		
		JLabel dilatationLabel = new JLabel( "Dilatation" );
		JFormattedTextField dilatationField = new JFormattedTextField( decimalFormat );
		dilatationField.setValue( 1.05 );
		dilatationField.setInputVerifier( new InputVerifier()
		{
			@Override
			public boolean verify( JComponent component )
			{
				JFormattedTextField field = ( JFormattedTextField )component;
				double previousValue = Double.valueOf( field.getValue().toString() );
				String currentText = field.getText();
				AbstractFormatter formatter = field.getFormatter();
				
				// Try to get the double value of the current text (which might fail and thus
				// throw an exception if eg. the user entered text)
				try
                {
	                double currentValue = Double.valueOf( currentText );
	                
	                // Since we disallow the value 0,
	                if( currentValue == 0.0 )
	                {
	                	// we'll just throw an exception to jump
	                	// into the next catch block and restores the previous value.
	                	throw new Exception( "Invalid value" );
	                }
                }
                catch( Exception e )
                {
                	// field.setText() might fail (but it shouldn't in our case).
                	try
                    {
	                    field.setText( formatter.valueToString( previousValue ) );
                    }
                    catch( ParseException e1 )
                    {
                    	// If it really failed, we just set a default value.
	                    field.setText( "1.05" );
                    }
                }
				
				// Let the field lose focus.
				return true;
			}
		} );
		dilatationField.setHorizontalAlignment( SwingConstants.RIGHT );
		JButton moreHorizontalDilatationButton = new JButton( "+ ↔" );
		JButton lessHorizontalDilatationButton = new JButton( "- ↔" );
		JButton moreVerticalDilatationButton = new JButton( "+ ↕" );
		JButton lessVerticalDilatationButton = new JButton( "- ↕" );
		
		moreHorizontalDilatationButton.addActionListener( new TransformationButtonListener( dilatationField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newScaling( this.doubleValue(), 1 );
			}
		} );
		lessHorizontalDilatationButton.addActionListener( new TransformationButtonListener( dilatationField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newScaling( 1 / this.doubleValue(), 1 );
			}
		} );
		moreVerticalDilatationButton.addActionListener( new TransformationButtonListener( dilatationField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newScaling( 1, this.doubleValue() );
			}
		} );
		lessVerticalDilatationButton.addActionListener( new TransformationButtonListener( dilatationField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newScaling( 1, 1 / this.doubleValue() );
			}
		} );
		
		JLabel transvectionLabel = new JLabel( "Transvection" );
		JFormattedTextField transvectionField = new JFormattedTextField( decimalFormat );
		transvectionField.setValue( 0.1 );
		transvectionField.setHorizontalAlignment( SwingConstants.RIGHT );
		JButton leftTransvectionButton = new JButton( "←" );
		JButton rightTransvectionButton = new JButton( "→" );
		JButton upTransvectionButton = new JButton( "↑" );
		JButton downTransvectionButton = new JButton( "↓" );
		
		leftTransvectionButton.addActionListener( new TransformationButtonListener( transvectionField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newShearX( -this.doubleValue() );
			}
		} );
		rightTransvectionButton.addActionListener( new TransformationButtonListener( transvectionField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newShearX( this.doubleValue() );
			}
		} );
		upTransvectionButton.addActionListener( new TransformationButtonListener( transvectionField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newShearY( this.doubleValue() );
			}
		} );
		downTransvectionButton.addActionListener( new TransformationButtonListener( transvectionField )
		{
			@Override
			public AffineTransformation getTransformation()
			{
				return AffineTransformation.newShearY( -this.doubleValue() );
			}
		} );
		
		JPanel editAffinePanel = new JPanel();
		GroupLayout editAffineLayout = new GroupLayout( editAffinePanel );
		
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
		
		/**
		 * Variations weights
		 */
		JPanel editWeightsPanel = new JPanel();
		GroupLayout editWeightsLayout = new GroupLayout( editWeightsPanel );
		
		JLabel linearLabel = new JLabel( "Linear" );
		final JFormattedTextField linearField = new JFormattedTextField( decimalFormat );
		
		JLabel sinusoidalLabel = new JLabel( "Sinusoidal" );
		final JFormattedTextField sinusoidalField = new JFormattedTextField( decimalFormat );
		
		JLabel sphericalLabel = new JLabel( "Spherical" );
		final JFormattedTextField sphericalField = new JFormattedTextField( decimalFormat );
		
		JLabel swirlLabel = new JLabel( "Swirl" );
		final JFormattedTextField swirlField = new JFormattedTextField( decimalFormat );
		
		JLabel horseshoeLabel = new JLabel( "Horseshoe" );
		final JFormattedTextField horseshoeField = new JFormattedTextField( decimalFormat );
		
		JLabel bubbleLabel = new JLabel( "Bubble" );
		final JFormattedTextField bubbleField = new JFormattedTextField( decimalFormat );
		
		// Update all variations weight when the transformation selection changes.
		this.addSelectedTransformationObserver( new TransformationSelectionObserver()
		{
			@Override
			public void selectionChanged()
			{
				int index = getSelectedTransformationIndex();
				
				linearField.setText( String.valueOf( builder.variationWeight( index, Variation.ALL_VARIATIONS.get( 0 ) ) ) );
				sinusoidalField.setText( String.valueOf( builder.variationWeight( index, Variation.ALL_VARIATIONS.get( 1 ) ) ) );
				sphericalField.setText( String.valueOf( builder.variationWeight( index, Variation.ALL_VARIATIONS.get( 2 ) ) ) );
				swirlField.setText( String.valueOf( builder.variationWeight( index, Variation.ALL_VARIATIONS.get( 3 ) ) ) );
				horseshoeField.setText( String.valueOf( builder.variationWeight( index, Variation.ALL_VARIATIONS.get( 4 ) ) ) );
				bubbleField.setText( String.valueOf( builder.variationWeight( index, Variation.ALL_VARIATIONS.get( 5 ) ) ) );
			}
		} );
		
		// Update the flame transformation everytime a variation weight is modified.
		linearField.addPropertyChangeListener( "value", new VariationWeightChangeListener( 0 ) );
		sinusoidalField.addPropertyChangeListener( "value", new VariationWeightChangeListener( 1 ) );
		sphericalField.addPropertyChangeListener( "value", new VariationWeightChangeListener( 2 ) );
		swirlField.addPropertyChangeListener( "value", new VariationWeightChangeListener( 3 ) );
		horseshoeField.addPropertyChangeListener( "value", new VariationWeightChangeListener( 4 ) );
		bubbleField.addPropertyChangeListener( "value", new VariationWeightChangeListener( 5 ) );
		
		editWeightsLayout.setHorizontalGroup(
			editWeightsLayout.createSequentialGroup()
				.addPreferredGap( ComponentPlacement.UNRELATED )
				.addGroup( editWeightsLayout.createParallelGroup( GroupLayout.Alignment.TRAILING )
					.addComponent( linearLabel )
					.addComponent( swirlLabel )
				)
				.addGroup( editWeightsLayout.createParallelGroup()
					.addComponent( linearField )
					.addComponent( swirlField )
				)
				.addGroup( editWeightsLayout.createParallelGroup( GroupLayout.Alignment.TRAILING )
					.addComponent( sinusoidalLabel )
					.addComponent( horseshoeLabel )
				)
				.addGroup( editWeightsLayout.createParallelGroup()
					.addComponent( sinusoidalField )
					.addComponent( horseshoeField )
				)
				.addGroup( editWeightsLayout.createParallelGroup( GroupLayout.Alignment.TRAILING )
					.addComponent( sphericalLabel )
					.addComponent( bubbleLabel )
				)
				.addGroup( editWeightsLayout.createParallelGroup()
					.addComponent( sphericalField )
					.addComponent( bubbleField )
				)
		);
		
		editWeightsLayout.setVerticalGroup(
			editWeightsLayout.createSequentialGroup()
				.addPreferredGap( ComponentPlacement.UNRELATED )
				.addGroup( editWeightsLayout.createParallelGroup( GroupLayout.Alignment.BASELINE )
					.addComponent( linearLabel )
					.addComponent( linearField )
					.addComponent( sinusoidalLabel )
					.addComponent( sinusoidalField )
					.addComponent( sphericalLabel )
					.addComponent( sphericalField )
				)
				.addGroup( editWeightsLayout.createParallelGroup( GroupLayout.Alignment.BASELINE )
					.addComponent( swirlLabel )
					.addComponent( swirlField )
					.addComponent( horseshoeLabel )
					.addComponent( horseshoeField )
					.addComponent( bubbleLabel )
					.addComponent( bubbleField )
				)
		);
		
		editWeightsPanel.setLayout( editWeightsLayout );
		
		/**
		 * The current transformation
		 */
		
		JPanel editCurrentPanel = new JPanel();
		editCurrentPanel.setBorder( BorderFactory.createTitledBorder( "Transformation courante" ) );
		editCurrentPanel.setLayout( new BoxLayout( editCurrentPanel, BoxLayout.PAGE_AXIS ) );
		editCurrentPanel.add( editAffinePanel );
		editCurrentPanel.add( new JSeparator() );
		editCurrentPanel.add( editWeightsPanel );
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout( new BoxLayout( bottomPanel, BoxLayout.LINE_AXIS ) );
		bottomPanel.add( editPanel );
		bottomPanel.add( editCurrentPanel );
		
		contentPane.add( topPanel, BorderLayout.CENTER );
		contentPane.add( bottomPanel, BorderLayout.PAGE_END );
		
		// Before showing the window, we pre-select the first transformation
		// to trigger the events and update the text fields values.
		transformList.setSelectedIndex( 0 );
		this.setSelectedTransformationIndex( 0 );
		
		// Causes the window to be sized to fit the preferred size and layouts of its subcomponents
		frame.pack();
		
		// Show the window
		frame.setVisible( true );
    }
	
	/**
	 * Remove an observer that was observing the selected transformation selection.
	 * 
	 * @param observer The observer to remove
	 */
	public void removeSelectedTransformationObserver( TransformationSelectionObserver observer )
    {
	    this.selectedTransformationObservers.remove( observer );
    }

	/**
	 * Add an observer which will be notified when the selection changes.
	 *  
	 * @param observer
	 */
	public void addSelectedTransformationObserver( TransformationSelectionObserver observer )
    {
		this.selectedTransformationObservers.add( observer );
    }

	/**
	 * Get the currently selected transformation's index.
	 */
	public int getSelectedTransformationIndex()
    {
	    return selectedTransformationIndex;
    }
	
	/**
	 * Notify the observer that the selection changed.
	 */
	private void fireSelectedTransformationChanged()
	{
		for( TransformationSelectionObserver observer : this.selectedTransformationObservers )
		{
			observer.selectionChanged();
		}
	}

	/**
	 * Set the currently selected transformation's by its index, and notify the watchers
	 * of the change.
	 * 
	 * @param selectedTransformationIndex
	 */
	public void setSelectedTransformationIndex( int selectedTransformationIndex )
    {
	    this.selectedTransformationIndex = selectedTransformationIndex;
	    this.fireSelectedTransformationChanged();
    }

	/**
	 * The model for the transformations JList.
	 * This class allow us to add and remove transformations from
	 * the list and takes care of returning a label for each transformation.
	 */
	protected class TransformationsListModel extends AbstractListModel
	{
		private static final long serialVersionUID = -8579595948096049981L;
		
		/**
		 * Add a transformation to the list and the builder.
		 */
		void addTransformation()
		{
			builder.addTransformation( FlameTransformation.IDENTITY() );
			
			int lastIndex = builder.transformationCount() - 1;
			this.fireIntervalAdded( this, lastIndex, lastIndex );
		}
		
		/**
		 * Remove a transformation from the list and the builder.
		 * 
		 * @param index The index of the transformation to remove.
		 */
		void removeTransformation( int index )
		{
			builder.removeTransformation( index );
			this.fireIntervalRemoved( this, index, index );
		}
		
		/**
		 * @see javax.swing.ListModel#getElementAt(int)
		 */
		@Override
        public String getElementAt( int index )
        {
	        return "Transformation n°" + ( index + 1 );
        }
		
		/**
		 * @see javax.swing.ListModel#getSize()
		 */
		@Override
        public int getSize()
        {
	        return builder.transformationCount();
        }
		
	}
	
	/**
	 * Observers that want to be notified when the selection changes
	 * must implement this interface, and register themselves
	 * with {@link FlameMakerGUI#addSelectedTransformationObserver(TransformationSelectionObserver)}
	 */
	public static interface TransformationSelectionObserver
	{
		/**
		 * Gets called when the transformation selection changes.
		 */
		void selectionChanged();
	}
	
	/**
	 * Listens to click on the buttons in the "Current transformation" panel
	 * and modify the current transformation accordingly.
	 * 
	 * Every specific listener has to implement a getTransformation() method
	 * that returns an AffineTransformation to compose the current one with.
	 */
	public abstract class TransformationButtonListener implements ActionListener
	{
		/**
		 * The text field holding the factor to modify the transformation by.
		 */
		protected JFormattedTextField field;
		
		public TransformationButtonListener( JFormattedTextField field )
		{
			this.field = field;
		}
		
		/**
		 * @return the transformation to compose the current one with.
		 */
		protected abstract AffineTransformation getTransformation();
		
		/**
		 * Replace the transformation currently selected by
		 * a new one composed of itself and a transformation
		 * that depends on which button has been clicked. 
		 */
		@Override
	    public void actionPerformed( ActionEvent e )
	    {
			int index = getSelectedTransformationIndex();
			AffineTransformation current = builder.affineTransformation( index );
			AffineTransformation transformation = this.getTransformation();
			
			if( transformation != null )
			{
				builder.setAffineTransformation(
					index,
					transformation.composeWith( current )
				);
			}
	    }
		
		/**
		 * @return The associated text field's value as a Double.
		 */
		protected double doubleValue()
		{
			return ( ( Number )this.field.getValue() ).doubleValue();
		}

	}
	
	/**
	 * This class tells the builder to updates a Variation's weight when the user modify
	 * the matching text field. It implements the Strategy design pattern to avoid
	 * to uselessly duplicate a lot of code when only the variation index varies amongst
	 * the different listeners.
	 */
	public class VariationWeightChangeListener implements PropertyChangeListener
	{
		/**
		 * The index of the variation to update.
		 */
		private int variationIndex;
		
		/**
		 * Create a new variation weight listener that
		 * will update the variation specified by its index.
		 * 
		 * @param variationIndex The index of the variation to udpate.
		 */
		public VariationWeightChangeListener( int variationIndex )
		{
			this.variationIndex = variationIndex;
		}
		
		/**
		 * Update the variation weight when the property it listens to changes.
		 * 
		 * @param e The event holding the new value
		 */
		@Override
        public void propertyChange( PropertyChangeEvent e )
        {
			double value = ( ( Number )( e.getNewValue() ) ).doubleValue();
			builder.setVariationWeight(
				getSelectedTransformationIndex(),
				Variation.ALL_VARIATIONS.get( this.variationIndex ),
				value
			);
        }
	}

	
}
