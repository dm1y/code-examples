public class FlightKey implements Comparable<FlightKey> {

	private String origin;
	private String destination;
	private String date;
	private String time;

	public FlightKey() {
		origin = null;
		destination = null;
		date = null;
		time = null;
	}

	@Override
	public String toString() {
		return origin + " " + destination + " " + date + " " + time;
	}

	public FlightKey(String origin, String destination, String date, String time) {
		this.origin = origin;
		this.destination = destination;
		this.date = date;
		this.time = time;
	}

	public String getOrigin() {
		return this.origin;
	}

	public String getDest() {
		return this.destination;
	}

	public String getDate() {
		return this.date;
	}

	public String getTime() {
		return this.time;
	}

	public String getHour() {
		return this.time.substring(0, 2);
	}

	@Override
	public int compareTo(FlightKey other) {
		if (origin.compareToIgnoreCase(other.origin) == 0) {
			if (destination.compareToIgnoreCase(other.destination) == 0) {
				if (date.compareToIgnoreCase(other.date) == 0) {
					return time.compareToIgnoreCase(other.time);
				} else {
					return date.compareToIgnoreCase(other.date);
				}
			} else {
				return destination.compareTo(other.destination);
			}
		}
		return origin.compareTo(other.origin);
	}

}
