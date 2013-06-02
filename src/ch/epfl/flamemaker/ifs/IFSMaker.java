package ch.epfl.flamemaker.ifs;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Arrays;

import ch.epfl.flamemaker.util.PBMWriter;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public class IFSMaker
{
    
    public static void main(String[] args)
    {
        IFSMaker.sierpinsky();
        IFSMaker.barnsley();
    }
    
    private static void barnsley()
    {
        List<AffineTransformation> transformations = Arrays.asList(
            new AffineTransformation(
                0, 0,   0,
                0, 0.16, 0
            ),  
            new AffineTransformation(
                0.2, -0.26, 0,
                0.23, 0.22, 1.6
            ),      
            new AffineTransformation(
                -0.15, 0.28, 0,
                0.26, 0.24, 0.44
            ),      
            new AffineTransformation(
                0.85,  0.04, 0,
                -0.04, 0.85, 1.6
            )
        );
        
        IFS ifs = new IFS( transformations );
        Rectangle frame = new Rectangle( new Point( 0, 4.5 ), 6, 10 );
        
        IFSAccumulator accumulator = ifs.compute( frame, 120, 200, 150 );
        
        PBMWriter writer;
        try {
            writer = new PBMWriter( "resources/barnsley.pbm" );
            writer.printAccumulator( accumulator );
        }
        catch( FileNotFoundException e ) {
            e.printStackTrace();
        }
    }
    
    private static void sierpinsky()
    {
        List<AffineTransformation> transformations = Arrays.asList(
            new AffineTransformation(
                0.5, 0, 0,
                0, 0.5, 0
            ),
            new AffineTransformation(
                0.5, 0, 0.5,
                0, 0.5, 0
            ),
            new AffineTransformation(
                0.5, 0, 0.25,
                0, 0.5, 0.5
            )
        );
        
        IFS ifs = new IFS( transformations );
        Rectangle frame = new Rectangle( new Point( .5, .5 ), 1, 1 );
        
        IFSAccumulator accumulator = ifs.compute( frame, 100, 100, 1 );
        
        PBMWriter writer;
        try {
            writer = new PBMWriter( "resources/sierpinski.pbm" );
            writer.printAccumulator( accumulator );
        }
        catch( FileNotFoundException e ) {
            e.printStackTrace();
        }
    }

}
