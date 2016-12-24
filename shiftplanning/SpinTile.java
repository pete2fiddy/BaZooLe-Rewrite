package shiftplanning;

import java.awt.Color;
import java.awt.Graphics;
import shiftcolor.ColorPalette;
import updatables.MouseUpdatable;
import updatables.Updatable;
import updatables.UpdatableOnQuadrantChange;

/**
 *
 * @author phusisian
 */
public class SpinTile extends Prism implements UpdatableOnQuadrantChange, MouseUpdatable, Updatable{

    private static final int numSidesCylinder = 32;
    private BoundPlane spinPlane;
    public SpinTile(Plane boundPlaneIn, ShapeLayer[] shapeLayersIn, XYZPoint topCenter, double radius) {
        super(boundPlaneIn, shapeLayersIn);
        spinPlane = new BoundPlane(boundPlaneIn.getBasePlane().getBoundPanel(), boundPlaneIn, topCenter, 2*radius, 2*radius);
        boundPlaneIn.getBasePlane().addPlane(spinPlane);
        XYZPoint treePoint = topCenter.clone();
        treePoint.setZ(0);
        boundPlaneIn.getBasePlane().addUpdatable(this);
        boundPlaneIn.getBasePlane().addThreeDDrawable(this);
        boundPlaneIn.getBasePlane().addQuadrantUpdatable(this);
        
        Tree t = new Tree(spinPlane, this, treePoint);
        for(ThreeDDrawable threeD : t.getScenerySolids().getSolids()){
            spinPlane.addThreeDDrawable(threeD);
        }
        
    }
    
    public static SpinTile createTileFromCenterPoint(XYZPoint topCenter, double radius){
        XYZPoint bottomCenter = topCenter.clone();
        bottomCenter.setZ(0);
        Prism cylinder = Prism.createPrism(topCenter.getBoundPlane(), bottomCenter, topCenter, radius, numSidesCylinder, ColorPalette.defaultGrassColor);
        return new SpinTile(topCenter.getBoundPlane(), cylinder.getLayers(), topCenter, radius);
    }

    @Override
    public void draw(Graphics g){
        super.draw(g);
        spinPlane.addSpin((Math.PI/32.0));
        //spinPlane.update();
        spinPlane.draw(g);
        
    }
    
    @Override
    public void updateOnQuadrantChange() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void updateOnMouseClick(){
        
    }
    @Override
    public void updateOnMouseHold(){
        
    }
    
    @Override
    public void update(){
        super.update();
        //spinPlane.update();
    }
    

}
