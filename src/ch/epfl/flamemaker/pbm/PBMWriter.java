package ch.epfl.flamemaker.pbm;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import ch.epfl.flamemaker.ifs.IFSAccumulator;

public class PBMWriter
{
	
	private String fileName;
	private PrintStream stream; 

	public PBMWriter( String fileName ) throws FileNotFoundException
	{
		this.fileName = fileName;
		this.stream = new PrintStream( this.fileName );
	}
	
	public void printAccumulator( IFSAccumulator ifs )
	{
		this.stream.println( "P1" );
		this.stream.println( ifs.width() + " " + ifs.height() );
		
		for( int i = ifs.height() - 1; i >= 0; i-- )
		{
			for( int j = 0; j < ifs.width(); j++ )
			{
			    this.stream.print( ifs.isHit( j, i ) ? "1 " : "0 " );
			}
			
			this.stream.println();
		}
		
		this.stream.close();
	}
}
