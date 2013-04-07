package ch.epfl.flamemaker.util;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.FlameAccumulator;

/**
 * Dump a Flame accumulator into a PPM file.
 */
public class PPMWriter
{
	
	private String fileName;
	private PrintStream stream; 

	public PPMWriter( String fileName ) throws FileNotFoundException
	{
		this.fileName = fileName;
		this.stream = new PrintStream( this.fileName );
	}
	
	public void printAccumulator( FlameAccumulator flame, Palette palette, Color bg )
	{
		this.stream.println( "P3" );
		this.stream.println( flame.width() + " " + flame.height() );
		this.stream.println( "100" );
		
		for( int i = flame.height() - 1; i >= 0; i-- )
		{
			for( int j = 0; j < flame.width(); j++ )
			{
				Color color = flame.color( palette, bg, j, i );
				int r = Color.sRGBEncode( color.red(), 100 );
				int g = Color.sRGBEncode( color.green(), 100 );
				int b = Color.sRGBEncode( color.blue(), 100 );
				
				
				this.stream.print( r + " " + g + " " + b + " " );
			}
			
			this.stream.println();
		}
		
		this.stream.close();
	}
}
