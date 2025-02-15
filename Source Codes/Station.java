
public class Station {
   private String stop_name;
   private int arrival_time;
   private int stop_sequence;
   private String direction_id;
   private String route_short_name;
   
   public Station(String stop_name,int arrival_time,int stop_sequence,String direction_id,String route_short_name) {//constructor
	   this.stop_name=stop_name;
	   this.arrival_time=arrival_time;
	   this.stop_sequence=stop_sequence;
	   this.direction_id=direction_id;
	   this.route_short_name=route_short_name;
   }
   // get methods
   public String getStopName() {
	   return stop_name;
   }
   public int getArrivalTime() {
	   return arrival_time;
   }
   public int getStopSequence() {
	   return stop_sequence;
   }
   public String getDirectionId() {
	   return direction_id;
   }
   public String getRouteShortName() {
	   return route_short_name;
   }
   

}
