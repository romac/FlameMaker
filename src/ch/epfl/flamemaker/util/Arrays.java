package ch.epfl.flamemaker.util;

public class Arrays
{

	public static boolean[][] copyOf2DArray( boolean[][] array )
	{
		boolean[][] copy = new boolean[ array.length ][ array[ 0 ].length ];
		
		for( int i = 0; i < array.length; i++ )
        {
	        for( int j = 0; j < array[ 0 ].length; j++ )
            {
	            copy[ i ][ j ] = array[ i ][ j ];
            }
        }
		
		return copy;
	}
	
}
