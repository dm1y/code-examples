/** A class representing a graph. Stores an array of nodes and adjacency list.
 * 
 */
import java.awt.Point;

public class MapGraph {

	private CityNode[] nodes;

	// for each vertex store a linked list of edges;
	private Edge[] adjacencyList;

	private int numNodes = 0;
	private int numEdges = 0;

	public final int EPS_DIST = 5;

	MapGraph(int numNodes) {
		nodes = new CityNode[numNodes];
		adjacencyList = new Edge[numNodes];
	}

	/**
	 * Returns a node with index i
	 */
	CityNode getNode(int i) {
		return nodes[i];
	}

	/**
	 * Returns the head of the linked list of edges for a vertex with id = i
	 */
	Edge getEdge(int i) {
		return adjacencyList[i];
	}

	/**
	 * Adds a node to the graph
	 * 
	 * @param node
	 */
	public void addNode(CityNode node) {
		nodes[numNodes] = node;
		numNodes++;
	}

	public int numNodes() {
		return numNodes;
	}

	/**
	 * Adds the edge to the linked list for this vertexId
	 * 
	 * @param vertexId
	 * @param edge
	 */
	public void addEdge(int nodeId, Edge edge) {
		if (adjacencyList[nodeId] == null) {
			adjacencyList[nodeId] = edge;
			numEdges++;

		} else {
			Edge tmp = adjacencyList[nodeId];
			Edge prev = tmp;

			while (tmp != null) {
				prev = tmp;
				tmp = tmp.getNext();
			}

			prev.setNext(edge);
			numEdges++;

		}
	}

	/**
	 * Given the location of the click, return the node of the graph at this
	 * location.
	 */
	public CityNode getVertex(Point loc) {
		for (CityNode v : nodes) {
			Point p = v.getLocation();
			if ((Math.abs(loc.x - p.x) < EPS_DIST)
					&& (Math.abs(loc.y - p.y) < EPS_DIST))
				return v;
		}
		return null;
	}

	/**
	 * Returns the array of all edges for drawing: each element in the array
	 * corresponds to one edge and is the array of two Point objects
	 * (corresponding to the locations of the two nodes connected by this edge).
	 */
	public Point[][] getEdges() {
		Point[][] edges = new Point[numEdges][2];

		int counter = 0;

		for (int i = 0; i < numNodes; i++) {
			Edge curr = adjacencyList[i];

			while (curr != null) {

				edges[counter][0] = nodes[i].getLocation();
				edges[counter][1] = curr.getDest().getLocation();
				counter++;
				curr = curr.getNext();
			}

		}
		return edges;
	}

	/**
	 * Returns the array of nodes as points. Used by MapGraph to draw little
	 * circles at the location of the nodes
	 */
	public Point[] getNodeLocations() {
		Point[] locations = new Point[numNodes];

		for (int i = 0; i < numNodes; i++) {
			locations[i] = nodes[i].getLocation();
		}

		return locations;
	}

	/**
	 * Returns the array of cities corresponding to the vertices of this graph
	 * in the array
	 * 
	 * @return
	 */
	public String[] getCities() {
		String[] labels = new String[numNodes];

		for (int i = 0; i < numNodes; i++) {
			labels[i] = nodes[i].getCity();
		}
		return labels;
	}

} // class MapGraph
