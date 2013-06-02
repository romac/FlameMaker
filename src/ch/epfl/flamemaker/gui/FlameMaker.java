package ch.epfl.flamemaker.gui;

/**
 * The main class of the program.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public class FlameMaker
{

	/**
	 * The entry point of the program, that will run {@link FlameMakerGUI#start()}
	 * on the event dispatching thread.
	 *  
	 * @param args
	 */
	public static void main( String[] args )
	{
		javax.swing.SwingUtilities.invokeLater( new Runnable()
		{
	        public void run()
	        {
	            new FlameMakerGUI().start();
	        }
	    } );
	}

}
