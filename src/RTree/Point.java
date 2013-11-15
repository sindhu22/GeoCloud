package com.geocloud.point;


public class Point implements Cloneable
{
    private int x = 0, y = 0;

    public Point(int X, int Y){ 
        x = X; y = Y; 
    }

    public int GetX() {
        return x;
    }

    public int GetY() {
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
}


