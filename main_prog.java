package Assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class HeapSort
{
    //Create a sorted array using heapsort alogo
    public ArrayList<Integer> sort(ArrayList<Integer> efforts) 
    {
        ArrayList<Integer> sortedArray=new ArrayList<Integer>();   
        BuildMinHeap(efforts);
        for(int i=efforts.size()-1;i>=2;i--)
         {
             Swap(efforts,1,i);
             sortedArray.add(efforts.get(i));
             efforts.remove(i);
             Heapify(efforts,1);
         } 
        return sortedArray;
    }
    // Check for min heap structure,create min heap and solve recursively
    private void Heapify(ArrayList<Integer> efforts, int i) 
    {
        int leftIndex=2*i;
        int rightIndex=2*i+1;
        int minIndex=i;

        //Find the smallest child if any
        if(leftIndex<=efforts.size()-1&& efforts.get(leftIndex) < efforts.get(i))
          minIndex=leftIndex;
        if(rightIndex<=efforts.size()-1&& efforts.get(rightIndex) < efforts.get(minIndex))
          minIndex=rightIndex;
        //If smaller child exist swao and heapify at smaller index recursively
        if(minIndex!=i) 
          {
              Swap(efforts,i ,minIndex); 
              Heapify(efforts, minIndex); 
          } 
        
    }
    //Swaps contents in the array given by the two indices
    private void Swap(ArrayList<Integer> efforts, int i1, int i2) 
    {
        var temp=efforts.get(i1);
        var temp1=efforts.get(i2);

        efforts.set(i2, temp);
        efforts.set(i1, temp1);
    }
    //Create min heap for entire array of efforts10
    private void BuildMinHeap(ArrayList<Integer> efforts) 
    {   
        int size=(int) Math.floor((efforts.size()-1)/2);
        for(int i=size;i>=1;i--)
         {
               Heapify(efforts, i);
         }
    }

}

public class main_prog
{
    private static ArrayList<Integer> FinalDishIndices;

    public static void main(String[] args) 
    {
      Scanner in = new Scanner(System.in);
      int n,M=0;
      ArrayList<Integer> Efforts= new ArrayList<Integer>();
      ArrayList<Integer> Points= new ArrayList<Integer>();
      Efforts.add(-1);
      System.out.print("n=");
      n=in.nextInt();
      System.out.print("M=");
      M=in.nextInt();
      System.out.print("+++ Efforts");
      for(int i=0;i<n;++i)
              Efforts.add(in.nextInt());

      System.out.print("+++ Part 1 (Greedy)");
      //Call to method Greedy using heapsort
      List<Integer> minEfforts=solveGreedy(M,Efforts); 
      
      //Printing
      System.out.print("Minimum Effort="+minEfforts.stream().mapToInt(x->x).sum()+"=");
      for(int i=0;i<minEfforts.size();++i)
       {
            if(i!=0)
              System.out.print("+");
         System.out.print(minEfforts.get(i));
       }

       //Read for part 2
       System.out.print("\n+++ Points");
       Points.add(0);
       for(int i=0;i<n;++i)
               Points.add(in.nextInt());
       System.out.print("+++ Efforts");
       Efforts.clear();
       Efforts.add(0);
       for(int i=0;i<n;++i)
               Efforts.add(in.nextInt());
       System.out.print("+++ Part 2 (DP)\n");
       //Call to solveDP for part 2 and extension 
       solveDP(M,Points,Efforts);  
    }
    //Method to solve Part 2 and extension using dynamic programming O(nP)
    private static void solveDP(int maxEffort,ArrayList<Integer> points, ArrayList<Integer> efforts) 
    {
        int size=points.size();
        int maxPoints=points.stream().mapToInt(x->x).sum();
        int[][] T=new int[size+1][maxPoints+1]; //used to compute max points and min efforts
        char[][] Traverse=new char[size+1][maxPoints+1]; //used for traversal of the table
        for(int i=0;i<size+1;i++) 
          T[i][0]=0;

        for(int p=1;p<maxPoints+1;p++)
          T[0][p]=Integer.MAX_VALUE;  
         
        for(int j=1;j<size+1;++j)
         {
           for(int p=0;p<maxPoints+1;++p)
           {
             int index=j;
             if(index==size)
               index=index-1;
             if(p<points.get(index))
               {
                 T[j][p]=T[j-1][p];
                 Traverse[j][p]='^';   //Means copied from above row [i-1,p]
               }
             else
             {
               int PrepareDish=Integer.MAX_VALUE;
               if(T[j-1][p-points.get(index)]!=Integer.MAX_VALUE)
                 PrepareDish=efforts.get(index)+T[j-1][p-points.get(index)];
               int NotPrepareDish=T[j-1][p];
               if(PrepareDish>maxEffort)
                { 
                  T[j][p]=T[j-1][p];     //Efforts required for preparing dish is higher than maxEffort simply copy the previous one
                  Traverse[j][p]='^';
                }
               else
                { 
                  T[j][p]=Math.min(NotPrepareDish, PrepareDish); //Calculate Efforts for preparing or not preparing a dish and fix whichever is minimum
                  if(NotPrepareDish > PrepareDish)
                   { 
                     Traverse[j][p]='\\';//Means copied from above row but not the same column diagonally up[i-1,p-pi]
                   }
                   else
                   {
                    Traverse[j][p]='^';
                   }
                }
             }  
           }
          }
          //PrintTable(T,Traverse,points); //To print table with directions ^ means choose not to prepare a dish \\ means chose to prepare a dish at i
          int k=0;

          for(int i=0;i<T[0].length;++i)//Find index where effort is maximum but not infinity(Integer.MAX_VALUE)
             {
               if(T[T.length-1][i]!=Integer.MAX_VALUE)
                 ++k;
             }

          FinalDishIndices=new ArrayList<Integer>();              
          ReadTableRecursively(points,Traverse,Traverse.length-1,k);//Read table copy indices of dishes finally chosen atmost n dishes so O(n)
          if(FinalDishIndices!=null) //Iterate if not null and display efforts and points for each chosen dish i
           {
             System.out.print("\nMaximum Points="+k+"=");
             for(int i=0;i<FinalDishIndices.size();++i)
              {
                   if(i!=0)
                     System.out.print("+");
                System.out.print(points.get(FinalDishIndices.get(i)));
              } 
              System.out.print("\nMinimum Effort="+T[T.length-1][k]+"=");
              for(int i=0;i<FinalDishIndices.size();++i)
               {
                    if(i!=0)
                      System.out.print("+");
                 System.out.print(efforts.get(FinalDishIndices.get(i)));
               }  
           }
       }
    //Method for printing table
    private static void PrintTable(int[][] T, char[][] Traverse,ArrayList<Integer> points)
    {
      for(int j=0;j<T.length;++j)
          {
            for(int p=0;p<T[j].length;++p)
            {
              if(T[j][p]!=Integer.MAX_VALUE)
               System.out.print(T[j][p]+"( "+(char)Traverse[j][p]+" )");
              else
              System.out.print("*");
            }
            System.out.println("");
          }
    }
    //Recursive method to find the indices of chosen dishes
    private static void ReadTableRecursively(ArrayLis10t<Integer> points,char[][] traverse, int i,int p) 
    { 
       if(i==0||p==0)
        return ;
       if(traverse[i][p]=='^')
         ReadTableRecursively(points,traverse,i-1, p);
       if(traverse[i][p]=='\\')
        { 
          ReadTableRecursively(points,traverse, i-1, p-points.get(i));  
          FinalDishIndices.add(i);
        }
    }
    //Method for solving the Part 1 by greedy appoch using heapsort in O(nlogn)
    private static List<Integer> solveGreedy(int m, ArrayList<Integer> efforts) 
    {   ArrayList<Integer> minEfforts=new ArrayList<>();
        HeapSort heap=new HeapSort();
        minEfforts=heap.sort(efforts);
        int sum=minEfforts.get(0);
        int k=0;

        while(sum<m)
        { 
           sum+=minEfforts.get(k);
           k++;
        }
        return minEfforts.subList(0, k-1);
    }
}