/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hlawniitask0;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import java.awt.geom.Arc2D;
import java.io.*;
import java.util.*;
import javafx.util.Pair;
import java.math.*;
import java.nio.charset.StandardCharsets; 
import java.nio.file.*; 

/**
 *
 * @author Dell
 */
    /**
     * Unit tests the {@code Stopwatch} data type.
     * Takes a command-line argument {@code n} and computes the 
     * sum of the square roots of the first {@code n} positive integers,
     * first using {@code Math.sqrt()}, then using {@code Math.pow()}.
     * It prints to standard output the sum and the amount of time to
     * compute the sum. Note that the discrete sum can be approximated by
     * an integral - the sum should be approximately 2/3 * (n^(3/2) - 1).
     *
     * @param args the command-line arguments
     */
    



public class HlawniiTask0 {
   public static class Stopwatch {
        private final long start;
        public Stopwatch() {
            start = System.currentTimeMillis();
        }
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }

    }
   static Vector<Pair<Double,Double>>source,distnation;///coordinates
   static Vector<Double> radius;
   static Vector<Pair<Double,Pair<Double,Integer>>>SOL=new Vector<Pair<Double,Pair<Double,Integer>>>();//Magdy Algorithms ^_^

   static Vector<Pair<Double,Double> > Nodes=new Vector<Pair<Double, Double>>();///coordinates of each point.
   static Vector<Vector<Pair<Integer,Pair<Double,Double> > >  > Connections;/// ocnnections between each point and the time and the distance coverd.  
   static int numOfNodes, numOfEdges;
   static Vector<Pair<Integer,Double>> sourceNodes,DistnationNodes;
   static double NodeCost[];
   static double distace[];
   static int numquery;
   static int path[];
   static Stack<Integer>disPath=new Stack<>();
   //static Vector<Pair<Double,Pair<Double,Integer>>>SOL=new Vector<Pair<Double,Pair<Double,Integer>>>();//Magdy Algorithms ^_^
   static List<String> linesN = Collections.emptyList(); 
   static List<String> linesQ = Collections.emptyList(); 
    public static void read(String map,String query) {
        try{
        linesN = 
            Files.readAllLines(Paths.get(map), StandardCharsets.UTF_8);///read all files in list of strings
        linesQ = 
            Files.readAllLines(Paths.get(query), StandardCharsets.UTF_8);///read all files in list of strings
        
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        
    }
   public static void connect(List<String> lines)//read form file and connect graph
    {
        
            int reading=0;///indexer on the list of strings
            numOfNodes=Integer.parseInt(lines.get(reading));
            
            reading++;
            for(int i=0;i<numOfNodes;i++)
            {
                
                String [] s=lines.get(reading).split(" ");
                double x=Double.parseDouble(s[1]);
                double y=Double.parseDouble(s[2]);
                Pair<Double, Double>p=new Pair<Double, Double>(x,y);
                Pair<Double, Integer>add1=new Pair<Double, Integer>(y,i);
                Pair<Double, Pair<Double,Integer>>add2=new Pair<Double, Pair<Double,Integer>>(x,add1);
                
                Nodes.add(p);
            
                SOL.add(add2);

                reading++;
            }
            
            Collections.sort(SOL, new Comparator<Pair<Double, Pair<Double,Integer>>>() {
    @Override
    public int compare(final Pair<Double, Pair<Double,Integer>> s, final Pair<Double, Pair<Double,Integer>> t) {
         if(s.getKey()>t.getKey())
            return 1;
       else if (s.getKey()<t.getKey())
           return -1;
       else return 0;
        }
    }
);
            
            Connections= new Vector<Vector<Pair<Integer, Pair<Double,Double>>>>();
            Connections.setSize(numOfNodes+3);
            numOfEdges=Integer.parseInt(lines.get(reading));
            reading++;
            for(int i=0;i<numOfNodes;i++){
            Vector<Pair<Integer, Pair<Double,Double>>> v=new Vector<>();
            Connections.set(i, v);}
            for(int i=0;i<numOfEdges;i++)
            {
                ///////////////readind form the file then split the input with converting///////////////////////
                String [] s=lines.get(reading).split(" ");
                int  from=Integer.parseInt(s[0]);
                int  to=Integer.parseInt(s[1]);
                double distance=Double.parseDouble(s[2]);
                double  speed=Double.parseDouble(s[3]);
                /////////////////////////////////////////
                Pair<Double,Double>pt=new Pair<Double,Double>(distance/speed,distance);//make a pair for time and distance coverd.
                Pair<Integer,Pair<Double,Double> >pr=new Pair<Integer,Pair<Double,Double> >(to,pt);///pair of conction parmters 
                Connections.elementAt(from).add(pr);//connect two nodes
                pr=new Pair<Integer,Pair<Double,Double>>(from,pt);///reverse the conction to be undircted 
                Connections.elementAt(to).add(pr);//connect two nodes
                reading++;
            }
            
        
    }
   
   //----------------------------------------------------
   public static void Query(List<String> lines)//read form file and connect graph
    {
            
            int reading=0;///indexer on the list of strings
            
             numquery=Integer.parseInt(lines.get(reading));
             reading++;
            source=new Vector<>();
            distnation=new Vector<>();
            radius=new Vector<>();
            for(int i=0;i<numquery;i++)
            {
               String[] line=lines.get(reading).split(" ");
               
               source.add(new Pair<>(Double.parseDouble(line[0]),Double.parseDouble(line[1])));
               distnation.add(new Pair<>(Double.parseDouble(line[2]),Double.parseDouble(line[3])));
               radius.add(Double.parseDouble(line[4])/1000);
                reading++;
            }
    }
   public static double calcdis(double x1,double y1,double x2,double y2)/// calculate the distance between two nodes
   {
       return  (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
   }
   
   public static Vector<Pair<Integer,Double>> findNodes(double x,double y,double rad)// find all nodes within a certin radius 
    {
       //Magdy Algorithms ^_^
        Vector<Pair<Integer,Double>>res=new Vector<>();
       int nstart=0 ,nendd=(SOL.size()-1),nmid,from=-1;
       while(nstart <= nendd)
        {
           nmid = nstart + (nendd-nstart)/2; 
           if(SOL.elementAt(nmid).getKey() < x-rad)
        {
            nstart = nmid+1;
        }
        else
        {
           from = nmid;
           nendd = nmid-1;
        }
        }
        while(true)
        {
            if(from>SOL.size()-1)break;
            double y2=SOL.elementAt(from).getValue().getKey();
            double x2=SOL.elementAt(from).getKey();
            if(x2>x+rad)break;
            double distance=calcdis(x,y,x2,y2);
                if(rad*rad>=distance)
                {
                    Pair<Integer,Double> p=new Pair<>(SOL.elementAt(from).getValue().getValue(),distance);
                    res.add(p);
                }   
                from++;
        }
        return res;
       // Magdy Algorithms ^_^
    }
   
   public static void shortestPath(int source,int distnation)//finding shortest path between two nodes
    {
        path=new int[numOfNodes+5];
        double curtime=0;//current time
        double curdistance=0;
        int node=source;
        path[source]=-1;
        //PriorityQueue and its compare function 
        PriorityQueue<Pair<Double,Pair<Integer,Double> > > pq;
       pq = new PriorityQueue<>(new Comparator<Pair<Double, Pair<Integer, Double>>>(){
       @Override
       public int compare (Pair<Double,Pair<Integer,Double>> s,Pair<Double,Pair<Integer,Double>> t){
       if(s.getKey()>t.getKey())
            return 1;
       else if (s.getKey()<t.getKey())
           return -1;
       else return 0;
       }   
       });
       
        Pair<Integer,Double>nd=new Pair<>(node,curdistance);
        Pair<Double,Pair<Integer,Double> >con=new Pair<>(curtime,nd);
        pq.add(con);///adding source to queue
        while(!pq.isEmpty())
        {
            ////////////////////Extracting the Peek of the queue//////////////////
            curtime=pq.peek().getKey();
            node=pq.peek().getValue().getKey();
            curdistance=pq.peek().getValue().getValue();
            //////////////////////////////////////////////////////////////
            pq.poll();
            if(NodeCost[node]<curtime)continue;
            NodeCost[node]=curtime;
            distace[node]=curdistance;
            if(Connections.elementAt(node)!=null)
            for(int i=0;i<Connections.elementAt(node).size();i++)
            {
                ////////////////////Extracting the the nodes the connect to the peek of the queue//////////////////
               int newNode=Connections.elementAt(node).elementAt(i).getKey();
               double roadTime=Connections.elementAt(node).elementAt(i).getValue().getKey();
               double roadDis=Connections.elementAt(node).elementAt(i).getValue().getValue();
               //////////////////////////////////////////////////////////////////////////////
               if(curtime+roadTime<NodeCost[newNode]) ///checking  is the cost of me now id smaller than the minimun cost that i had
               {
                   ///re-new the informations of the node and push it to the queue
                   NodeCost[newNode]=curtime+roadTime;
                   distace[newNode]=curdistance+roadDis;
                   nd=new Pair<>(newNode,curdistance+roadDis);
                   con=new Pair<>(NodeCost[newNode],nd);
                   pq.add(con);
                   path[newNode]=node;
               }
            }
            
        }
    }
    
    public static void main(String[] args) {
        
               
        try
        {
         PrintStream  outfile=new PrintStream( "./out.txt");
         System.setOut(outfile);
        }catch(FileNotFoundException e)
                {
                    e.printStackTrace();
                }
               
        //apache.commons.lang.time.StopWatch;
        String map_path="./Samples\\[3] Large Cases\\SFMap.txt";
        String query_path="./Samples\\[3] Large Cases\\SFQueries.txt";
        read(map_path,query_path);
        Stopwatch timer2 = new Stopwatch(); 
        connect(linesN);
        Query(linesQ);
        double myInf =Integer.MAX_VALUE;
        NodeCost=new double [numOfNodes+2];
      //  Arrays.fill(NodeCost, myInf);
       // for(int i=0 ; i<NodeCost.length;i++)NodeCost[i]=myInf;
        
          
        
        for(int i=0;i<numquery;i++)
        {
                 
                sourceNodes=findNodes(source.elementAt(i).getKey(), source.elementAt(i).getValue(),radius.elementAt(i));
                DistnationNodes=findNodes(distnation.elementAt(i).getKey(), distnation.elementAt(i).getValue(),radius.elementAt(i));   
                // Arrays.fill(NodeCost, myInf);
        double min =myInf;
        double mindis=0;
        double dis = 0,diss = 0;
        double minwalk=0;
       
        double mindistace[]=new double[numOfNodes+2];
          Pair<Double,Double> p= new Pair<>(source.elementAt(i).getKey(),source.elementAt(i).getValue());
                //**********************************
                Nodes.add(p);
                p= new Pair<>(distnation.elementAt(i).getKey(),distnation.elementAt(i).getValue());
                Nodes.add(p);
                
                
                Vector<Pair<Integer,Pair<Double,Double>>> adding=new Vector<>();
               

                for(int t=0;t<sourceNodes.size();t++){
                    double di=Math.sqrt(sourceNodes.elementAt(t).getValue());
                    Pair<Double,Double> a=new Pair<>(di/5,di);
                    Pair<Integer,Pair<Double,Double>> co=new Pair<>(sourceNodes.elementAt(t).getKey(),a);
                    adding.add(co);
                    
                    
                }
                    Connections.set(numOfNodes, adding);
                    

                for(int t=0;t<DistnationNodes.size();t++){
                     double di=Math.sqrt(DistnationNodes.elementAt(t).getValue());
                    Pair<Double,Double> a=new Pair<>(di/5,di);
                    Pair<Integer,Pair<Double,Double>> co=new Pair<>(numOfNodes+1,a);
                    Connections.elementAt(DistnationNodes.elementAt(t).getKey()).add(co);
                    
                }
                
                Arrays.fill(NodeCost, myInf); 
        
        distace=new double[numOfNodes+2];
        
        shortestPath(numOfNodes,numOfNodes+1);
        
        min=(NodeCost[numOfNodes+1]);
                
           int cr=path[numOfNodes+1];
           double minwalk1=Math.sqrt(calcdis(Nodes.elementAt(cr).getKey(), Nodes.elementAt(cr).getValue(), Nodes.elementAt(numOfNodes+1).getKey(), Nodes.elementAt(numOfNodes+1).getValue()));
           while(cr!=numOfNodes)
           {
               disPath.push(cr);
               cr=path[cr];
           }
           minwalk1+=Math.sqrt(calcdis(Nodes.elementAt(disPath.peek()).getKey(), Nodes.elementAt(disPath.peek()).getValue(), Nodes.elementAt(numOfNodes).getKey(), Nodes.elementAt(numOfNodes).getValue()));
           
           while(!disPath.isEmpty())
           {
               System.out.print(disPath.peek()+" ");
               disPath.pop();
           }
           
           System.out.println();
        System.out.println(String.format("%.2f",Math.round(min*60*100.0)/100.0)+" mins");
            System.out.println(String.format("%.2f",Math.round(distace[numOfNodes+1]*100.0)/100.0)+" km");
            System.out.println(String.format("%.2f",Math.round(minwalk1*100.0)/100.0)+" km");
             System.out.println(String.format("%.2f",Math.round((distace[numOfNodes+1]-minwalk1)*100.0)/100.0)+" km");
            System.out.println();
    
    
    
    Connections.elementAt(numOfNodes).removeAllElements();
    Nodes.remove(Nodes.lastElement());
    Nodes.remove(Nodes.lastElement());
    
    for(int t=0;t<DistnationNodes.size();t++){
                     double di=Math.sqrt(DistnationNodes.elementAt(t).getValue());
                    Pair<Double,Double> a=new Pair<>(di/5,di);
                    Pair<Integer,Pair<Double,Double>> co=new Pair<>(numOfNodes+1,a);
                    Connections.elementAt(DistnationNodes.elementAt(t).getKey()).remove(co);
                    
                }
    
        
        }
            System.out.println(timer2.elapsedTime()*1000+" ms");
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
//        Vector<Integer> source2=new Vector<Integer>();
//        Vector<Pair<Double,Double>> swap=new Vector<>();
//         
//        if (sourceNodes.size()>DistnationNodes.size())
//        {
//           source2=sourceNodes;
//           sourceNodes=DistnationNodes;
//           DistnationNodes=source2;
//           swap=source;
//           source=distnation;
//           distnation=swap;
//           
//           
//        }for (int snode: sourceNodes )
//        { 
//            diss=calcdis(Nodes.elementAt(snode).getKey(), Nodes.elementAt(snode).getValue(),source.elementAt(i).getKey(), source.elementAt(i).getValue());
//            diss=Math.sqrt(diss);
//            double times = diss/5;
//            Arrays.fill(NodeCost, myInf);
//            
//            for (int dnode : DistnationNodes )
//            {
//                if(NodeCost[dnode]==myInf){
//                 distace=new double[numOfNodes];
//                
//                shortestPath(snode, dnode);
//                
//                }
//                dis=calcdis(Nodes.elementAt(dnode).getKey(), Nodes.elementAt(dnode).getValue(),distnation.elementAt(i).getKey(), distnation.elementAt(i).getValue());
//                dis=Math.sqrt(dis);
//                double time = dis/5; 
//                if (min>times+time+NodeCost[dnode]){
//                min=(times+time+NodeCost[dnode]);
//                minwalk=dis+diss;
//                mindis=distace[dnode]+minwalk;
//                mindistace=distace;
//                
//                }
//                
//            }
//        }
//    System.out.println(Math.round(min*60*100.0)/100.0);
//    System.out.println(Math.round(mindis*100.0)/100.0);
//    System.out.println(Math.round(minwalk*100.0)/100.0);
//    System.out.println(Math.round((mindis-(minwalk))*100.0)/100.0);
//    
//    System.out.println();
//    
//    
//        
//    } 
    

    }
    
}
