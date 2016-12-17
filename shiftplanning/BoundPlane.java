package shiftplanning;

import containers.GamePanel;
import java.awt.Graphics;

/**
 *
 * @author phusisian
 */
public class BoundPlane extends Plane implements ThreeDDrawable
{
    public BoundPlane(GamePanel boundPanelIn, Plane boundPlane, double xPos, double yPos, double width, double length) {
        super(boundPanelIn, xPos, yPos, width, length);
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
