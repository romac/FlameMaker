package ch.epfl.flamemaker.color;

/**
 * Represents a color palette.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public interface Palette
{
	
	/**
	 * Return the color associated with the given index.
	 *  
	 * @return A color
	 */
	Color colorForIndex( double index );
	
}
