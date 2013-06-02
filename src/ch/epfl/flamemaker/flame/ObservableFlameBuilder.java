package ch.epfl.flamemaker.flame;

import java.util.Observable;

import ch.epfl.flamemaker.flame.Flame.Builder;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;

/**
 * A decorator for {@link Flame.Builder} that is makes it {@link Observable}.
 * 
 * @author Romain Ruetschi #218357
 * @author Arthur Passuello #229261
 */
public class ObservableFlameBuilder extends Observable
{
	
	private Builder builder;
	
	/**
	 * Set the builder to make observable.
	 * 
	 * @param builder
	 */
	public ObservableFlameBuilder( Flame.Builder builder )
	{
		this.builder = builder;
	}
	
	/**
	 * @see Flame.Builder#transformationCount()
	 */
	public int transformationCount()
	{
		return this.builder.transformationCount();
	}

	/**
	 * @see Flame.Builder#addTransformation(FlameTransformation)
	 */
	public void addTransformation( FlameTransformation transformation )
	{
		this.builder.addTransformation( transformation );
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * @see Flame.Builder#affineTransformation(int)
	 */
	public AffineTransformation affineTransformation( int index )
	{
		return this.builder.affineTransformation( index );
	}
	
	/**
	 * @see Flame.Builder#setAffineTransformation(int, AffineTransformation)
	 */
	public void setAffineTransformation( int index, AffineTransformation transformation )
	{
		this.builder.setAffineTransformation( index, transformation );
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * @see Flame.Builder#variationWeight(int, Variation)
	 */
	public double variationWeight( int index, Variation variation )
	{
		return this.builder.variationWeight( index, variation );
	}

	/**
	 * @see Flame.Builder#setVariationWeight(int, Variation, double)
	 */
	public void setVariationWeight( int index, Variation variation, double weight )
	{
		this.builder.setVariationWeight( index, variation, weight );
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * @see Flame.Builder#removeTransformation(int)
	 */
	public void removeTransformation( int index )
	{
		this.builder.removeTransformation( index );
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * @see Flame.Builder#build()
	 */
	public Flame build()
	{
		return this.builder.build();
	}
	
}
