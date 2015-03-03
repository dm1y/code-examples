/**
 * Edge class represents a single node in the linked list of edges for a vertex.
 * 
 */

class Edge {

	private CityNode origin;
	private CityNode dest;
	private int dist;
	private Edge next;

	// FILL IN VARIABLES and METHODS
	public Edge(CityNode origin, CityNode dest, int dist) {
		this.origin = origin;
		this.dest = dest;
		this.dist = dist;
		this.next = null;
	}

	public void setNext(Edge x) {
		next = x;
	}

	public Edge getNext() {
		return next;
	}

	public CityNode getOrigin() {
		return origin;
	}

	public CityNode getDest() {
		return dest;
	}

	public int getDist() {
		return dist;
	}

	@Override
	public String toString() {
		return origin + " " + dest + " " + dist;
	}
}