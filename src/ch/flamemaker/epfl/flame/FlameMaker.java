package ch.flamemaker.epfl.flame;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.util.PGMWriter;

public class FlameMaker
{

	public static void main( String[] args )
	{
		FlameMaker.sharkFin();
		// FlameMaker.barnsley();
	}
	
	private static void sharkFin()
	{
		List<FlameTransformation> transformations = Arrays.asList(
			new FlameTransformation(
                new AffineTransformation(
                	-0.4113504, -0.7124804, -0.4,
                	 0.7124795, -0.4113508,  0.8
                ),
                new double[] { 1, 0.1, 0, 0, 0, 0 }
            ),
            new FlameTransformation(
                new AffineTransformation(
                    -0.3957339,  0, 		-1.6,
                     0, 		-0.3957337,  0.2
                ),
                new double[] { 0, 0, 0, 0.8, 1, 0 }
           ),
           new FlameTransformation(
                new AffineTransformation(
                	0.4810169, 0, 		  1,
                    0, 		   0.4810169, 0.9
                ),
                new double[] { 1, 0, 0, 0, 0, 0 }
            )
        );
        
		Flame flame = new Flame( transformations );
        Rectangle frame = new Rectangle( new Point( -0.25, 0 ), 5, 4 );
        FlameAccumulator accumulator = flame.compute( frame, 500, 400, 50 );
        
        PGMWriter writer;
        try {
            writer = new PGMWriter ( "resources/sharkfin.pgm" );
            writer.printAccumulator( accumulator );
        }
        catch( FileNotFoundException e ) {
            e.printStackTrace();
        }
	}
	
	@SuppressWarnings( "unused" )
    private static void barnsley()
    {
        List<FlameTransformation> transformations = Arrays.asList(
        	new FlameTransformation(
                new AffineTransformation(
                    0, 0,   0,
                    0, 0.16, 0
                ),
                new double[] { 1, 0, 0, 0, 0, 0 }
            ),
            new FlameTransformation(
                new AffineTransformation(
                    0.2, -0.26, 0,
                    0.23, 0.22, 1.6
                ),
                new double[] { 1, 0, 0, 0, 0, 0 }
            ),
            new FlameTransformation(
                new AffineTransformation(
                    -0.15, 0.28, 0,
                    0.26, 0.24, 0.44
                ),
                new double[] { 1, 0, 0, 0, 0, 0 }
           ),
           new FlameTransformation(
                new AffineTransformation(
                    0.85,  0.04, 0,
                    -0.04, 0.85, 1.6
                ),
                new double[] { 1, 0, 0, 0, 0, 0 }
           )
        );
        
        Flame flame = new Flame( transformations );
        Rectangle frame = new Rectangle( new Point( 0, 4.5 ), 6, 10 );
        
        FlameAccumulator accumulator = flame.compute( frame, 120, 200, 150 );
        
        PGMWriter writer;
        try {
            writer = new PGMWriter( "resources/barnsley.pgm" );
            writer.printAccumulator( accumulator );
        }
        catch( FileNotFoundException e ) {
            e.printStackTrace();
        }
    }
	
}
