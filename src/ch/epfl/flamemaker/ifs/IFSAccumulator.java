package ch.epfl.flamemaker.ifs;
import ch.epfl.flamemaker.util.Arrays;

public class IFSAccumulator
{
	
	// TODO: Rename to isHit?
	private boolean[][] hitMap;
	private int width;
	private int height;
	
	IFSAccumulator( boolean[][] hitMap )
	{
		this.hitMap = Arrays.copyOf2DArray( hitMap );
		
		// TODO: Delete?
		this.width = this.hitMap.length; 
		this.height = this.hitMap[ 0 ].length;
	}
	
	public int width()
	{
		return this.width;
	}
	
	public int height()
	{
		return this.height;
	}
	
	public boolean isHit( int x, int y )
	{
		if( x < 0 || x > this.width() - 1 ) {
			throw new IndexOutOfBoundsException( "x (" + x + ") is out of bounds (" + this.width + "x" + this.height + ")." );
		}
		
		if( y < 0 || y > this.height() - 1 ) {
			throw new IndexOutOfBoundsException( "y (" + y + ") is out of bounds (" + this.width + "x" + this.height + ")." );
		}
		
		return this.hitMap[ x ][ y ];
	}
	
}
