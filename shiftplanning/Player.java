package shiftplanning;

import inputs.MouseInput;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import updatables.MouseUpdatable;

/**
 *
 * @author phusisian
 */
public class Player implements TwoDDrawable, MouseUpdatable{
    private XYZPoint position;
    //might support multiple planes now?
    private Plane boundPlane;
    private Traversable clickedTraversable;
    private Traversable boundTraversable;
    private static final int numTicksPerUnit = 50;
    private ArrayList<XYZPointCollection> movementCollections;
    private ArrayList<XYZPoint> movementPoints;
    private ArrayList<Traversable> chain;
    private int collectionIndex = 0;
    private int pointIndex = 0;
    private int movementPointIndex = 0;
    
    public Player(Plane boundPlaneIn, XYZPoint positionIn){
        boundPlane = boundPlaneIn;
        boundPlane.addTwoDDrawable(this);
        position = positionIn;
        boundPlane.addMouseUpdatable(this);
        //setBoundTraversable();
    }
    
    private void setBoundTraversable(){
        boundTraversable = boundPlane.getBasePlane().getTraversableLogic().getNearestTraversableFromPoint(position);
    }
    
    private void setClickedTraversable(){
        clickedTraversable = boundPlane.getBasePlane().getTraversableLogic().getTraversableAtXYPoint(new Point(MouseInput.mouseX, MouseInput.mouseY));
        ArrayList<Traversable> tempChain = boundPlane.getBasePlane().getTraversableLogic().getTraversableChain(boundTraversable);
        if(boundPlane.getBasePlane().getTraversableLogic().traversableInChain(tempChain, clickedTraversable)){
            movementCollections = boundPlane.getBasePlane().getTraversableLogic().getMovementPath(tempChain, boundTraversable, clickedTraversable);
            chain = tempChain;
            setMovementPoints();
        }
       
       
    }
    
    private void setMovementPoints(){
        if(movementCollections != null){
            movementPoints = new ArrayList<XYZPoint>();
            XYZPoint start = movementCollections.get(0).getHalfListPoint();
            XYZPoint secondPoint = getPointClosestToTraversable(chain.get(0), chain.get(1));
            XYZPoint end = movementCollections.get(movementCollections.size() - 1).getHalfListPoint();
            XYZPoint secondToLast = getPointClosestToTraversable(chain.get(chain.size() - 2), chain.get(chain.size() - 1));
            movementPoints.add(start);
            movementPoints.add(secondPoint);
            //XYZPoint currentPoint = getPointOnTraversableClosestToPoint(secondToLast, chain.get(1));
            int pointsCounted = 0;
            for(int i = 1; i < chain.size(); i++){
                System.out.println("Called");
                while(pointsCounted < chain.get(i).getMovementPoints().getXYZPointsLength()){
                    XYZPoint newPoint = getPointOnTraversableClosestToPoint(movementPoints.get(movementPoints.size() - 1), chain.get(i));
                    movementPoints.add(newPoint);
                    pointsCounted++;
                }
            }
            movementPoints.add(secondToLast);
            movementPoints.add(end);
        }
    }
    
    private XYZPoint getPointClosestToTraversable(Traversable start, Traversable end){
        double minDist = XYZPoint.getTotalMagnitudeBetweenPoints(start.getMovementPoints().getXYZPoints()[0], end.getMovementPoints().getXYZPoints()[0]);
        int minDistIndex = 0;
        XYZPoint[] startPoints = start.getMovementPoints().getXYZPoints();
        XYZPoint[] endPoints = end.getMovementPoints().getXYZPoints();
        for(int i = 1; i < startPoints.length; i++){
            for(int j = 0; j < endPoints.length ; j++){
                double checkDist = XYZPoint.getTotalMagnitudeBetweenPoints(startPoints[i], endPoints[j]);
                if(checkDist < minDist){
                    minDist = checkDist;
                    minDistIndex = i;
                }
            }
        }
        return startPoints[minDistIndex];
    }
    
    private XYZPoint getPointOnTraversableClosestToPoint(XYZPoint start, Traversable end){
        double minDist = XYZPoint.getTotalMagnitudeBetweenPoints(start, end.getMovementPoints().getXYZPoints()[0]);
        int minDistIndex = 0;
        //XYZPoint[] startPoints = start.getMovementPoints().getXYZPoints();
        XYZPoint[] endPoints = end.getMovementPoints().getXYZPoints();
        
        for(int j = 0; j < endPoints.length ; j++){
            double checkDist = XYZPoint.getTotalMagnitudeBetweenPoints(start, endPoints[j]);
            if(checkDist < minDist){
                minDist = checkDist;
                minDistIndex = j;
            }
        }
        
        return endPoints[minDistIndex];
    }
    
    private void moveToPoint(XYZPoint endPoint){
        if(endPoint != null){
            int numTicksToMove = getNumTicksToMove(endPoint);
            XYZPoint translatePoint = XYZPoint.getMovementTranslatePoint(position, endPoint, numTicksToMove);
            position.translate(translatePoint);
            position.update();
            
            if(numTicksToMove == 0){
                position = endPoint.clone();
            }
        }
    }
    
    private int getNumTicksToMove(XYZPoint endPoint){
        return (int)(XYZPoint.getTotalMagnitudeBetweenPoints(position, endPoint) * numTicksPerUnit);
    }
    
    private void move(){
        if(movementCollections != null){
            XYZPoint nextPoint = movementPoints.get(movementPointIndex);
            moveToPoint(nextPoint);
            if(getNumTicksToMove(nextPoint) == 0){
                if(movementPointIndex < movementPoints.size() - 1){
                    movementPointIndex = 0;
                }else{
                    movementCollections = null;
                    movementPoints = null;
                    chain = null;
                    movementPointIndex = 0;
                }
            }
            /*
            XYZPoint nextPoint = movementCollections.get(collectionIndex).getXYZPoints()[pointIndex];
            moveToPoint(nextPoint);
            if(getNumTicksToMove(nextPoint) == 0){
                if(pointIndex < movementCollections.get(collectionIndex).getXYZPoints().length - 1){
                    pointIndex++;
                }else{
                    pointIndex = 0;
                    if(collectionIndex < movementCollections.size() - 1){
                        collectionIndex ++;
                    }else{
                        movementCollections = null;
                        movementPoints = null;
                        chain = null;
                    }
                }
            }*/
        }
    }

    @Override
    public void draw(Graphics g) {
        
        setBoundTraversable();
        //System.out.println("Bound Traversable: " + boundTraversable);
            
        
        //setClickedTraversable();
        /*if(clickedTraversable != null){
            if(boundPlane.getBasePlane().getTraversableLogic().traversableInChain(tempChain, clickedTraversable)){
                g.setColor(Color.GREEN);
                
               
                
            }
        }else{
            g.setColor(Color.BLUE);
        }*/
        /*
        for(Traversable t : tempChain){
            for(XYZPoint p : t.getMovementPoints().getXYZPoints()){
                //g.setColor(Color.BLUE);
                //p.draw(g);
            }
        } 
        */
        move();
        g.setColor(Color.BLUE);
        position.draw(g);
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    //@Override
    public double getSortDistanceConstant() {
        return position.getSortDistanceConstant();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnMouseClick() {
        setClickedTraversable();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOnMouseHold() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
