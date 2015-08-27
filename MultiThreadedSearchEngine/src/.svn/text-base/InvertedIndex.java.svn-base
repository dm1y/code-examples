import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Class that creates an inverted index. First it organizes the information read
 * from the FileParser class and stores it into an inverted index. The inverted
 * index stores a mapping from word to the file(s) it was found, and the
 * position(s) in that file it was located.
 * 
 * It also searches for the given words provided by the SearchResults class and
 * returns the results to the Search Results class.
 * 
 * It also outputs this information onto a text file.
 * 
 * @author: Diana Ly
 */
public class InvertedIndex {

	private final TreeMap<String, TreeMap<String, ArrayList<Integer>>> wordNested;
	private final MultiReaderLock lock;

	// private final Logger log = LogManager.getLogger();

	public InvertedIndex() {
		wordNested = new TreeMap<>();
		lock = new MultiReaderLock();
	}

	/**
	 * Checks whether the String argument word exists in the wordNested TreeMap.
	 * If it doesn't exist, it puts the word into the TreeMap. The TreeMap
	 * inside wordNested (nestedMap) is then initialized. If the nestedMap does
	 * not contain the key given by the String argument sourcepath, it will put
	 * that argument as its key. The TreeSet position is then initialized. The
	 * TreeSet position then adds the current int argument count into position.
	 * 
	 * @param word
	 * @param sourcepath
	 * @param count
	 * 
	 */
	public void addWords(String word, String sourcepath, int count) {
		lock.lockWrite();

		if (!hasWord(word)) {
			wordNested.put(word, new TreeMap<String, ArrayList<Integer>>());
		}
		TreeMap<String, ArrayList<Integer>> nestedMap = wordNested.get(word);

		if (!nestedMap.containsKey(sourcepath)) {
			nestedMap.put(sourcepath, new ArrayList<Integer>());
		}
		ArrayList<Integer> position = nestedMap.get(sourcepath);

		position.add(count);

		lock.unlockWrite();

	}

	/**
	 * Searches for queries within the inverted index
	 * 
	 * @param wordlist
	 * @return list
	 */
	public ArrayList<WordFrequency> partialSearch(ArrayList<String> wordlist) {
		ArrayList<WordFrequency> list = new ArrayList<WordFrequency>();
		HashMap<String, WordFrequency> map = new HashMap<String, WordFrequency>();

		lock.lockRead();
		for (String word : wordlist) {

			for (String key : wordNested.tailMap(word).keySet()) {
				if (!key.startsWith(word)) {
					break;
				} else {
					for (String src : wordNested.get(key).keySet()) {
						if (!map.containsKey(src)) {
							int position = wordNested.get(key).get(src).get(0);
							String path = src;
							int frequency = wordNested.get(key).get(src).size();
							WordFrequency wordSearched = new WordFrequency(
									frequency, position, path);

							map.put(src, wordSearched);
						} else {
							map.get(src).updatePosition(
									wordNested.get(key).get(src).get(0));
							map.get(src).addFrequency(
									wordNested.get(key).get(src).size());
						}
					}
				}
			}
		}
		lock.unlockRead();

		list.addAll(map.values());
		Collections.sort(list);

		return list;
	}

	/**
	 * Writes the information of InvertedIndex argument index into a text file.
	 * The name of the output file is provided by the argument String
	 * output_name. If an I/O Exception is present, prints out a user friendly
	 * message.
	 * 
	 * @param output_name
	 * 
	 */
	public void writeFile(String output_name) {
		lock.lockRead();
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(
				Paths.get(output_name), Charset.forName("UTF-8")))) {

			for (String word : wordNested.keySet()) {
				writer.write(word);

				for (String src : wordNested.get(word).keySet()) {
					writer.println();
					writer.write("\""
							+ src
							+ "\", "
							+ wordNested.get(word).get(src).toString()
									.toString().replace("]", "")
									.replace("[", ""));
				}
				writer.println();
				writer.println();
			}
			writer.println();

		} catch (IOException e) {
			System.out.println("Unable to write file.");
			// log.debug("Unable to write file " + output_name);
		}
		lock.unlockRead();

	}

	/**
	 * Checks whether the provided word exists in the argument map.
	 * 
	 * @param word
	 *            to check for
	 * @return <code>true</code> if the word exists
	 */
	private boolean hasWord(String word) {
		return wordNested.containsKey(word);
	}

	/**
	 * Adds values of InvertedIndex other to the global TreeMap wordNested
	 * 
	 * @param other
	 */
	public void addAll(InvertedIndex other) {

		lock.lockWrite();

		for (String key : other.wordNested.keySet()) {
			if (!hasWord(key)) {
				wordNested.put(key, other.wordNested.get(key));

			} else if (hasWord(key)) {
				for (String src : other.wordNested.get(key).keySet()) {
					TreeMap<String, ArrayList<Integer>> nestedMap = wordNested
							.get(key);

					if (!nestedMap.containsKey(src)) {
						nestedMap.put(src, other.wordNested.get(key).get(src));

					} else if (nestedMap.containsKey(src)) {
						ArrayList<Integer> position = wordNested.get(key).get(
								src);
						position.addAll(other.wordNested.get(key).get(src));

					}
				}
			}
		}
		lock.unlockWrite();
	}

}