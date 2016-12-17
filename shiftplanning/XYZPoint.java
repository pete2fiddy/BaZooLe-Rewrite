/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shiftplanning;

import java.awt.Color;
import java.awt.Graphics;

/*holds x, y, and z coords for ease of access, along with their bound 
 */
public class XYZPoint implements ThreeDDrawable, Updatable
{
    /*** might be better to use x y z instead of an array ***/
    private double[] point = new double[3];
    private XYPoint precalculatedPointProjection = new XYPoint(0,0);
    private Plane boundPlane;
    public XYZPoint(Plane boundPlaneIn, double x, double y, double z)
    {
        boundPlane = boundPlaneIn;
        point[0] = x;
        point[1] = y;
        point[2] = z;
        update();
    }
    
    public XYZPoint()//creates an empty bound point with no bound plane
    {
        point[0] = 0;
        point[1] = 0;
        point[2] = 0;
    }
    
    /*
    Getters and setters for XYZ and boundPlane, including one which returns the point as an array in the form {x,y,z};
    */
    public double[] getPointArray()
    {
        return point;
    }
    
    public double getX()
    {
        return point[0];
    }
    
    public double getY()
    {
        return point[1];
    }
    
    public double getZ()
    {
        return point[2];
    }
    
    public static double getTotalMagnitudeBetweenPoints(XYZPoint p1, XYZPoint p2)
    {
        return Math.sqrt( Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2) + Math.pow(p2.getZ() - p1.getZ(), 2) );
    }
    
    public static double getXYMagnitudeBetweenPoints(XYZPoint p1, XYZPoint p2)
    {
        return Math.sqrt( Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2) );
    }

    @Override
    public void draw(Graphics g) 
    {
        XYPoint screenPoint = boundPlane.convertCoordsToPoint(this);
        g.fillOval((int)screenPoint.getX() - 5, (int)screenPoint.getY() - 5, 10, 10);
    }
    
    public void drawLineToPoint(Graphics g, XYZPoint xyzPointIn)
    {
        int[] p1 = precalculatedPointProjection.getPointAsIntArray();
        int[] p2 = xyzPointIn.getPrecalculatedPointProjection().getPointAsIntArray();
        g.drawLine(p1[0], p1[1], p2[0], p2[1]);
    }
    
    public XYPoint getPrecalculatedPointProjection()
    {
        return precalculatedPointProjection;
    }

    @Override
    public double getSortDistanceConstant() 
    {
        double constant = 0;
        double slope = 0;
        switch(boundPlane.getSpinQuadrant().getSpinQuadrantNum() + 1)
        {
            case 1://works
                slope = -1;
                constant = getY()-(slope*getX());
                break;
            case 2://works
                slope = 1;
                constant = -(getY()-(slope*getX()));
                break;
            case 3:
                slope = -1;
                constant = -(getY()-(slope*getX()));
                break;
            case 4:
                slope = 1;
                constant = getY()-(slope*getX());
                break;
        }
        return constant;
    }

    public void translate(XYZPoint translatePoint)
    {
        point[0] += translatePoint.getX();
        point[1] += translatePoint.getY();
        point[2] += translatePoint.getZ();
    }
    
    @Override
    public void update() 
    {
        precalculatedPointProjection = boundPlane.convertCoordsToPoint(this);
    }
    
    public String toString()
    {
        return Double.toString(point[0]) + ", " + Double.toString(point[1]) + ", " + Double.toString(point[2]);
    }
}
