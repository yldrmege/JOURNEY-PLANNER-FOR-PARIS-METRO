import ADTPackage.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Math;
import java.util.HashMap;
public class Test {

	public static void main(String[] args) {
		DirectedGraph<String> paris_metro=new DirectedGraph<String>();			
		HashMap<String,LList<String>>edgeinformation=new HashMap<String,LList<String>>();		
		String beginVertex="";
		String finishVertex="";
		String preferetion="";
		Scanner input=new Scanner(System.in);		
		System.out.print("Do you want to select stations or read the excel file?(yes for excel file):");		
		String selection=input.nextLine();
		selection=selection.toLowerCase();
		System.out.println();
	    if(!selection.equals("yes")) {
	    	System.out.print("Origin Station:");		
			beginVertex=input.nextLine();				
			System.out.println();
			System.out.print("Destination Station:");
			finishVertex=input.nextLine();			
			System.out.println();
			System.out.print("Minimum Time or Fewer Stops(Write yes for Minimum Time):");
			preferetion=input.nextLine();
			preferetion=preferetion.toLowerCase();
			System.out.println();
	    }		
		input.close();
		
		Station beginStation;
		Station endStation;
		String stopnames;			
		try {			// read excel file
			int sequence_difference=0;
			double arrival_time=0;				
			File file=new File("Paris_RER_Metro_v2.csv");
			Scanner scanner=new Scanner(file);			
			scanner.nextLine();
			String line1=scanner.nextLine();
			String[] lines1=line1.split(",");
			beginStation=new Station(lines1[1],Integer.parseInt(lines1[2]),Integer.parseInt(lines1[3]),lines1[4],lines1[5]);							
			while(scanner.hasNext()) {//file reading process				
				String line=scanner.nextLine();
				String[] lines=line.split(",");
				endStation=new Station(lines[1],Integer.parseInt(lines[2]),Integer.parseInt(lines[3]),lines[4],lines[5]);		
				paris_metro.addVertex(beginStation.getStopName());
				paris_metro.addVertex(endStation.getStopName());
				sequence_difference=endStation.getStopSequence()-beginStation.getStopSequence();		
				// control situation for 2 vertices
				if(sequence_difference==1&&beginStation.getDirectionId().equals(endStation.getDirectionId())&&beginStation.getRouteShortName().equals(endStation.getRouteShortName())) {
					arrival_time=beginStation.getArrivalTime()-endStation.getArrivalTime();				
					paris_metro.addEdge(beginStation.getStopName(),endStation.getStopName(),Math.abs(arrival_time));					
					stopnames=beginStation.getStopName()+endStation.getStopName();			
					if(edgeinformation.containsKey(stopnames)) {// adding HashMap 													
							LList<String>values=edgeinformation.get(stopnames);
							values.add(beginStation.getRouteShortName()+","+String.valueOf(Math.abs(arrival_time)));
							edgeinformation.put(stopnames, values);
					}
					else {
							LList<String>values=new LList<>();
							values.add(beginStation.getRouteShortName()+","+String.valueOf(Math.abs(arrival_time)));
							edgeinformation.put(stopnames, values);
					}					
				}				
				beginStation=new Station(endStation.getStopName(),endStation.getArrivalTime(),endStation.getStopSequence()
						,endStation.getDirectionId(),endStation.getRouteShortName());																
			}				
			 scanner.close();
		}catch (FileNotFoundException e) {
			System.out.println("File not found");			
			//e.printStackTrace();			
		}	
		try {
			File file=new File("walk_edges.txt");
			Scanner scanner=new Scanner(file);			
			while(scanner.hasNext()) {
				String line=scanner.nextLine();
				String[] lines=line.split(",");
				paris_metro.addEdge(lines[0],lines[1],300);
				
				stopnames=lines[0]+lines[1];			
				if(edgeinformation.containsKey(stopnames)) {													
						LList<String>values=edgeinformation.get(stopnames);
						values.add("Walk"+","+String.valueOf(Math.abs(300)));
						edgeinformation.put(stopnames, values);
				}
				else {
						LList<String>values=new LList<>();
						values.add("Walk"+","+String.valueOf(Math.abs(300)));
						edgeinformation.put(stopnames, values);
				}	
			}					
			scanner.close();
		}catch (FileNotFoundException e) {
			System.out.println("File not found");			
			e.printStackTrace();		
		}		
		if(!selection.equals("yes")) {
			Operation.GetInfo(beginVertex, finishVertex, preferetion, paris_metro, edgeinformation);
		}
		else {			
			long start=0;			
			long end=0;			
			double result=0;
			int stop=0;
			double min=0;
			System.out.println();	
			LinkedStack<String>path=new LinkedStack<>();
			try {
				File file=new File("Test100.csv");
				Scanner scanner=new Scanner(file);			
				scanner.nextLine();				
				while(scanner.hasNext()) {					
					String line=scanner.nextLine();
					String[] lines=line.split(",");
					start=System.currentTimeMillis();
					if(lines[2].equals("0")) {	
						stop=paris_metro.getShortestPath(lines[0], lines[1], path);
					}
					else if(lines[2].equals("1")) {
						min=paris_metro.getCheapestPath(lines[0], lines[1], path);
					}																
					end=System.currentTimeMillis();
					result+=end-start;
					if(lines[2].equals("0")) {
						System.out.println(lines[0]+" → "+lines[1]+" :"+stop+" stop(s)");
					}
					else if(lines[2].equals("1")) {
						System.out.println(lines[0]+" → "+lines[1]+" :"+min/60+" minute(s)");
					}
					System.out.println();					
				}				
				
				System.out.println("Total Time is:"+result+" ms");
				System.out.println();
				System.out.println("Average Time is:"+result/100+" ms");
				scanner.close();
			}catch (FileNotFoundException e) {
				System.out.println("File not found");			
				//e.printStackTrace();			
			}	
			
		}		
		//System.out.println(paris_metro.alternativePaths("Château de Vincennes", "Reuilly-Diderot"));
 }
	
}
