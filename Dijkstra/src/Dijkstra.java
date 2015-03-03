import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dijkstra {

	private int sourceVertex = -1; // store the id of the source vertex
	private MapGraph graph;

	private HashTable table;
	private PriorityQueue queue;
	private int[][] dijkstra; // 0 = cost, 1 = path

	Dijkstra(String filename) {
		loadGraph(filename);
	}

	public MapGraph getGraph() {
		return graph;
	}

	/**
	 * Compute all the shortest paths from the source vertex to all the other
	 * vertices in the graph; This function is called from GUIApp, when the user
	 * clicks on the city.
	 */
	public void computePaths(CityNode vSource) {
		int size = graph.numNodes();

		dijkstra = new int[size][2];

		for (int i = 0; i < size; i++) {
			dijkstra[i][0] = Integer.MAX_VALUE;
			dijkstra[i][1] = -1;
		}

		sourceVertex = table.get(vSource.getCity());
		dijkstra[sourceVertex][0] = 0;

		// Create the queue
		queue = new PriorityQueue(size);

		// Insert source vertex
		queue.insert(sourceVertex, 0);

		while (!queue.isEmpty()) {
			int min = queue.remove_min();
			Edge e = graph.getEdge(min);

			// Visit each edge
			for (Edge x = e; x != null; x = x.getNext()) {
				int i = table.get(x.getDest().getCity());
				int new_dist = x.getDist() + dijkstra[min][0];

				// If the cost is less, reduce key and update tables
				if (new_dist < dijkstra[i][0]) {
					queue.reduce_key(i, new_dist);
					dijkstra[i][0] = new_dist;
					dijkstra[i][1] = min;
				}
			}
		}
	}

	/**
	 * Returns the shortest path between the source vertex and this vertex.
	 * Returns the array of node id-s on the path
	 */
	public ArrayList<Integer> shortestPath(CityNode vTarget) {
		ArrayList<Integer> path = new ArrayList<>();
		ArrayList<Integer> result = new ArrayList<>();

		int target = table.get(vTarget.getCity());
		int p = target;

		while (p != -1) {
			path.add(p);
			p = dijkstra[p][1];
		}

		for (int i = path.size() - 1; i >= 0; i--) {
			result.add(path.get(i));
		}

		return result;
	}

	/**
	 * Loads graph info from the text file into MapGraph graph
	 * 
	 * @param filename
	 */
	public void loadGraph(String filename) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();

			int size = Integer.parseInt(reader.readLine());

			graph = new MapGraph(size);
			table = new HashTable(size);

			int index = 0;

			while (!(line = reader.readLine()).equals("ARCS")) {
				String[] info = line.split("\\s");

				CityNode node = new CityNode(info[0],
						Double.parseDouble(info[1]),
						Double.parseDouble(info[2]));

				graph.addNode(node);
				table.addNode(info[0], index);

				index++;
			}

			while ((line = reader.readLine()) != null) {
				String[] info = line.split("\\s");

				CityNode city1 = graph.getNode(table.get(info[0]));
				CityNode city2 = graph.getNode(table.get(info[1]));
				int dist = Integer.parseInt(info[2]);

				Edge edge = new Edge(city1, city2, dist);
				Edge edge2 = new Edge(city2, city1, dist);

				graph.addEdge(table.get(info[0]), edge);
				graph.addEdge(table.get(info[1]), edge2);
			}

		} catch (IOException e) {
			System.out.println("Error: Graph information cannot be loaded.");
			// e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		// Create an instance of the Dijkstra class
		// The parameter is the name of the file
		Dijkstra dijkstra = new Dijkstra(args[0]);

		// create the GUI window with the panel
		GUIApp app = new GUIApp(dijkstra);
	}
}
