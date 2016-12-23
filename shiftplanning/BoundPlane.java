package shiftplanning;

import containers.GamePanel;
import java.awt.Graphics;

/**
 *
 * @author phusisian
 */
public class BoundPlane extends Plane implements ThreeDDrawable
{
    private Plane boundPlane;
    public BoundPlane(GamePanel boundPanelIn, Plane boundPlaneIn, double xPos, double yPos, double width, double length) {
        super(boundPanelIn, xPos, yPos, width, length);
        boundPlane = boundPlaneIn;
    }
    
    @Override
    public BasePlane getBasePlane(){
        return boundPlane.getBasePlane();
    }
    
    /***Not implemented yet.***/
    @Override
    public void draw(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /***Not implemented yet.***/
    @Override
    public double getSortDistanceConstant() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
