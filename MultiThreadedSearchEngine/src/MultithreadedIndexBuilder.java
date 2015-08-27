import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class that traverses through directories and crawls through webpages. This
 * is implemented using mulithreading.
 * 
 * @author Diana Ly
 * 
 */
public class MultithreadedIndexBuilder {

	private static final Logger logger = LogManager.getLogger();

	private final String URL = "(([A-Za-z][A-Za-z0-9+.-]://))([^/#?]+)$";
	private final String REGEX = "(?i)a\\s*?href\\s*?=\\s*?\"\\s*?((" + URL
			+ ")|(.*?))\\s*?\"\\s*?";
	private final WorkQueue workers;
	private final TreeSet<Path> paths;
	private final MultiReaderLock lock;
	private int pending;
	private final HashSet<URI> masterLinks;

	public MultithreadedIndexBuilder(WorkQueue worker) {
		workers = worker;
		paths = new TreeSet<Path>();
		lock = new MultiReaderLock();
		pending = 0;
		masterLinks = new HashSet<>();

	}

	/**
	 * Increments pending
	 */
	private synchronized void incrementPending() {
		pending++;
		logger.debug("Pending is now {}", pending);
	}

	/**
	 * Decrements pending
	 */
	private synchronized void decrementPending() {
		pending--;
		logger.debug("Pending is now {}", pending);

		if (pending <= 0) {
			this.notifyAll();
		}
	}

	/**
	 * Has threads wait
	 */
	public synchronized void finish() {
		try {
			while (pending > 0) {
				logger.debug("Waiting until finished");
				this.wait();
			}
		} catch (InterruptedException e) {
			logger.debug("Finish interrupted", e);
		}
	}

	/**
	 * Resets
	 */
	public void reset() {
		finish();
		lock.lockWrite();

		paths.clear();

		lock.unlockWrite();
		logger.debug("Counters reset");
	}

	/**
	 * Shuts down
	 */
	public void shutdown() {
		finish();
		workers.shutdown();
	}

	/**
	 * Makes the provided non-null and non-empty String case-insensitive and
	 * replaces any special characters with the empty string.
	 * 
	 * @param word
	 *            to create a case-insensitive and non special character
	 *            containing
	 * @return <code>word</code> if the text is non-null and not empty
	 * 
	 */
	public static String normalizeWord(String word) {
		if (word != null && !word.isEmpty()) {
			return word.replaceAll("[\\W_]", "").toLowerCase().trim();
		} else {
			return null;
		}
	}

	/**
	 * Tests whether the provided String is a word, i.e. whether the String is
	 * non-null and not empty
	 * 
	 * @param text
	 *            to test
	 * @return <code>true</code> if the text is non-null and not empty
	 * 
	 */
	public static boolean isWord(String text) {
		return (text != null && !text.trim().isEmpty());
	}

	/**
	 * Builds inverted index using a webcrawler
	 * 
	 * @param seedUrl
	 * @param index
	 */
	public void buildIndex(String seedUrl, InvertedIndex index) {
		try {
			URL seedURL = new URL(seedUrl);

			lock.lockWrite();
			masterLinks.add(seedURL.toURI());
			lock.unlockWrite();

			workers.execute(new HTMLCrawlerWorker(seedURL.toURI(), index));

		} catch (MalformedURLException | URISyntaxException e) {
			System.out.println("Unable to fetch URL.");
		}

		finish();
	}

	/**
	 * Worker class that parses html pages
	 * 
	 * @author Diana
	 * 
	 */
	private class HTMLCrawlerWorker implements Runnable {

		private final URI url;
		private final InvertedIndex index;

		public HTMLCrawlerWorker(URI url, InvertedIndex index) {
			logger.debug("Worker created for {}", url.toString());
			this.url = url;
			this.index = index;

			incrementPending();
		}

		@Override
		public void run() {

			try {
				HTTPFetcher fetch = new HTTPFetcher(url.toString());

				fetch.fetch();

				String html = fetch.getHTML();

				String header = fetch.getHeader();

				int GROUP = 1;

				Pattern p = Pattern.compile(REGEX);

				Matcher m = p.matcher(html);

				URL absolute = null;

				lock.lockWrite();
				while (m.find() && masterLinks.size() < 50) {
					absolute = new URL(url.toURL(), m.group(GROUP).replaceAll(
							"#.*", ""));

					if (header.contains("text/html")
							&& !masterLinks.contains(absolute.toURI())) {
						masterLinks.add(absolute.toURI());
						workers.execute(new HTMLCrawlerWorker(absolute.toURI(),
								this.index));
					}

				}
				lock.unlockWrite();

				parseHTML(this.index, html, this.url);

			} catch (MalformedURLException | URISyntaxException e) {
				System.out.println("Unable to fetch URL.");
			}
			decrementPending();
		}
	}

	/**
	 * Parses out the HTML, cleans it, and sends it to the InvertedIndex class.
	 * 
	 * @param index
	 * @param html
	 * @param url
	 */
	private void parseHTML(InvertedIndex index, String html, URI url) {
		int counter = 0;

		InvertedIndex local = new InvertedIndex();

		ArrayList<String> words = HTMLCleaner.fetchWords(html);

		for (int i = 0; i < words.size(); i++) {
			counter++;
			local.addWords(words.get(i), url.toString(), counter);
		}

		index.addAll(local);
	}
}