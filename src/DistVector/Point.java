
import java.util.*;

public class Point implements Cloneable, Comparator, Comparable<Point> 
{
    private float x = 0, y = 0;

    public Point(float X, float Y){ 
        x = X; y = Y; 
    }

    public float GetX() {
        return x;
    }

    public float GetY() {
        return y;
    }

    public Object clone() {
        return new Point(x,y);
    }

    public String toString() {
	    /*
        return new String("x = " + x + " y = " + y + "\n");
	*/
	    return new String("(" + x + ", " + y + ")");
    }

    public float GetDist(Point p){
        return (float)Math.sqrt( ((p.GetX()-x) * (p.GetX()-x)) + ( (p.GetY()-y) * (p.GetY()-y) ) );
    }

    public float GetDistFast(Point p){
        return ( ((p.GetX()-x) * (p.GetX()-x)) + ( (p.GetY()-y) * (p.GetY()-y) ) );
    }

    public int compare(Object o1, Object o2){
        Point p1 = (Point)o1;
        Point p2 = (Point)o2;

        if(p1.GetX() == p2.GetX()){
            return new Float(p1.GetY()).compareTo(p2.GetY());
        }

        return new Float(p1.GetX()).compareTo(p2.GetX());
    }

    public int compareTo(Point p){
        if(this.GetX() == p.GetX()){
            return new Float(this.GetY()).compareTo(p.GetY());
        }
        
        return new Float(this.GetX()).compareTo(p.GetX());
    }
}


