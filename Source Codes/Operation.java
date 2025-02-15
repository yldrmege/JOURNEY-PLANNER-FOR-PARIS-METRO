import java.util.HashMap;

import ADTPackage.*;

public class Operation {
	  /**
	    * Gets information about the suggested route between two stops on the Paris metro.
	    *
	    * @param beginVertex The starting stop.
	    * @param finishVertex The destination stop.
	    * @param preference A string indicating user preference ("yes" for cheapest path, "no" for shortest path).
	    * @param paris_metro The directed graph representing the Paris metro.
	    * @param edgeinformation A map containing information about edges (connections between stops).
	    */
   public static void GetInfo(String beginVertex,String finishVertex,String preferetion,DirectedGraph<String> paris_metro,HashMap<String,LList<String>>edgeinformation) {
	   StackInterface<String>path=new LinkedStack<>();	
		double result=0;
		 // Choose the appropriate method based on user preference ("yes" for cheapest path, "no" for shortest path).
		if(preferetion.equals("yes"))
			result= paris_metro.getCheapestPath(beginVertex,finishVertex,path);
		else
			result=paris_metro.getShortestPath(beginVertex,finishVertex,path);
	    
	// getting the edge costs
			VertexInterface<String> endVertex = paris_metro.getVerticesDict().getValue(finishVertex);
			LList<Double> tempedgeCostsList=new LList<>(); 
			
			while (endVertex!=null&&endVertex.hasPredecessor()) {
			    VertexInterface<String> previousVertex = endVertex.getPredecessor();
			    
			    double edgeCost = endVertex.getCost() - previousVertex.getCost();
			    tempedgeCostsList.add(edgeCost);			    
			    endVertex = previousVertex; 			    
			}			
		if(path.isEmpty()) {
			System.out.println("There is no route between these two stops on the Paris metro, so enter other stops");
			System.out.println();
		}
		else {
			System.out.println("Suggestion");
			System.out.println();
			LList<Double>edgeCosts=new LList<>();
			for(int i=tempedgeCostsList.getLength();i>=1;i--) {
				edgeCosts.add(tempedgeCostsList.getEntry(i));
			}	
			ListInterface<String>data=new LList<>();
			while(!path.isEmpty()) {
				data.add(path.pop());
			}
			LList<String>edgedata=new LList<>();
			String informationline="";		
			String [] information_fragmentation=new String[2];
			String previousedge="";
			String edge="";
			String previousentry=data.getEntry(1);
			String entry="";
			int previousstation=1;
			int station=0;
			edgedata=edgeinformation.get(data.getEntry(1)+data.getEntry(2));
			if(edgedata.getLength()==1) {
				informationline=edgedata.getEntry(1);
				information_fragmentation=informationline.split(",");
				previousedge=information_fragmentation[0];				
			}
			else {
				for(int i=1;i<=edgedata.getLength();i++) {
					informationline=edgedata.getEntry(i);
					information_fragmentation=informationline.split(",");		
					if(preferetion.equals("yes")) {
						if(edgeCosts.getEntry(1)==Double.parseDouble(information_fragmentation[1])) {
							previousedge=information_fragmentation[0];
							break;						
						}	
					}
					else {
						previousedge=information_fragmentation[0];
						break;
					}
										
				}
			}	
			for(int i=2;i<data.getLength();i++) {
				edgedata=edgeinformation.get(data.getEntry(i)+data.getEntry(i+1));
				if(edgedata.getLength()==1) {
					informationline=edgedata.getEntry(1);
					information_fragmentation=informationline.split(",");
					edge=information_fragmentation[0];
				}
				else {
					for(int j=1;j<=edgedata.getLength();j++) {
						informationline=edgedata.getEntry(j);
						information_fragmentation=informationline.split(",");
						if(preferetion.equals("yes")) {
							if(edgeCosts.getEntry(i)==Double.parseDouble(information_fragmentation[1])) {
								edge=information_fragmentation[0];
								break;						
							}	
						}
						else {
							edge=information_fragmentation[0];
							break;
						}					
					}					
				}
				if(previousedge.equals(edge))
					previousedge=edge;
				else {
					entry=data.getEntry(i);
					station=i;
					System.out.println("Line "+previousedge+":");
					int stationcount=station-previousstation;
					System.out.println(previousentry+" → "+entry+" ( "+stationcount+" station(s) )");
					System.out.println();
					previousentry=entry;
					previousstation=station;
					previousedge=edge;
				}
				
			}
			entry=data.getEntry(data.getLength());
			station=data.getLength();
			System.out.println("Line "+previousedge+":");
			int stationcount=station-previousstation;
			System.out.println(previousentry+" → "+entry+" ( "+stationcount+" station(s) )");
			System.out.println();			
		}	    			    
	    if(preferetion.equals("yes"))
	    	System.out.println(result/60+" minute(s)");
	    else
	    	System.out.println((int)result+" stop(s)");
	    
   }
}
