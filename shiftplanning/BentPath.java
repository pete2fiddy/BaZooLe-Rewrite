package shiftplanning;

import updatables.Updatable;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author phusisian
 */
public class BentPath implements TwoDDrawable, Traversable, Updatable {

    private final Color darkBrown = new Color(125, 62, 17);
    private final Color darkerBrown = new Color(115, 59, 18);
    private final Color evenDarkerBrown = new Color(102, 52, 16);
    private double theta;
    private XYZPoint vertex;
    private Tile boundTile;
    private XYZPointCollection movementPoints;
    private XYZPoint relativeTilePoint;
    private ShapeLayer pathShape;
    private PathConnector pathConnector;
    public BentPath(Tile boundTileIn, XYZPoint vertexIn, double thetaIn){
        boundTile = boundTileIn;
        vertex = vertexIn;
        theta = thetaIn;
        double relativeTileX = vertex.getX() - boundTile.getPlacementPoint().getX();
        double relativeTileY = vertex.getY() - boundTile.getPlacementPoint().getY();
        relativeTilePoint = new XYZPoint(boundTile.getBoundPlane(), relativeTileX, relativeTileY, boundTile.getHeight());
        
        setMovementPoints();
        pathShape = ShapeLayer.createShapeFromLineWithStroke(movementPoints, .1, Color.BLUE);
        pathConnector = new PathConnector(this);
        pathShape.setColor(darkerBrown);
        //boundTile.getBoundPlane().getBasePlane().addTraversable(this);
    }
    
    private void setMovementPoints(){
        XYZPoint[] movementPointArray = new XYZPoint[3];
        //The order of these points DO matter, as the path the player takes will be reversed if they are in the wrong order. Just reverse them if the path is reversed.
        //Code that "un-reverses" the point order if, say, the player is moving backwards across it, will be fitted around a certain ordering.
        movementPointArray[1] = vertex;
        movementPointArray[2] = boundTile.getShapeLayer(boundTile.getLayerLength() - 1).getIntersectingEdgePoint(vertex, theta - (Math.PI/2.0));
        movementPointArray[0] = boundTile.getShapeLayer(boundTile.getLayerLength() - 1).getIntersectingEdgePoint(vertex, theta);
        movementPoints = new XYZPointCollection(boundTile.getBoundPlane(), movementPointArray);
    }
    
    @Override
    public void draw(Graphics g) {
        movementPoints.draw(g);
        pathShape.draw(g);
        pathShape.drawOutline(g, evenDarkerBrown, 1.0);
        pathConnector.draw(g);
    }

    @Override
    public XYZPointCollection getMovementPoints() {
        return movementPoints;
    }

    @Override
    public void update() {
        movementPoints.update();
        double newRelX = vertex.getX() - boundTile.getPlacementPoint().getX();
        double newRelY = vertex.getY() - boundTile.getPlacementPoint().getY();
        double dx = relativeTilePoint.getX() - newRelX;
        double dy = relativeTilePoint.getY() - newRelY;
        XYZPoint translatePoint = new XYZPoint(vertex.getBoundPlane(), dx, dy, 0);
        movementPoints.translate(translatePoint);
        pathShape.update();
        pathShape.translate(translatePoint);
        pathConnector.update();
        //pathConnector.translate(translatePoint);
    }
    
    @Override
    public Tile getBoundTile(){
        return boundTile;
    }
    
    @Override
    public boolean isConnected(Traversable t){
        return pathConnector.traversableIsConnected(t);
    }

    @Override
    public ShapeLayer getShape() {
        return pathShape;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XYZPoint[] getConnectorPoints() {
        return pathConnector.getConnectorPoints();
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}                                                                           