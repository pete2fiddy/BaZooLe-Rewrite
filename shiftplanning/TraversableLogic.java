package shiftplanning;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author phusisian
 */
public class TraversableLogic {
    private BasePlane boundPlane;
    private Traversable[] traversables = new Traversable[0];
    public TraversableLogic(BasePlane boundPlaneIn){
        boundPlane = boundPlaneIn;
    }
    
    public void addTraversable(Traversable t){
        Traversable[] temp = new Traversable[traversables.length + 1];
        for(int i = 0; i < traversables.length; i++){
            temp[i] = traversables[i];
        }
        temp[traversables.length] = t;
        traversables = temp;
    }
    
    public Traversable getNearestTraversableFromPoint(XYZPoint checkPoint){
        int smallestDistIndex = 0;
        double smallestDist = traversables[0].getShape().getMagnitudeToMidpoint(checkPoint);
        for(int i = 1; i < traversables.length; i++){
            double newMag = traversables[i].getShape().getMagnitudeToMidpoint(checkPoint);
            if(newMag < smallestDist){
                smallestDist = newMag;
                smallestDistIndex = i;
            }
        }
        return traversables[smallestDistIndex];
    }
    
    public void setColorOfConnectedPaths(){
        for(int i = 0; i < traversables.length; i++){
            for(int j = 0; j < traversables.length; j++){
                if(traversables[i].isConnected(traversables[j])){
                    traversables[i].getShape().setColor(Color.RED);
                }else{
                    traversables[i].getShape().setColor(Color.BLUE);
                }
            }
        }
    }
    
    private ArrayList<Traversable> getConnectedTraversablesFromList(Traversable checkTraversable, ArrayList<Traversable> traversableList){
        ArrayList<Traversable> connectedTraversables = new ArrayList<Traversable>();
        for(Traversable t : traversableList){
            if(t.isConnected(checkTraversable) && t != checkTraversable){
                connectedTraversables.add(t);
            }
        }
        return connectedTraversables;
    }
    
    private void removeTraversablesFromList(ArrayList<Traversable> fullList, ArrayList<Traversable> removes){
        for(Traversable t : removes){
            fullList.remove(t);
        }
    }
    
    private ArrayList<Traversable> getTraversableList(){
        ArrayList<Traversable> giveReturn = new ArrayList<Traversable>();
        for(Traversable t : traversables){
            giveReturn.add(t);
        }
        return giveReturn;
    }
    
    //this does not support chains that can branch -- meaning chains that can have more than two conected paths (which I don't think I ever will support)
    public ArrayList<Traversable> getTraversableChain(Traversable t){
        ArrayList<Traversable> traversableList = getTraversableList();
        //traversableList.remove(t);
        ArrayList<Traversable> chain = new ArrayList<Traversable>();
        //to find the base of the chain, look until it finds a traversable with only one connection.
        Traversable base = getBaseTraversableOfChain(t);
        chain.add(base);
        
        if(getConnectedTraversables(chain.get(0)).size() == 0){
            return chain;
        }
        Traversable connectedTraversable = getConnectedTraversables(chain.get(0)).get(0);
        chain.add(connectedTraversable);
        while( getConnectedTraversablesExcluding(chain.get(chain.size() - 1), chain.get(chain.size() - 2)).size() != 0){
            ArrayList<Traversable> connectedExclude = getConnectedTraversablesExcluding(chain.get(chain.size() - 1), chain.get(chain.size() - 2));
            chain.add(connectedExclude.get(0));
        }
       
        return chain;
    }
    
    private ArrayList<Traversable> getConnectedTraversablesExcluding(Traversable checkTraversable, Traversable removeTraversable){
        ArrayList<Traversable> giveReturn = getConnectedTraversables(checkTraversable);
        giveReturn.remove(removeTraversable);
        return giveReturn;
    }
    
    
    private ArrayList<Traversable> getConnectedTraversables(Traversable checkTraversable){
        ArrayList<Traversable> connectedTraversables = new ArrayList<Traversable>();
        for(Traversable t : traversables){
            if(t.isConnected(checkTraversable) && t != checkTraversable){
                connectedTraversables.add(t);
            }
        }
        return connectedTraversables;
    }
    
    private Traversable getBaseTraversableOfChain(Traversable t){
        if(getConnectedTraversables(t).size() == 0){
            return t;
        }
        Traversable base = getConnectedTraversables(t).get(0);
        Traversable oldBase = t;
        while(getConnectedTraversablesExcluding(base, oldBase).size() != 0){
            Traversable temp = getConnectedTraversablesExcluding(base, oldBase).get(0);
            oldBase = base;
            base = temp;
        }
        return base;
    }
}
