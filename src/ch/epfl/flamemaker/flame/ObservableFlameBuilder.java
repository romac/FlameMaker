package ch.epfl.flamemaker.flame;

import java.util.ArrayList;

import ch.epfl.flamemaker.flame.Flame.Builder;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;

// TODO: Implements Observable

public class ObservableFlameBuilder
{
	
	private Builder builder;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	public ObservableFlameBuilder( Flame.Builder builder )
	{
		this.builder = builder;
	}
	
	public void addObserver( Observer observer )
	{
		this.observers.add( observer );
	}
	
	public void removeObserver( Observer observer )
	{
		this.observers.remove( observer );
	}
	
	public void notifyObservers()
	{
		for( Observer observer : this.observers )
		{
			observer.flameBuilderChanged();
		}
	}

	public int transformationCount()
	{
		return this.builder.transformationCount();
	}

	public void addTransformation( FlameTransformation transformation )
	{
		this.builder.addTransformation( transformation );
		this.notifyObservers();
	}

	public AffineTransformation affineTransformation( int index )
	{
		return this.builder.affineTransformation( index );
	}

	public void setAffineTransformation( int index, AffineTransformation transformation )
	{
		this.builder.setAffineTransformation( index, transformation );
		this.notifyObservers();
	}

	public double variationWeight( int index, Variation variation )
	{
		return this.builder.variationWeight( index, variation );
	}

	public void setVariationWeight( int index, Variation variation, double weight )
	{
		this.builder.setVariationWeight( index, variation, weight );
		this.notifyObservers();
	}

	public void removeTransformation( int index )
	{
		this.builder.removeTransformation( index );
		this.notifyObservers();
	}
	
	public Flame build()
	{
		return this.builder.build();
	}
	
	public static interface Observer
	{
		void flameBuilderChanged();
	}
	
}
