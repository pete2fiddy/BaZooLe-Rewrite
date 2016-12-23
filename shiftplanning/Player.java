package shiftplanning;

/**
 *
 * @author phusisian
 */
public class Player {
    private XYZPoint position;
    //Does not currently support multiple planes.
    private Plane boundPlane;
    private Traversable boundTraversable;
    
    public Player(Plane boundPlaneIn, XYZPoint positionIn){
        boundPlane = boundPlaneIn;
        setBoundTraversable();
    }
    
    private void setBoundTraversable(){
        boundTraversable = boundPlane.getTraversableLogic().getNearestTraversableFromPoint(position);
    }
}
