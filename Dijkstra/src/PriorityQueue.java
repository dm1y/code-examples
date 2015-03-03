public class PriorityQueue {

	public class PQNode {
		int elem;
		int priority;

		PQNode(int x, int y) {
			elem = x;
			priority = y;
		}

		@Override
		public String toString() {
			return elem + " " + priority;
		}
	}

	private PQNode[] heap;
	private int[] positions;

	private int maxsize;
	private int length;

	public PriorityQueue(int max) {
		maxsize = max;
		heap = new PQNode[maxsize + 1];
		positions = new int[maxsize];

		length = 0;

		for (int i = 0; i < maxsize + 1; i++) {
			heap[i] = new PQNode(Integer.MAX_VALUE, Integer.MAX_VALUE);
		}

		heap[0] = new PQNode(Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	private int leftchild(int pos) {
		return 2 * pos;
	}

	private int parent(int pos) {
		return pos / 2;
	}

	private boolean isleaf(int pos) {
		return ((pos > length / 2) && (pos <= length));
	}

	private void swap(int pos1, int pos2) {
		PQNode tmp;

		int temp = positions[heap[pos1].elem];
		positions[heap[pos1].elem] = positions[heap[pos2].elem];
		positions[heap[pos2].elem] = temp;

		tmp = heap[pos1];
		heap[pos1] = heap[pos2];
		heap[pos2] = tmp;
	}

	// Inserts the integer element into the queue, using the priority.
	public void insert(int elem, int priority) {
		length++;
		heap[length] = new PQNode(elem, priority);
		positions[elem] = length;
		int current = length;

		while (heap[current].priority < heap[parent(current)].priority) {
			swap(current, parent(current));
			current = parent(current);
		}
	}

	public boolean isEmpty() {
		return length == 0;
	}

	public void print() {
		int i;
		for (i = 1; i <= length; i++)
			System.out.println(i + " " + heap[i] + " ");
		System.out.println("END");
	}

	// Removes the element with the smallest priority from the
	// queue, and returns it.
	public int remove_min() {
		swap(1, length);
		length--;
		if (length != 0)
			pushdown(1);
		return heap[length + 1].elem;
	}

	private void pushdown(int position) {
		int smallestchild;
		while (!isleaf(position)) {
			smallestchild = leftchild(position);
			if ((smallestchild < length)
					&& (heap[smallestchild].priority > heap[smallestchild + 1].priority))
				smallestchild = smallestchild + 1;
			if (heap[position].priority <= heap[smallestchild].priority)
				return;
			swap(position, smallestchild);
			position = smallestchild;
		}
	}

	// Reduce the priority of the element elem in the priority queue to
	// new_priority, rearranging the queue as necessary. To implement this
	// method efficiently, you will need to keep track of where each element
	// is in the queue. The simplest way to do it is to store an array of
	// pointers into the queue.
	void reduce_key(int elem, int new_priority) {

		// If the element is not in the queue, insert it
		if (positions[elem] == 0) {
			insert(elem, new_priority);

		} else {
			// Adjust its priority if it already exists in the queue
			int index = positions[elem];
			heap[index].priority = new_priority;

			int pos = index;

			// Rearrange if needed
			while (heap[pos].priority < heap[parent(pos)].priority) {
				swap(pos, parent(pos));
				pos = parent(pos);
			}
		}

	}
}
