/*  *	Author:      Moi  *	Date:        Aujourd'hui  */

public class Rectangle {
	
	private  Point centre;
	private  double width;
	private  double height;

	Rectangle (Point center,double width, double height){
	
		if (width > 0 && height > 0){
		this.width = width;
		this.height = height;
		this.centre = center;
		}
		else{System.out.print("Données éronées");}
	}
	
	public double left(){
		return this.centre.x() - (this.width/2);
	}
	public double right(){
		return this.centre.x() + (this.width/2);
	}
	public double bottom(){
		return this.centre.y() - (height/2);
	}
	public double top(){
		double y = centre.y();
		return y + (height/2);
	}
	public double width(){
		return width;
	}
	public double height(){
		return height;
	}
	public Point center(){
		return centre;
	}
	public boolean contains(Point p){
		double x = p.x();
		double y = p.y();
	
		double xmin = this.left();
		double xmax = this.right();
		double ymin = this.bottom();
		double ymax = this.top();
		
	if (xmin <= x && x < xmax && ymin <= y && y < ymax){
		return true;
	}
	else return false;
	
	}
	public double aspectRatio(){
		return 	this.width/this.height;
	}
	public Rectangle expandToAspectRatio(double newAspectRatio)
	{
		if (newAspectRatio <= 0){
			throw new IllegalArgumentException("aspectRatio cannot be null nor negative.");
		}
		
		double newHeight = this.height();
		double newWidth = this.width();
		
		if (newAspectRatio > 1){
			newWidth = this.height*newAspectRatio;
		} 
		else if(newAspectRatio < 1) {
			newHeight = this.width()/newAspectRatio;			
		}
		
		Rectangle expanded = new Rectangle (this.center(), newWidth, newHeight);
		return expanded;
	}
	
}


// try {
// 	rect.expandToAspectRatio( -1 );
// }
// catch( Exception e ) {
//	System.out.println( "Error: " + e.getMessage() );
// }
