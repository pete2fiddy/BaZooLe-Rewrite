package shiftplanning;

import containers.GamePanel;
import java.awt.Graphics;


public class BasePlane extends Plane implements ThreeDDrawable
{
    private LayeredSolid testSolid;
    public BasePlane(GamePanel boundPanelIn, double xPos, double yPos, double width, double length) {
        super(boundPanelIn, xPos, yPos, width, length);
        ShapeLayer lower = ShapeLayer.createShapeLayerUsingIdealPolygon(this, 0, 0, 0, 3, 5);
        ShapeLayer upper = ShapeLayer.createShapeLayerUsingIdealPolygon(this, 0, 0, 5, 3, 5);
        ShapeLayer[] layers = {lower,upper};
        testSolid = new LayeredSolid(this, layers);
        addUpdatable(testSolid);
    }
    
    @Override
    public void draw(Graphics g){
        super.draw(g);
    }

    /***Not implemented yet. Necessary?***/
    @Override
    public double getSortDistanceConstant(){
        return -1;
    }
}
