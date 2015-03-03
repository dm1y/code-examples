import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// class HashTable uses open hashing

public class HashTable {

	private class Node {
		String city;
		int id;
		Node next;
		Node prev;

		Node(String city, int id) {
			this.city = city;
			this.id = id;
			prev = null;
			next = null;
		}

		@Override
		public String toString() {
			return city + " " + id;
		}
	}

	Node[] table;

	public HashTable(int size) {
		table = new Node[size];
	}

	// For debugging
	public void printList() {
		for (int i = 0; i < table.length; i++) {
			Node tmp = table[i];
			while (tmp != null) {
				System.out.println(i + " " + tmp);
				tmp = tmp.next;
			}
		}
	}

	void addNode(String city, int id) {
		int index = hash(city);

		Node node = new Node(city, id);

		if (table[index] == null) {
			table[index] = node;

		} else {
			Node tmp = table[index];
			Node pre = tmp;
			boolean add = false;

			while (tmp != null) {

				if (tmp.city.compareTo(node.city) > 0) {
					node.next = tmp;
					node.prev = tmp.prev;

					if (tmp.prev != null) {
						tmp.prev = node;
					} else {
						table[index] = node;
					}

					add = true;
					break;
				}
				pre = tmp;
				tmp = tmp.next;
			}

			if (!add) {
				node.next = pre.next;
				node.prev = pre;
				pre.next = node;
			}
		}
	}

	private int hash(String city) {
		long index = 0;

		for (int i = 0; i < city.length(); i++) {
			index = (index << 4) + city.charAt(i);
		}

		index = index % table.length;

		return (int) index;
	}

	public int get(String city) {
		int index = hash(city.trim());

		if (city.equals(table[index].city)) {
			return table[index].id;

		} else {
			Node tmp = table[index];
			while (tmp != null) {
				if (city.equals(tmp.city)) {
					return tmp.id;
				}
				tmp = tmp.next;
			}
		}

		// This should never hit.
		System.out.println("Error: Unable to find city.");
		return -1;
	}

	public static void main(String[] args) throws NumberFormatException,
			IOException {

		BufferedReader reader = new BufferedReader(new FileReader("USA.txt"));

		String line = reader.readLine();

		int size = Integer.parseInt(reader.readLine());

		HashTable x = new HashTable(size);

		int i = 0;

		System.out.println(size);

		while (!(line = reader.readLine()).equals("ARCS")) {
			String[] info = line.split("\\s");
			x.addNode(info[0], i);
			i++;
		}

		x.printList();
	}
}