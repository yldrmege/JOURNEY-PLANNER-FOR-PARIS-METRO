
import java.util.Iterator;
import java.util.PriorityQueue;
import ADTPackage.*; // Classes that implement various ADTs
import java.util.List;
import java.util.ArrayList;
public class DirectedGraph<T> implements GraphInterface<T> {

	private DictionaryInterface<T, VertexInterface<T>> vertices;

	private int edgeCount;

	public DirectedGraph() {
		vertices = new LinkedDictionary<>();//dictionary where vertexes are kept
		
		edgeCount = 0;
	} // end default constructor
	public DictionaryInterface<T,VertexInterface<T>> getVerticesDict(){
		return vertices;
	}	
	public boolean addVertex(T vertexLabel) {
		VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));

		return addOutcome == null; // Was addition to dictionary successful?
	} // end addVertex

	public boolean addEdge(T begin, T end, double edgeWeight) {
		boolean result = false;

		VertexInterface<T> beginVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);
		if ((beginVertex != null) && (endVertex != null))
			result = beginVertex.connect(endVertex, edgeWeight);
		if (result)
			edgeCount++;
		return result;
	} // end addEdge

	public boolean addEdge(T begin, T end) {
		return addEdge(begin, end, 0);
	} // end addEdge

	public boolean hasEdge(T begin, T end) {
		boolean found = false;

		VertexInterface<T> beginVertex = vertices.getValue(begin);

		VertexInterface<T> endVertex = vertices.getValue(end);

		if ((beginVertex != null) && (endVertex != null)) {
			Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();
			while (!found && neighbors.hasNext()) {
				VertexInterface<T> nextNeighbor = neighbors.next();
				if (endVertex.equals(nextNeighbor))
					found = true;
			} // end while
		} // end if
		return found;
	} // end hasEdge
	
	
	protected void resetVertices()
	{
	  Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
	  while (vertexIterator.hasNext())
	  {
	    VertexInterface<T> nextVertex = vertexIterator.next();
	    nextVertex.unvisit();
	    nextVertex.setCost(0);
	    nextVertex.setPredecessor(null);
	  } // end while
	} // end resetVertices
	
	public boolean isEmpty() {
		return vertices.isEmpty();
	} // end isEmpty

	public void clear() {
		vertices.clear();
		edgeCount = 0;
	} // end clear

	public int getNumberOfVertices() {
		return vertices.getSize();
	} // end getNumberOfVertices

	public int getNumberOfEdges() {
		return edgeCount;
	} // end getNumberOfEdges
	
	
	/**
	 * Finds the shortest path between two vertices in the graph using breadth-first search.
	 *
	 * @param begin The label of the starting vertex.
	 * @param end The label of the destination vertex.
	 * @param path A stack to store the vertices in the shortest path.
	 * @return The length of the shortest path, or 0 if either the starting or destination vertex is not found.
	 */
	public int getShortestPath(T begin, T end, StackInterface<T> path) {
	    boolean done = false;
	    resetVertices(); // Resets the visited status, cost, and predecessor of all vertices.
	    QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<VertexInterface<T>>();

	    VertexInterface<T> originVertex = vertices.getValue(begin);
	    VertexInterface<T> endVertex = vertices.getValue(end);

	    // Check if either the starting or destination vertex is not found.
	    if (originVertex == null || endVertex == null) {
	        return 0;
	    }

	    originVertex.visit(); // Mark the starting vertex as visited.
	    vertexQueue.enqueue(originVertex); // Enqueue the starting vertex.

	    // Perform breadth-first search to find the shortest path.
	    while (!done && !vertexQueue.isEmpty()) {
	        VertexInterface<T> frontVertex = vertexQueue.dequeue();
	        Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();

	        while (!done && neighbors.hasNext()) {
	            VertexInterface<T> nextNeighbor = neighbors.next();

	            if (!nextNeighbor.isVisited()) {
	                // If the neighbor is not visited, mark it as visited, update cost and predecessor, and enqueue.
	                nextNeighbor.visit();
	                nextNeighbor.setCost(1 + frontVertex.getCost());
	                nextNeighbor.setPredecessor(frontVertex);
	                vertexQueue.enqueue(nextNeighbor);
	            }

	            if (nextNeighbor.equals(endVertex)) {
	                // If the destination vertex is reached, set done to true to break out of the loop.
	                done = true;
	            }
	        }
	    }

	    // Retrieve the length of the shortest path.
	    int pathLength = (int) endVertex.getCost();

	    // Push the vertices of the shortest path onto the stack.
	    path.push(endVertex.getLabel());
	    VertexInterface<T> vertex = endVertex;

	    while (vertex.hasPredecessor()) {
	        vertex = vertex.getPredecessor();
	        path.push(vertex.getLabel());
	    }

	    return pathLength;
	}

	public double getCheapestPath(T begin, T end, StackInterface<T> path) 
	{
		resetVertices();
		boolean done = false;

		// use EntryPQ instead of Vertex because multiple entries contain 
		// the same vertex but different costs - cost of path to vertex is EntryPQ's priority value
		PriorityQueue<EntryPQ> priorityQueue = new PriorityQueue<EntryPQ>();
		
		VertexInterface<T> originVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);
		if(originVertex==null||endVertex==null) {			
			return 0;
		}	
		priorityQueue.add(new EntryPQ(originVertex, 0, null));
	
		while (!done && !priorityQueue.isEmpty())
		{
			EntryPQ frontEntry = priorityQueue.poll();
			VertexInterface<T> frontVertex = frontEntry.getVertex();
			
			if (!frontVertex.isVisited())
			{
				frontVertex.visit();
				frontVertex.setCost(frontEntry.getCost());
				frontVertex.setPredecessor(frontEntry.getPredecessor());
				
				if (frontVertex.equals(endVertex))
					done = true;
				else 
				{
					Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
					Iterator<Double> edgeWeights = frontVertex.getWeightIterator();
					while (neighbors.hasNext())
					{
						VertexInterface<T> nextNeighbor = neighbors.next();
						Double weightOfEdgeToNeighbor = edgeWeights.next();
						
						if (!nextNeighbor.isVisited())
						{
							double nextCost = weightOfEdgeToNeighbor + frontVertex.getCost();							
							priorityQueue.add(new EntryPQ(nextNeighbor, nextCost, frontVertex));
						} // end if
					} // end while
				} // end if
			} // end if
		} // end while		
		// traversal ends, construct cheapest path				
			double pathCost = endVertex.getCost();		
			path.push(endVertex.getLabel());
			
			VertexInterface<T> vertex = endVertex;
			while (vertex.hasPredecessor())
			{
				vertex = vertex.getPredecessor();
				path.push(vertex.getLabel());
			} // end while
				
		return pathCost;		
	} // end getCheapestPath
	
	private class EntryPQ implements Comparable<EntryPQ> 	
	{
		private VertexInterface<T> vertex; 	
		private VertexInterface<T> previousVertex; 
		private double cost; // cost to nextVertex
		
		private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex)
		{
			this.vertex = vertex;
			this.previousVertex = previousVertex;
			this.cost = cost;
		} // end constructor
		
		public VertexInterface<T> getVertex()
		{
			return vertex;
		} // end getVertex
		
		public VertexInterface<T> getPredecessor()
		{
			return previousVertex;
		} // end getPredecessor

		public double getCost()
		{
			return cost;
		} // end getCost
		
		public int compareTo(EntryPQ otherEntry)
		{
			// using opposite of reality since our priority queue uses a maxHeap;
			// could revise using a minheap
			return (int)Math.signum(cost - otherEntry.cost);
		} // end compareTo
		
		public String toString()
		{
			return vertex.toString() + " " + cost;
		} // end toString 
	} // end EntryPQ

	@Override
	public QueueInterface<T> getBreadthFirstTraversal(T origin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueueInterface<T> getDepthFirstTraversal(T origin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StackInterface<T> getTopologicalOrder() {
		// TODO Auto-generated method stub
		return null;
	}	
	public List<List<T>> alternativePaths(T start, T end) {
        VertexInterface<T> startVertex = vertices.getValue(start);
        VertexInterface<T> endVertex = vertices.getValue(end);
        List<List<T>> allPaths = new ArrayList<>();
        List<T> visited = new ArrayList<>();
        if (startVertex != null && endVertex != null) {
            visited.add(start);
            findAllPathsHelper(startVertex, endVertex, visited, allPaths);
        }
        return allPaths;
    }

    private void findAllPathsHelper(VertexInterface<T> current, VertexInterface<T> endVertex, List<T> visited, List<List<T>> allPaths) {    	
        T currentLabel = current.getLabel();        
        if (currentLabel.equals(endVertex.getLabel())) {
            // Found a path, add a copy of visited list to allPaths
            allPaths.add(new ArrayList<>(visited));
            return;
        }

        Iterator<VertexInterface<T>> neighbors = current.getNeighborIterator();
        while (neighbors.hasNext()) {
            VertexInterface<T> neighbor = neighbors.next();
            T neighborLabel = neighbor.getLabel();
            
            if (!visited.contains(neighborLabel)) {
                visited.add(neighborLabel);
                findAllPathsHelper(neighbor, endVertex, visited, allPaths);
                //visited.remove(visited.size()-1);

            }
        }
    }


			
} // end DirectedGraph
