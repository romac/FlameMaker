package ch.epfl.flamemaker.ifs;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import ch.epfl.flamemaker.pbm.*;

import ch.epfl.flamemaker.geometrie2d.AffineTransformation;
import ch.epfl.flamemaker.geometrie2d.Point;
import ch.epfl.flamemaker.geometrie2d.Rectangle;

public class IFSMaker
{

	
	public static void main(String[] args)
	{
		
		ArrayList<AffineTransformation> transformations = new ArrayList<AffineTransformation>();
		
		AffineTransformation A1 = new AffineTransformation(
		    .5, 0, 0,
		    0, .5, 0
		);
		
		AffineTransformation A2 = new AffineTransformation(
	        .5, 0, .5,
	        0, .5, 0
	    );
		
		AffineTransformation A3 = new AffineTransformation(
	        .5, 0, .25,
	        0, .5, .5
	    );
		
		transformations.add( A1 );
		transformations.add( A2 );
		transformations.add( A3 );
		
		IFS ifs = new IFS( transformations );
		Rectangle frame = new Rectangle( new Point( .5, .5 ), 1, 1 );
		
		IFSAccumulator accumulator = ifs.compute( frame, 100, 100, 1 );
		
		PBMWriter writer;
        try {
            writer = new PBMWriter( "sierpinski.pbm" );
            writer.printAccumulator( accumulator );
        }
        catch( FileNotFoundException e ) {
            e.printStackTrace();
        }
	}

}
