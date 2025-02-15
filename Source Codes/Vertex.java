import java.util.Iterator;
import java.util.NoSuchElementException;
import ADTPackage.*;
public class Vertex<T> implements VertexInterface<T> {

	private T label;

	private ListWithIteratorInterface<Edge> edgeList; // Edges to neighbors

	private boolean visited; // True if visited

	private VertexInterface<T> previousVertex; // On path to this

	private double cost; // Of path to this vertex

	public Vertex(T vertexLabel) {

		label = vertexLabel;

		edgeList = new LinkedListWithIterator<Edge>();

		visited = false;

		previousVertex = null;

		cost = 0;
	} // end constructor

	public boolean connect(VertexInterface<T> endVertex, double edgeWeight) {
		boolean result = false;
		if (!this.equals(endVertex))

		{ // Vertices are distinct
			Iterator<VertexInterface<T>> neighbors = getNeighborIterator();
			boolean duplicateEdge = false;
			while (!duplicateEdge && neighbors.hasNext()) {
				VertexInterface<T> nextNeighbor = neighbors.next();
				if (endVertex.equals(nextNeighbor))
					duplicateEdge = true;
			} // end while					
				edgeList.add(new Edge(endVertex, edgeWeight));
				result = true;			 
		} // end if
		return result;
	} // end connect

	public boolean connect(VertexInterface<T> endVertex) {
		return connect(endVertex, 0);
	} // end connect

	public Iterator<VertexInterface<T>> getNeighborIterator() {
		return new NeighborIterator();
	}
	
	public void visit() {
        visited = true;
    }

    @Override
    public void unvisit() {
        visited = false;
    }

    @Override
    public boolean isVisited() {
        return visited;
    }

    @Override
    public Iterator<Double> getWeightIterator() {
       LinkedListWithIterator<Double> weights = new LinkedListWithIterator<Double>();
        for (Edge edge : edgeList) {
            weights.add(edge.getWeight());
        }
        return weights.iterator();
    }

    @Override
    public boolean hasNeighbor() {
        return !edgeList.isEmpty();
    }

    @Override
    public VertexInterface<T> getUnvisitedNeighbor() {
        for (Edge edge : edgeList) {
            if (!edge.getEndVertex().isVisited()) {
                return edge.getEndVertex();
            }
        }
        return null;
    }

    @Override
    public void setPredecessor(VertexInterface<T> predecessor) {
        previousVertex = predecessor;
    }

    @Override
    public VertexInterface<T> getPredecessor() {
        return previousVertex;
    }

    @Override
    public boolean hasPredecessor() {
        return previousVertex != null;
    }

    @Override
    public void setCost(double newCost) {
        cost = newCost;
    }

    @Override
    public double getCost() {
        return cost;
    }
    public T getLabel() {
        return label;
    }

	protected class Edge {
		private VertexInterface<T> vertex; // Vertex at end of edge
		private double weight;

		protected Edge(VertexInterface<T> endVertex, double edgeWeight) {

			vertex = endVertex;

			weight = edgeWeight;
		} // end constructor

		protected VertexInterface<T> getEndVertex() {

			return vertex;
		} // end getEndVertex

		protected double getWeight() {

			return weight;
		} // end getWeight
	} // end Edge
	private class NeighborIterator implements Iterator<VertexInterface<T>>
	 {
	
	private Iterator<Edge> edges;
	
	
	private NeighborIterator()
	 {
	
	edges = edgeList.iterator();
	 } // end default constructor
	
	
	public boolean hasNext()
	 {
	
	return edges.hasNext();
	 } // end hasNext
	
	
	public VertexInterface<T> next()
	{
	
	VertexInterface<T> nextNeighbor = null;
	
	if (edges.hasNext())
	 {
	
	Edge edgeToNextNeighbor = edges.next();
	
	nextNeighbor = edgeToNextNeighbor.getEndVertex();
	 }
	
	else
	
	throw new NoSuchElementException();
	
	return nextNeighbor;
	 } // end next
	
	
	public void remove()
	 {
	
	throw new UnsupportedOperationException();
	 } // end remove
	 } // end NeighborIterator

}