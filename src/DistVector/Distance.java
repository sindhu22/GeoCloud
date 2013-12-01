
import java.util.*;

public class Distance implements Cloneable, Comparator, Comparable<Distance> 
{
    private Point left;
    private Point right;
    private float distance;

    public Distance(Point pl, Point pr){ 
        left = pl;
        right = pr;
        distance = left.GetDist(right);
    }

    public Object clone() {
        return new Distance(left, right);
    }
    
    public Point GetLeft() {
        return left;
    }

    public Point GetRight() {
        return right;
    }

    public float GetDistance() {
        return distance;
    }

    public String toString() {
	    /*
        return new String("x1 = " + left.GetX() + " y1 = " + left.GetY() + " x2 = " + right.GetX() + " y2 = " +
        left.GetY() + " distance = " + distance + "\n"); */
	    return new String(left.GetX() + " " + left.GetY() + " " + distance);
    }

    public int compare(Object o1, Object o2){
        Distance d1 = (Distance)o1;
        Distance d2 = (Distance)o2;

        return new Float(d1.GetDistance()).compareTo(d2.GetDistance());
    }

    public int compareTo(Distance p){
        return new Float(this.GetDistance()).compareTo(p.GetDistance());
    }
}


