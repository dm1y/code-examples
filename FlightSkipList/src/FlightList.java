import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Flight database that is implemented as a skip list
 * 
 * @author Diana
 */
public class FlightList {

	FlightNode root, upperlevel;
	FlightNode endpt, endpt2;

	FlightList() {
		root = new FlightNode();
		endpt = new FlightNode();

		upperlevel = new FlightNode();
		endpt2 = new FlightNode();

		root.setNext(endpt);
		endpt.setPrev(root);

		upperlevel.setNext(endpt2);
		endpt2.setPrev(upperlevel);

		root.setUp(upperlevel);
		upperlevel.setDown(root);

		endpt.setUp(endpt2);
		endpt2.setDown(endpt);
	}

	FlightList(String filename) {
		try {
			root = new FlightNode();
			endpt = new FlightNode();

			upperlevel = new FlightNode();
			endpt2 = new FlightNode();

			root.setNext(endpt);
			endpt.setPrev(root);

			upperlevel.setNext(endpt2);
			endpt2.setPrev(upperlevel);

			root.setUp(upperlevel);
			upperlevel.setDown(root);

			endpt.setUp(endpt2);
			endpt2.setDown(endpt);

			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;

			while ((line = reader.readLine()) != null) {
				String[] info = line.split("\\s");
				FlightKey key = new FlightKey(info[0], info[1], info[2],
						info[3]);
				FlightData val = new FlightData(info[4],
						Integer.parseInt(info[5]));
				insert(key, val);
			}

			reader.close();

		} catch (IOException e) {
			System.out.println("Error: Unable to read file.");
		}
	}

	/**
	 * Insert a (key, value) pair to the skiplist. The key is of type FlightKey
	 * and stores origin, dest, date and time as strings. Return true if it was
	 * able to successfully insert the element.
	 */
	public boolean insert(FlightKey key, FlightData data) {
		FlightNode node = new FlightNode(key, data);
		insert(node, upperlevel);
		return true;
	}

	/**
	 * Helper for insert
	 */
	private boolean insert(FlightNode node, FlightNode start) {
		FlightNode curr = start;
		FlightNode prev = curr;

		while (curr.getNext() != null) {
			if (curr.getKey() != null
					&& curr.getKey().compareTo(node.getKey()) >= 0) {
				break;
			}
			prev = curr;
			curr = curr.getNext();
		}

		// Base Case
		if (curr.getDown() == null) {
			node.setNext(curr);
			node.setPrev(prev);
			prev.setNext(node);
			curr.setPrev(node);

			coinToss(node, root, endpt);

			return true;
		}

		return insert(node, prev.getDown());
	}

	/**
	 * Helper method for insert. Adds a node to the upper level if when coin is
	 * heads (0). Stops when coin is tails (1)
	 */
	private void coinToss(FlightNode node, FlightNode start, FlightNode end) {
		int coin = new Random().nextInt(2);

		if (coin == 0) {
			// Creates new node and links it to the added node in the root
			FlightNode new_node = new FlightNode(node.getKey(), node.getValue());
			node.setUp(new_node);
			new_node.setDown(node);

			// Check whether the level above is empty
			if (start.getUp().getNext().getKey() == null) {

				// Make a new level if the above level is empty
				FlightNode top_level = new FlightNode();
				FlightNode top_right = new FlightNode();
				top_level.setNext(top_right);
				top_right.setPrev(top_level);

				// Link the new level with the bottom
				top_level.setUp(start.getUp());
				top_level.setDown(start);

				start.getUp().setDown(top_level);

				top_right.setUp(start.getUp().getNext());
				top_right.setDown(end);

				end.getUp().setDown(top_right);

				start.setUp(top_level);
				end.setUp(top_right);
			}

			FlightNode curr = start.getUp();
			FlightNode prev = curr;

			while (curr.getNext() != null) {
				if (curr.getKey() != null
						&& curr.getKey().compareTo(new_node.getKey()) >= 0) {
					break;
				}
				prev = curr;
				curr = curr.getNext();
			}

			new_node.setNext(curr);
			new_node.setPrev(prev);
			prev.setNext(new_node);
			curr.setPrev(new_node);

			coinToss(new_node, start.getUp(), end.getUp());
		}
	}

	/**
	 * Returns true if it was able to find the entry with a given key in the
	 * skip list.
	 */
	boolean find(FlightKey key) {
		return find(key, upperlevel);
	}

	/**
	 * Helper for find
	 */
	private boolean find(FlightKey key, FlightNode start) {
		FlightNode curr = start;

		while (curr.getNext() != null) {
			if (curr.getKey() != null) {
				if (curr.getKey().compareTo(key) == 0) {
					return true;
				}
			}
			curr = curr.getNext();
		}

		if (start.getDown() == null) {
			return false;
		}

		return find(key, start.getDown());
	}

	/**
	 * Returns a list of nodes that have the same origin and destination cities
	 * and the same date as the key, with departure times in increasing order
	 * from the requested departure time.
	 */
	ArrayList<FlightNode> successors(FlightKey key) {
		ArrayList<FlightNode> successors = new ArrayList<>();

		successors = successors(key, upperlevel);

		return successors;
	}

	/**
	 * Helper method for successors
	 */
	private ArrayList<FlightNode> successors(FlightKey key, FlightNode start) {
		FlightNode curr = findCurrPos(key, start);

		if (start.getDown() == null) {
			ArrayList<FlightNode> s = new ArrayList<>();

			while (curr.getNext() != null) {
				if (curr.getKey() != null) {
					if (curr.getKey().getOrigin()
							.equalsIgnoreCase(key.getOrigin())
							&& curr.getKey().getDest()
									.equalsIgnoreCase(key.getDest())
							&& curr.getKey().getDate()
									.equalsIgnoreCase(key.getDate())
							&& curr.getKey().getHour().compareTo(key.getHour()) > 0) {
						s.add(curr);
					} else if (curr.getKey().compareTo(key) > 0) {
						break;
					}
				}
				curr = curr.getNext();
			}
			return s;
		}
		return successors(key, start.getDown());
	}

	/**
	 * Helper method for successors and predecessors to calculate the current
	 * position of the FlightNode
	 */
	private FlightNode findCurrPos(FlightKey key, FlightNode start) {
		FlightNode curr = start;
		FlightNode prev = curr;

		while (curr.getNext() != null && curr.getNext().getKey() != null) {
			if (curr.getKey() != null) {
				if (curr.getKey().compareTo(key) >= 0) {
					break;
				}
			}
			prev = curr;
			curr = curr.getNext();
		}
		return prev;
	}

	/**
	 * Returns a list of nodes that have the same origin and destination cities
	 * and the same date as the key, with departure times in decreasing order
	 * from the requested departure time.
	 */
	ArrayList<FlightNode> predecessors(FlightKey key) {
		ArrayList<FlightNode> predecessors = new ArrayList<>();

		predecessors = predecessors(key, upperlevel);

		return predecessors;
	}

	/**
	 * Helper for predecessors
	 */
	private ArrayList<FlightNode> predecessors(FlightKey key, FlightNode start) {
		FlightNode curr = findCurrPos(key, start);

		if (start.getDown() == null) {
			LinkedList<FlightNode> s = new LinkedList<>();

			while (curr.getPrev() != null) {
				if (curr.getKey() != null) {
					if (curr.getKey().getOrigin()
							.equalsIgnoreCase(key.getOrigin())
							&& curr.getKey().getDest()
									.equalsIgnoreCase(key.getDest())
							&& curr.getKey().getDate()
									.equalsIgnoreCase(key.getDate())
							&& curr.getKey().getHour().compareTo(key.getTime()) <= 0) {
						s.addFirst(curr);
					} else {
						break;
					}
				}
				curr = curr.getPrev();
			}
			// Convert back to ArrayList
			List<FlightNode> pred = new ArrayList<FlightNode>(s);
			ArrayList<FlightNode> p = (ArrayList<FlightNode>) pred;

			return p;
		}
		return predecessors(key, start.getDown());
	}

	/**
	 * Returns a list of nodes that have the same origin and destination cities
	 * and the same date as the key, with departure times within the
	 * timeDifference of the departure time of the key.
	 */
	ArrayList<FlightNode> findFlights(FlightKey key, int timeDifference) {
		ArrayList<FlightNode> flights = new ArrayList<>();

		flights = findFlights(key, timeDifference, upperlevel, flights);

		return flights;
	}

	/**
	 * Helper for findFlights
	 */
	private ArrayList<FlightNode> findFlights(FlightKey key, int timediff,
			FlightNode start, ArrayList<FlightNode> flights) {
		ArrayList<FlightNode> temp = new ArrayList<>();

		temp.addAll(predecessors(key));

		if (find(key)) {
			FlightNode orig = getNode(key, start);
			temp.add(orig);
		}

		temp.addAll(successors(key));

		for (FlightNode x : temp) {

			int flight = Integer.parseInt(x.getKey().getTime().substring(0, 2));
			int k = Integer.parseInt(key.getTime().substring(0, 2));
			int diff = Math.abs(flight - k);

			if (diff <= timediff) {
				flights.add(x);
			}
		}

		return flights;
	}

	/**
	 * Prints out the skip list (flight key and flight value)
	 */
	public void print() {
		print(upperlevel);
	}

	/**
	 * Helper for print method
	 */
	private void print(FlightNode start) {
		FlightNode curr = start;

		while (curr.getNext() != null && curr.getNext().getKey() != null) {
			System.out.print("[" + curr.getNext() + "] ");
			curr = curr.getNext();
		}

		if (start.getDown() != null) {
			System.out.println();
			print(start.getDown());
		}
	}

	/**
	 * Removes the key from the skip list
	 */
	public boolean remove(FlightKey key) {
		if (!find(key)) {
			return false;
		}

		return remove(key, upperlevel);
	}

	private boolean remove(FlightKey key, FlightNode start) {
		FlightNode curr = start;
		FlightNode prev = curr;

		while (curr.getNext() != null && curr.getNext().getKey() != null) {
			if (curr.getKey() != null) {
				if (curr.getKey().compareTo(key) == 0) {
					curr.getNext().setPrev(prev);
					prev.setNext(curr.getNext());
					// No break to delete any duplicates
				}
			}
			prev = curr;
			curr = curr.getNext();
		}

		if (start.getDown() == null) {
			while (curr.getNext() != null && curr.getNext().getKey() != null) {
				if (curr.getKey() != null) {
					if (curr.getKey().compareTo(key) == 0) {
						curr.getNext().setPrev(prev);
						prev.setNext(curr.getNext());
						break;
					}
				}
				prev = curr;
				curr = curr.getNext();
			}
			return true;
		}

		return remove(key, start.getDown());
	}

	/**
	 * Suggests a flight with the departure date three days before and after the
	 * requested departure date if it cannot find any flights on the specified
	 * date
	 */
	public ArrayList<FlightNode> suggestFlights(FlightKey key) {
		ArrayList<FlightNode> temp = new ArrayList<>();

		if (find(key)) {
			temp.add(getNode(key, upperlevel));
		}

		temp.addAll(getSameDay(key, upperlevel));

		if (!temp.isEmpty()) {
			return temp;

		} else {

			String[] date = key.getDate().split("/");

			int month = Integer.parseInt(date[0]);
			int day = Integer.parseInt(date[1]);
			int year = Integer.parseInt(date[2]);

			Calendar calendar = Calendar.getInstance();

			calendar.set(year, month - 1, day);
			calendar.add(Calendar.DAY_OF_MONTH, -3);

			Date begDate = calendar.getTime();

			calendar.set(year, month - 1, day);
			calendar.add(Calendar.DAY_OF_MONTH, 3);

			Date endDate = calendar.getTime();

			temp.addAll(getSuggestions(key, upperlevel, begDate, endDate, key));

			return temp;
		}
	}

	/**
	 * Gets the date of the FlightKey
	 */
	private Date getDate(FlightKey key) {
		String[] date = key.getDate().split("/");

		int month = Integer.parseInt(date[0]);
		int day = Integer.parseInt(date[1]);
		int year = Integer.parseInt(date[2]);

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);

		return calendar.getTime();
	}

	/**
	 * Returns a list of flights that have the same origin, destination, and
	 * date of the given key
	 */
	private ArrayList<FlightNode> getSameDay(FlightKey key, FlightNode start) {
		FlightNode curr = findCurrPos(key, start);

		if (start.getDown() == null) {

			ArrayList<FlightNode> p = new ArrayList<>();

			while (curr.getNext() != null) {
				if (curr.getKey() != null) {
					if (curr.getKey().getOrigin()
							.equalsIgnoreCase(key.getOrigin())
							&& curr.getKey().getDest()
									.equalsIgnoreCase(key.getDest())
							&& curr.getKey().getDate().compareTo(key.getDate()) == 0) {

						p.add(curr);
					}
				}
				curr = curr.getNext();
			}
			return p;
		}

		return getSameDay(key, start.getDown());
	}

	/**
	 * Returns a list of nodes that have the same origin and destination cities
	 * with departure times being within three days of the key
	 */
	private ArrayList<FlightNode> getSuggestions(FlightKey key,
			FlightNode start, Date begDate, Date endDate, FlightKey orig) {
		FlightNode curr = findCurrPos(key, start);

		if (start.getDown() == null) {
			FlightNode temp = curr;
			LinkedList<FlightNode> b = new LinkedList<>();

			// Gets the days three days before
			while (temp.getPrev() != null) {
				if (temp.getKey() != null) {
					if (temp.getKey().getOrigin()
							.equalsIgnoreCase(key.getOrigin())
							&& temp.getKey().getDest()
									.equalsIgnoreCase(key.getDest())
							&& (getDate(temp.getKey()).after(begDate))) {

						if (getDate(temp.getKey()).before(endDate)
								|| getDate(temp.getKey()).equals(endDate)) {

							b.addFirst(temp);
						}

					} else {
						break;
					}
				}
				temp = temp.getPrev();
			}
			// Convert to ArrayList
			List<FlightNode> pred = new ArrayList<FlightNode>(b);
			ArrayList<FlightNode> p = (ArrayList<FlightNode>) pred;

			// Gets the days three days after
			while (curr.getNext() != null) {
				if (curr.getKey() != null) {
					if (curr.getKey().getOrigin()
							.equalsIgnoreCase(key.getOrigin())
							&& curr.getKey().getDest()
									.equalsIgnoreCase(key.getDest())
							&& (getDate(temp.getKey()).before(endDate))) {

						if (getDate(temp.getKey()).after(begDate)
								|| getDate(temp.getKey()).equals(begDate)) {

							p.add(curr);
						}

					} else {
						break;
					}
				}
				curr = curr.getNext();
			}

			return p;
		}
		return getSuggestions(key, start.getDown(), begDate, endDate, orig);
	}

	/**
	 * Returns the FlightNode of the key
	 */
	private FlightNode getNode(FlightKey key, FlightNode start) {
		FlightNode curr = start;

		while (curr.getNext() != null) {
			if (curr.getKey() != null) {
				if (curr.getKey().compareTo(key) == 0) {
					return curr;
				}
			}
			curr = curr.getNext();
		}

		if (start.getDown() == null) {
			return null;
		}

		return getNode(key, start.getDown());
	}
}