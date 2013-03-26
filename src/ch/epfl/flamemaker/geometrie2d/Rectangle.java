package ch.epfl.flamemaker.geometrie2d;

public class Rectangle
{

    private Point center;
    private double width;
    private double height;

    public Rectangle( Point center, double width, double height )
    {

        if( width <= 0 || height <= 0 ) {
            throw new IllegalArgumentException( "Width and height cannot be null nor negative");
        }
        
        this.width = width;
        this.height = height;
        this.center = center;
    }

    public double left()
    {
        return this.center.x() - ( this.width / 2 );
    }

    public double right()
    {
        return this.center.x() + ( this.width / 2 );
    }

    public double bottom()
    {
        return this.center.y() - ( height / 2 );
    }

    public double top()
    {
        double y = center.y();
        return y + ( height / 2 );
    }

    public double width()
    {
        return width;
    }

    public double height()
    {
        return height;
    }

    public Point center()
    {
        return center;
    }

    public boolean contains(Point p)
    {
        double x = p.x();
        double y = p.y();

        double xmin = this.left();
        double xmax = this.right();
        double ymin = this.bottom();
        double ymax = this.top();

        return xmin <= x && x < xmax && ymin <= y && y < ymax;

    }

    public double aspectRatio()
    {
        return this.width / this.height;
    }

    public Rectangle expandToAspectRatio( double newAspectRatio )
    {
        if( newAspectRatio <= 0 )
        {
            throw new IllegalArgumentException( "aspectRatio cannot be null nor negative." );
        }

        double newHeight = this.height();
        double newWidth = this.width();

        if( newAspectRatio > 1 )
        {
            newWidth = this.height * newAspectRatio;
        }
        else if ( newAspectRatio < 1 )
        {
            newHeight = this.width() / newAspectRatio;
        }
        
        return new Rectangle( this.center(), newWidth, newHeight );
    }

    public String toString()
    {
        return "( " + center + ", " + this.width() + ", " + this.height() + " )";
    }
}
