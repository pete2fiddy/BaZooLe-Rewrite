package shiftplanning;

import inputs.MouseInput;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author phusisian
 */
public class Tile extends Prism implements MouseUpdatable
{
    private static final int numTicksToMovePerUnit = 50;
    private boolean clicked = false;
    private double width, length;
    private double height;
    private Color color = Color.GREEN;
    private XYZPoint clickMidpoint;
    private XYZPoint translateMovementPoint;
    private int numTicksMoved = 0;
    
    //consider moving a shape layer array into a class? ( I guess that's technically layered solid, which you need to pass to it to instantiate)
    public Tile(Plane boundPlaneIn, ShapeLayer[] shapeLayersIn)
    {
        super(boundPlaneIn, shapeLayersIn);
    }
    
    public static Tile createTileUsingBottomLeftCorner(Plane boundPlaneIn, XYZPoint bottomLeftCorner, double width, double length, double zHeight)
    {
        ShapeLayer layer1 = ShapeLayer.createFlatShapeLayerRectangle(boundPlaneIn, bottomLeftCorner, width, length);
        XYZPoint newPoint = new XYZPoint(boundPlaneIn, bottomLeftCorner.getX(), bottomLeftCorner.getY(), bottomLeftCorner.getZ() + zHeight);
        ShapeLayer layer2 = ShapeLayer.createFlatShapeLayerRectangle(boundPlaneIn, newPoint, width, length);
        ShapeLayer[] layers = {layer1, layer2};
        Tile t = new Tile(boundPlaneIn, layers);
        t.setWidth(width);
        t.setLength(length);
        t.setHeight(zHeight);
        return t;
    }
    
    public void setHeight(double heightIn)
    {
        height = heightIn;
    }
    
    public void setWidth(double widthIn)
    {
        width = widthIn;
    }
    
    public void setLength(double lengthIn)
    {
        length = lengthIn;
    }
    
    /*public void setCenter(XYZPoint newCenter)
    {
        ShapeLayer firstLayer = ShapeLayer.createFlatShapeLayerRectangle(getBoundPlane(), newCenter, width, length);
        ShapeLayer secondLayer = ShapeLayer.createFlatShapeLayerRectangle(getBoundPlane(), new XYZPoint(getBoundPlane(), newCenter.getX(), newCenter.getY(), newCenter.getZ() + height), width, length);
        ShapeLayer[] layers = {firstLayer, secondLayer};
        setShapeLayers(layers);
    }*/
    
    
    public void updateOnMouseHold() 
    {
        
    }
    
    private void respondToClick()
    {
        clicked = true;
        color = Color.RED;
        clickMidpoint = getMidpointAtIndex(0);/***may need to clone***/
    }
    
    private void respondToUnclick()
    {
        /*if(isClickMoveableTo())
        {
            translate(new XYZPoint(getBoundPlane(), 1, 1, 0));
        }*/
        
        if(getTranslateMovementPoint() != null)
        {
            translateMovementPoint = getTranslateMovementPoint();
            
            //translate(getTranslateMovementPoint());
        }
        //setCenter(MouseInput.mouseCoordsOnPlane);
        clicked = false;
        color = Color.GREEN;
    }
    
    /*private boolean isClickMoveableTo()
    {
        if(MouseInput.mouseCoordsOnPlane.getX() > clickMidpoint.getX() - (width/2.0) && MouseInput.mouseCoordsOnPlane.getX() < clickMidpoint.getX() + (width/2.0))
        {
            return true;
        }else if(MouseInput.mouseCoordsOnPlane.getY() > clickMidpoint.getY() - (length/2.0) && MouseInput.mouseCoordsOnPlane.getY() < clickMidpoint.getY() + (length/2.0))
        {
            return true;
        }
        return false;
    }*/
    
    private boolean isClickMoveX()
    {
        return (MouseInput.mouseCoordsOnPlane.getY() > clickMidpoint.getY() - (length/2.0) && MouseInput.mouseCoordsOnPlane.getY() < clickMidpoint.getY() + (length/2.0));
        
    }
    
    private boolean isClickMoveY()
    {
        return (MouseInput.mouseCoordsOnPlane.getX() > clickMidpoint.getX() - (width/2.0) && MouseInput.mouseCoordsOnPlane.getX() < clickMidpoint.getX() + (width/2.0));
    }

    private XYZPoint getTranslateMovementPoint()
    {
        if(isClickMoveX())
        {
            return new XYZPoint(getBoundPlane(), (int)(MouseInput.mouseCoordsOnPlane.getX() - clickMidpoint.getX()), 0, 0);
        }else if(isClickMoveY())
        {
            return new XYZPoint(getBoundPlane(), 0, (int)(MouseInput.mouseCoordsOnPlane.getY() - clickMidpoint.getY()), 0);
        }
        return null;
    }
    
    private void moveAndUpdateSortOrder()
    {
        if(translateMovementPoint != null && numTicksMoved < numUnitsToMove())
        {
            numTicksMoved++;
            translate(new XYZPoint(getBoundPlane(), translateMovementPoint.getX()/(double)numUnitsToMove(), translateMovementPoint.getY()/(double)numUnitsToMove(), 0));
            getBoundPlane().updateDistanceSorter();
        }else{
            translateMovementPoint = null;
            numTicksMoved = 0;
        }
    }
    
    private int numUnitsToMove()
    {
        return (int)Math.abs(translateMovementPoint.getX() * numTicksToMovePerUnit + translateMovementPoint.getY() * numTicksToMovePerUnit);
    }
    
    public void updateOnMouseClick()
    {
        if(visibleSideContainsPoint(MouseInput.clickX, MouseInput.clickY))
        {
            respondToClick();
        }else if(clicked){
            respondToUnclick();
        }
    }

    @Override
    public void draw(Graphics g)
    {
        g.setColor(color);
        super.draw(g);
    }
    
    
    @Override
    public void update()
    {
        super.update();
        moveAndUpdateSortOrder();
    }
}
