package shiftplanning;

/**
 *
 * @author phusisian
 */
public class DistanceSorter implements UpdatableOnQuadrantChange, Updatable
{
    /*** DistanceSorter has a few issues involving how it sorts. Currently goes by the closest point to the viewer, but there are times where (generally long) shapes can extend farther back than the solid they are drawn over.
    /*https://www.cs.cmu.edu/~adamchik/15-121/lectures/Sorting%20Algorithms/code/MergeSort.java Used this as source for merge sort.*/
    private Plane boundPlane;
    public DistanceSorter(Plane boundPlaneIn, ThreeDDrawable[] threeDDrawablesIn)
    {
        boundPlane = boundPlaneIn;
    }

    public void printDrawablesSortConstants()
    {
        for(ThreeDDrawable threeD : boundPlane.getThreeDDrawables())
        {
            System.out.println("Constant: " + threeD.getSortDistanceConstant());
        }
    }
    
    private void sortByDistance(ThreeDDrawable[] a)
    {
        ThreeDDrawable[] tmp = new ThreeDDrawable[a.length];
        mergeSort(a, tmp,  0,  a.length - 1);
    }
    
    private void mergeSort(ThreeDDrawable[ ] a, ThreeDDrawable [ ] tmp, int left, int right)
    {
        if( left < right )
        {
            int center = (left + right) / 2;
            mergeSort(a, tmp, left, center);
            mergeSort(a, tmp, center + 1, right);
            merge(a, tmp, left, center + 1, right);
        }
    }
    
    private void merge(ThreeDDrawable[ ] a, ThreeDDrawable[ ] tmp, int left, int right, int rightEnd )
    {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd)
        {
            if(compareTo(a[left], a[right]) <= 0)//(a[left].compareTo(a[right]) <= 0)
            {
                tmp[k++] = a[left++];
            }else{
                tmp[k++] = a[right++];
            }
        }

        while(left <= leftEnd)    // Copy rest of first half
        {
            tmp[k++] = a[left++];
        }
        while(right <= rightEnd)  // Copy rest of right half
        {
            tmp[k++] = a[right++];
        }

        // Copy tmp back
        for(int i = 0; i < num; i++, rightEnd--)
        {
            a[rightEnd] = tmp[rightEnd];
        }
    }
    
    private int compareTo(ThreeDDrawable c1, ThreeDDrawable c2)
    {
        if(c1.getSortDistanceConstant() > c2.getSortDistanceConstant())
        {
            return 1;
        }else if(c1.getSortDistanceConstant() == c2.getSortDistanceConstant())
        {
            return 0;
        }
        return -1;
    }
    
    @Override
    public void updateOnQuadrantChange()
    {
        sortByDistance(boundPlane.getThreeDDrawables());
    }

    @Override
    public void update() 
    {
        sortByDistance(boundPlane.getThreeDDrawables());
    }
    
    
}
