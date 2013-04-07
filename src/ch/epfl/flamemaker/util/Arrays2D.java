package ch.epfl.flamemaker.util;

/**
 * Holds a few useful methods for copying 2D arrays
 * as well as finding the max value of a 2D array.
 * @author romac
 *
 */
public class Arrays2D
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

	public static int[][] copyOf2DArray( int[][] array )
	{
		int[][] copy = new int[ array.length ][ array[ 0 ].length ];

		for( int i = 0; i < array.length; i++ )
		{
			for( int j = 0; j < array[ 0 ].length; j++ )
			{
				copy[ i ][ j ] = array[ i ][ j ];
			}
		}

		return copy;
	}

	public static double[][] copyOf2DArray( double[][] array )
	{
		double[][] copy = new double[ array.length ][ array[ 0 ].length ];

		for( int i = 0; i < array.length; i++ )
		{
			for( int j = 0; j < array[ 0 ].length; j++ )
			{
				copy[ i ][ j ] = array[ i ][ j ];
			}
		}

		return copy;
	}

	public static int maxOf2DArray( int[][] array )
	{
		int max = Integer.MIN_VALUE;

		for( int i = 0; i < array.length; i++ )
		{
			for( int j = 0; j < array[ 0 ].length; j++ )
			{
				if( array[ i ][ j ] > max )
				{
					max = array[ i ][ j ];
				}
			}
		}

		return max;
	}

}
