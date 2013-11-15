package com.geocloud.point;

import java.util.*;

public class Point implements Cloneable, Comparator
{
    private long x = 0, y = 0;

    public Point(long X, long Y){ 
        x = X; y = Y; 
    }

    public long GetX() {
        return x;
    }

    public long GetY() {
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

    public long GetDistFast(Point p){
        return ( ((p.GetX()-x) * (p.GetX()-x)) + ( (p.GetY()-y) * (p.GetY()-y) ) );
    }

    public int compare(Object o1, Object o2){
        Point p1 = (Point)o1;
        Point p2 = (Point)o2;

        if(p1.GetX() == p2.GetX()){
            return new Long(p1.GetY()).compareTo(p2.GetY());
        }

        return new Long(p1.GetX()).compareTo(p2.GetX());
    }
}


