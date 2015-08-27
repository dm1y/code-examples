/**
 * Class that creates WordFrequency objects, updates the frequency and position
 * of the WordFrequency object, and overrides Comparable's compareTo method for
 * sorting purposes.
 * 
 * @author Diana
 * 
 */
public class WordFrequency implements Comparable<WordFrequency> {

	private int frequency;
	private int initialPosition;
	private final String sourcepath;

	public WordFrequency(int frequency, int position, String sourcepath) {
		this.frequency = frequency;
		this.initialPosition = position;
		this.sourcepath = sourcepath;
	}

	/**
	 * Returns String sourcepath of WordFrequency object
	 * 
	 * @return this.sourcepath
	 */
	public String getSourcepath() {
		return this.sourcepath;
	}

	/**
	 * Returns the int frequency of WordFrequency object
	 * 
	 * @return this.frequency
	 */
	public int getFrequency() {
		return this.frequency;
	}

	/**
	 * Returns the int initial position of object WordFrequency
	 * 
	 * @return this.initialPosition
	 */
	public int getPosition() {
		return this.initialPosition;
	}

	/**
	 * Adds the old and new frequency of object WordFrequency and updates it
	 * 
	 * @param x
	 */
	public void addFrequency(int x) {
		this.frequency = this.frequency + x;
	}

	/**
	 * Updates the position of object WordFrequency only if the new position is
	 * less than the old one
	 * 
	 * @param x
	 */
	public void updatePosition(int x) {
		if (this.initialPosition > x) {
			this.initialPosition = x;
		}
	}

	/**
	 * Overrides toString method
	 */
	@Override
	public String toString() {
		return "\"" + this.sourcepath.replace("]", "").replace("[", "")
				+ "\", " + this.frequency + ", " + this.initialPosition;
	}

	/**
	 * Overrides compareTo method. Compares/sorts WordFrequency objects by
	 * frequency. If the frequency is the same, it will compare by initial
	 * position, followed by the path.
	 */
	@Override
	public int compareTo(WordFrequency other) {
		if (this.frequency != other.frequency) {
			return Integer.compare(other.frequency, this.frequency);
		} else {
			if (this.initialPosition != other.initialPosition) {
				return Integer.compare(this.initialPosition,
						other.initialPosition);
			} else {
				return String.CASE_INSENSITIVE_ORDER.compare(this.sourcepath,
						other.sourcepath);
			}
		}
	}

}
