package rnn;

import java.util.*;

public class Point implements Cloneable, Comparator, Comparable<Point> 
{
    private double x = 0, y = 0;

    public Point(double X, double Y){ 
        x = X; y = Y; 
    }

    public double GetX() {
        return x;
    }

    public double GetY() {
        return y;
    }

    public Object clone() {
        return new Point(x,y);
    }

    public String toString() {
        return new String("x = " + x + " y = " + y + "\n");
    }

    public double GetDist(Point p){
        return Math.sqrt( ((p.GetX()-x) * (p.GetX()-x)) + ( (p.GetY()-y) * (p.GetY()-y) ) );
    }

    public double GetDistFast(Point p){
        return ( ((p.GetX()-x) * (p.GetX()-x)) + ( (p.GetY()-y) * (p.GetY()-y) ) );
    }

    public int compare(Object o1, Object o2){
        Point p1 = (Point)o1;
        Point p2 = (Point)o2;

        if(p1.GetX() == p2.GetX()){
            return new Double(p1.GetY()).compareTo(p2.GetY());
        }

        return new Double(p1.GetX()).compareTo(p2.GetX());
    }

    public int compareTo(Point p){
        if(this.GetX() == p.GetX()){
            return new Double(this.GetY()).compareTo(p.GetY());
        }
        
        return new Double(this.GetX()).compareTo(p.GetX());
    }
}


