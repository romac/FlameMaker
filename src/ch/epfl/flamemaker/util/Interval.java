package ch.epfl.flamemaker.util;

public class Interval
{
	
	public static boolean contains( double n, double min, double max )
	{
		return n >= min && n <= max;
	}
	
}
