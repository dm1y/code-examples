import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CrawlTester.LocalTester.class })
public class CrawlTester {

	private static final String NUM_THREADS = "5";

	/**
	 * Tests the output of {@link Driver}. If the path arguments are provided,
	 * will make sure the actual output matches the expected output. If the test
	 * fails, will include the name of the test in the assertion output.
	 * 
	 * @param testName
	 *            - name of the test being run
	 * @param args
	 *            - arguments to pass to {@link Driver}
	 * @param actual
	 *            - path to actual output
	 * @param expected
	 *            - path to expected output
	 */
	private static void testProject(String testName, String[] args,
			Path actual, Path expected) {
		try {
			if (actual != null) {
				Files.deleteIfExists(actual);
			}

			Driver.main(args);
			Assert.assertTrue(testName, BaseTester.testFiles(actual, expected));
		} catch (Exception e) {
			Assert.fail(String.format("Test Case: %s%nException: %s", testName,
					e.toString()));
		}
	}

	/**
	 * Tests the index result output.
	 */
	private static void testThreadIndexOutput(String test, String link) {
		String base = BaseTester.getBaseDirectory();
		String name = String.format("invertedindex-%s.txt", test);

		Path actual = Paths.get(name);
		Path expected = Paths.get(base, "output", name);

		String[] args = new String[] { "-u", link, "-i", name, "-t",
				NUM_THREADS };

		testProject(name, args, actual, expected);
	}

	/**
	 * Tests the search result output.
	 */
	private static void testThreadSearchOutput(String test, String link) {
		String base = BaseTester.getBaseDirectory();
		String name = String.format("searchresults-%s.txt", test);

		Path query = Paths.get(base, "input", "search", "queries-multi.txt");
		Path actual = Paths.get(name);
		Path expected = Paths.get(base, "output", name);

		String[] args = new String[] { "-u", link, "-q",
				query.toAbsolutePath().toString(), "-r", name, "-t",
				NUM_THREADS };

		testProject(name, args, actual, expected);
	}

	/**
	 * This class tests whether {@link Driver} produces the correct inverted
	 * index and search result output for different LOCAL seed urls. Test these
	 * BEFORE running the external seeds to avoid spamming those web servers.
	 * 
	 * You can right-click this class to run just these set of tests!
	 */
	public static class LocalTester {

		@Test
		public void testIndexBirds() {
			String test = "birds";
			String link = "http://www.cs.usfca.edu/~sjengle/cs212/crawl/birds.html";

			testThreadIndexOutput(test, link);
		}

		@Test
		public void testSearchBirds() {
			String test = "birds";
			String link = "http://www.cs.usfca.edu/~sjengle/cs212/crawl/birds.html";

			testThreadSearchOutput(test, link);
		}
	}

}
