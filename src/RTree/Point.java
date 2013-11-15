package com.geocloud.point;


public class Point implements Cloneable
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
}


