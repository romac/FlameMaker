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
	
	public Point transformPoint(Point p) {
		return new Point (this.a*p.x()+this.b*p.y()+this.c, this.d*p.x()+this.c*p.y()+this.f);
	}
	
	static AffineTransformation newTranslation(double dx, double dy){
		 return new AffineTransformation(1, 0, dx, 0, 1, dy);
		
	}
	static AffineTransformation newRotation (double theta){
		return new AffineTransformation(Math.cos(theta), -Math.sin(theta), 0, Math.sin(theta), Math.cos(theta), 0);
	}
	static AffineTransformation newScaling(double sx, double sy){
		return new AffineTransformation(sx, 0, 0, 0, sy, 0);
	}
	static AffineTransformation newShearX(double sx){
		return new AffineTransformation (1, sx, 0, 0, 1, 0);
	}
	static AffineTransformation newShearY(double sy){
		return new AffineTransformation (1, 0, 0, sy, 1, 0);
	}
	static AffineTransformation IDENTITY (){
		return new AffineTransformation (1, 0, 0, 0, 1,0);
	}
	
	
}
