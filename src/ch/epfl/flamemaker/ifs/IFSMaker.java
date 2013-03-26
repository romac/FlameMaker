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
		IFSMaker.sierpinsky();
		IFSMaker.barnsley();
	}

	private static void barnsley()
	{
		ArrayList<AffineTransformation> transformations = new ArrayList<AffineTransformation>();

		AffineTransformation A1 = new AffineTransformation(
		    0, 0,   0,
		    0, 0.16, 0
		);

		AffineTransformation A2 = new AffineTransformation(
	        0.2, -0.26, 0,
	        0.23, 0.22, 1.6
	    );

		AffineTransformation A3 = new AffineTransformation(
	        -0.15, 0.28, 0,
	        0.26, 0.24, 0.44
	    );

		AffineTransformation A4 = new AffineTransformation(
			0.85,  0.04, 0,
			-0.04, 0.85, 1.6
		);
		
		transformations.add( A1 );
		transformations.add( A2 );
		transformations.add( A3 );
		transformations.add( A4 );

		IFS ifs = new IFS( transformations );
		Rectangle frame = new Rectangle( new Point( 0, 4.5 ), 6, 10 );

		IFSAccumulator accumulator = ifs.compute( frame, 120, 200, 150 );

		PBMWriter writer;
        try {
            writer = new PBMWriter( "resources/barnsley.pbm" );
            writer.printAccumulator( accumulator, true );
        }
        catch( FileNotFoundException e ) {
            e.printStackTrace();
        }
	}

	private static void sierpinsky()
	{
		ArrayList<AffineTransformation> transformations = new ArrayList<AffineTransformation>();
		
		AffineTransformation A1 = new AffineTransformation(
		    0.5, 0, 0,
		    0, 0.5, 0
		);
		
		AffineTransformation A2 = new AffineTransformation(
	        0.5, 0, 0.5,
	        0, 0.5, 0
	    );
		
		AffineTransformation A3 = new AffineTransformation(
	        0.5, 0, 0.25,
	        0, 0.5, 0.5
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
