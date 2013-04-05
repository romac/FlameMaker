package ch.epfl.flamemaker.flame;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.util.PGMWriter;

public class FlamePPMMaker
{

	public static void main( String[] args )
	{
		// FlameMaker.barnsley();
		FlamePPMMaker.sharkfin();
		FlamePPMMaker.turbulence();
	}
	
	private static void sharkfin()
	{
		System.out.print( "Generating Sharkfin fractal..." );
		
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
                new double[] { 0, 0, 0, 0, 0.8, 1 }
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
        
        System.out.println( "Done." );
	}
	
	private static void turbulence()
	{
		System.out.print( "Generating Turbulence fractal..." );
		
		List<FlameTransformation> transformations = Arrays.asList(
			new FlameTransformation(
                new AffineTransformation(
                	0.7124807, -0.4113509, -0.3,
                	0.4113513,  0.7124808, -0.7
                ),
                new double[] { 0.5, 0, 0, 0.4, 0, 0 }
            ),
            new FlameTransformation(
                new AffineTransformation(
                	0.3731079, -0.6462417, 0.4,
                	0.6462414,  0.3731076, 0.3
                ),
                new double[] { 1, 0, 0.1, 0, 0, 0 }
           ),
           new FlameTransformation(
                new AffineTransformation(
                	0.0842641, -0.314478, -0.1,
                	0.3144780,  0.0842641, 0.3
                ),
                new double[] { 1, 0, 0, 0, 0, 0 }
            )
        );
        
		Flame flame = new Flame( transformations );
        Rectangle frame = new Rectangle( new Point( 0.1, 0.1 ), 3, 3 );
        FlameAccumulator accumulator = flame.compute( frame, 500, 500, 50 );
        
        PGMWriter writer;
        try {
            writer = new PGMWriter ( "resources/turbulence.pgm" );
            writer.printAccumulator( accumulator );
        }
        catch( FileNotFoundException e ) {
            e.printStackTrace();
        }
        System.out.println( "Done." );
	}
	
	@SuppressWarnings( "unused" )
    private static void barnsley()
    {
		System.out.print( "Generating Barnsely fractal..." );
		
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
        
        System.out.println( "Done." );
    }
	
}
