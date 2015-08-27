import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * A class designed to make fetching the results of different HTTP operations
 * easier.
 * 
 * @author Diana Ly
 * 
 */
public class HTTPFetcher {
	/** Port used by socket. For web servers, should be port 80. */
	private static final int PORT = 80;

	/** Used to determine if headers have been read. */
	private boolean head;

	/** The URL to fetch from a web server. */
	private final URL url;

	/** Used to retrieve header */
	private final StringBuilder header;

	/** Used to retrieve result */
	private final StringBuilder html;

	/**
	 * Initializes this fetcher. Must call {@link #fetch()} to actually start
	 * the process.
	 * 
	 * @param url
	 *            - the link to fetch from the webserver
	 * @throws MalformedURLException
	 *             if unable to parse URL
	 */
	public HTTPFetcher(String url) throws MalformedURLException {
		this.url = new URL(url);
		this.header = new StringBuilder();
		this.head = true;
		this.html = new StringBuilder();
	}

	/**
	 * Returns the port being used to fetch URLs.
	 * 
	 * @return port number
	 */
	public int getPort() {
		return PORT;
	}

	/**
	 * Returns the URL being used by this fetcher.
	 * 
	 * @return URL
	 */
	public URL getURL() {
		return url;
	}

	/**
	 * Returns header
	 * 
	 * @return header
	 */
	public String getHeader() {
		return header.toString();
	}

	/**
	 * Returns html
	 * 
	 * @return
	 */
	public String getHTML() {
		return html.toString();
	}

	/**
	 * Crafts the HTTP request from the URL. Must be overridden.
	 * 
	 * @return HTTP request
	 */
	private String craftRequest() {
		String host = this.getURL().getHost();
		String resource = this.getURL().getFile().isEmpty() ? "/" : this
				.getURL().getFile();

		StringBuffer output = new StringBuffer();
		output.append("GET " + resource + " HTTP/1.1\n");
		output.append("Host: " + host + "\n");
		output.append("Connection: close\n");
		output.append("\r\n");

		return output.toString();
	}

	/**
	 * Handles each line fetched from the web server. May be overridden.
	 * 
	 * @param line
	 *            - text retrieved from web server
	 */
	private String processLine(String line) {
		if (head) {
			// Check if we hit the blank line separating headers and HTML
			if (line.trim().isEmpty()) {
				head = false;
			}
		} else {
			return line + "\n";
		}
		return "";
	}

	/**
	 * Will connect to the web server and fetch the URL using the HTTP request
	 * from {@link #craftRequest()}, and then call {@link #processLine(String)}
	 * on each of the returned lines.
	 */
	public void fetch() {
		try (Socket socket = new Socket(url.getHost(), PORT);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream());) {
			String request = craftRequest();

			writer.println(request);
			writer.flush();

			String line = reader.readLine();

			while (line != null) {
				if (line.isEmpty()) {
					break;
				} else {
					header.append(line + " ");
					line = reader.readLine();
				}
			}

			while (line != null) {
				html.append(processLine(line) + " ");
				line = reader.readLine();
			}

		} catch (Exception ex) {
			System.out.println("Unable to fetch server.");
		}
	}
}