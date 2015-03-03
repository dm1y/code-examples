/**
 * Individual node of FlightList
 * 
 * @author Diana
 * 
 */
public class FlightNode {

	private FlightKey key;
	private FlightData value;
	private FlightNode next, prev, down, up;

	public FlightNode() {
		key = null;
		value = null;
		next = null;
		prev = null;
		down = null;
		up = null;
	}

	public FlightNode(FlightKey key, FlightData value) {
		this.key = key;
		this.value = value;
		next = null;
		prev = null;
		down = null;
		up = null;
	}

	public FlightNode(FlightKey key) {
		this.key = key;
		next = null;
		prev = null;
		down = null;
		up = null;
	}

	public FlightKey getKey() {
		return this.key;
	}

	public FlightData getValue() {
		return this.value;
	}

	public FlightNode getNext() {
		return this.next;
	}

	public FlightNode getPrev() {
		return this.prev;
	}

	public FlightNode getUp() {
		return this.up;
	}

	public FlightNode getDown() {
		return this.down;
	}

	public void setNext(FlightNode x) {
		this.next = x;
	}

	public void setPrev(FlightNode x) {
		this.prev = x;
	}

	public void setUp(FlightNode x) {
		this.up = x;
	}

	public void setDown(FlightNode x) {
		this.down = x;
	}

	@Override
	public String toString() {
		return key + " " + value;
	}
}
