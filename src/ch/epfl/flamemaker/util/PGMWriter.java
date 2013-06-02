package ch.epfl.flamemaker.util;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import ch.epfl.flamemaker.flame.FlameAccumulator;

/**
 * Dump a Flame accumulator into a PGM file.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public class PGMWriter
{
	
	private String fileName;
	private PrintStream stream; 

	public PGMWriter( String fileName ) throws FileNotFoundException
	{
		this.fileName = fileName;
		this.stream = new PrintStream( this.fileName );
	}
	
	public void printAccumulator( FlameAccumulator flame )
	{
		this.stream.println( "P2" );
		this.stream.println( flame.width() + " " + flame.height() );
		this.stream.println( "100" );
		
		for( int i = flame.height() - 1; i >= 0; i-- )
		{
			for( int j = 0; j < flame.width(); j++ )
			{
			    this.stream.print( Math.round( flame.intensity( j, i ) * 100 ) + " " );
			}
			
			this.stream.println();
		}
		
		this.stream.close();
	}
}
