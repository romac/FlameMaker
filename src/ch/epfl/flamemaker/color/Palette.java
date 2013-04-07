package ch.epfl.flamemaker.color;

/**
 * Represents a color palette.
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
