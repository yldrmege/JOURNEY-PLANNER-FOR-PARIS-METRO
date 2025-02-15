import ADTPackage.*;
public interface GraphAlgorithmsInterface<T>
 {
	public QueueInterface<T> getBreadthFirstTraversal(T origin);
	
	public QueueInterface<T> getDepthFirstTraversal(T origin);
	
	public StackInterface<T> getTopologicalOrder();
	
	public int getShortestPath(T begin, T end, StackInterface<T> path);
	
	public double getCheapestPath(T begin, T end, StackInterface<T>
	path);

 } // end GraphAlgorithmsInterface