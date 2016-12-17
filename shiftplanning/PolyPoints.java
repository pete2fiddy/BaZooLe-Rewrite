/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shiftplanning;

/**
 *
 * @author phusisian
 */
/*
Currently not used but may use (and convert everything that needs it to use it) later.
*/
public class PolyPoints
{
    private int[][] polyPoints;
    public PolyPoints(int[][] polyPointsIn)
    {
        polyPoints = polyPointsIn;
    }
    
    public void setPolyPoints(int[][] polyPointsIn)
    {
        polyPoints = polyPointsIn;
    }
    
    public int[] getXPoints()
    {
        return polyPoints[0];
    }
    
    public int[] getYPoints()
    {
        return polyPoints[1];
    }
    
    public int getNumPoints()
    {
        return polyPoints[0].length;
    }
}
