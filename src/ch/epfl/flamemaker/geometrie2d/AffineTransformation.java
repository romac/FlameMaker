package ch.epfl.flamemaker.geometrie2d;

public class AffineTransformation implements Transformation {
	
	private double a, b, c, d, e, f;
	
	AffineTransformation (double a, double b, double c, double d, double e, double f){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
	}
	
	
}
