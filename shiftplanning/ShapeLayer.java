package shiftplanning;

import java.awt.Color;
import java.awt.Graphics;

/*Holds a single layer to a shape made up of layers (e.g. rocketship)*/
public class ShapeLayer implements ThreeDDrawable, Updatable
{
    private Plane boundPlane;
    private XYZPointCollection xyzPointCollection; //holds the XYZPoints that make up this layer
    
    public ShapeLayer(Plane boundPlaneIn, XYZPointCollection xyzPointCollectionIn)
    {
        boundPlane = boundPlaneIn;
        xyzPointCollection = xyzPointCollectionIn;
    }
    
    public XYZPointCollection getXYZPointCollection()
    {
        return xyzPointCollection;
    }
    
    public static ShapeLayer createFlatShapeLayerRectangle(Plane boundPlaneIn, XYZPoint bottomLeftCorner, double width, double length)
    {
        double startX = bottomLeftCorner.getX();
        double startY = bottomLeftCorner.getY();
        double z = bottomLeftCorner.getZ();
        XYZPoint[] xyzPoints = {bottomLeftCorner, new XYZPoint(boundPlaneIn, startX + width, startY, z), new XYZPoint(boundPlaneIn, startX+width, startY+length, z), new XYZPoint(boundPlaneIn, startX, startY+length, z)};
        return new ShapeLayer(boundPlaneIn, new XYZPointCollection(boundPlaneIn, xyzPoints));
    }
    
    public void translate(XYZPoint translatePoint)
    {
        xyzPointCollection.translate(translatePoint);
    }
    
    public int getNumPoints()
    {
        return xyzPointCollection.getXYZPointsLength();
    }
    
    public static ShapeLayer createShapeLayerUsingIdealPolygon(Plane boundPlaneIn, double centerX, double centerY, double z, double radius, int numSides)//make this take an XYZPoint instead of centerX, centerY, z
    {
        radius = 2*radius/Math.sqrt(2);
        XYZPoint[] points = new XYZPoint[numSides];
        double thetaConstant = (Math.PI*2.0)/(double)numSides;
        for (int i = 0; i < points.length; i++) 
        {
            double xPos = centerX + radius*Math.cos((Math.PI/4.0)+i*thetaConstant);
            double yPos = centerY + radius*Math.sin((Math.PI/4.0)+i*thetaConstant);
            points[i] = new XYZPoint(boundPlaneIn, xPos, yPos, z);
        }
        XYZPointCollection tempCollection = new XYZPointCollection(boundPlaneIn, points);
        return new ShapeLayer(boundPlaneIn, tempCollection);
    }
    
    @Override
    public void draw(Graphics g)
    {
        g.fillPolygon(xyzPointCollection.getXYPolyPoints()[0], xyzPointCollection.getXYPolyPoints()[1], xyzPointCollection.getXYZPointsLength());
    }

    @Override
    public double getSortDistanceConstant() {
        return xyzPointCollection.getClosestPointToFront().getSortDistanceConstant();//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update()
    {
        xyzPointCollection.update();
    }
    
}
