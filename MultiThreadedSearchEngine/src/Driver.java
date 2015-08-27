import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * 
 * If no arguments are given, the program will output an error statement.
 * 
 * @author: Diana Ly
 */
public class Driver {

	private static final Logger log = LogManager.getLogger();
	private static int PORT = 8080;

	/**
	 * 
	 * @param args
	 * 
	 */
	public static void main(String[] args) {

		ArgumentParser arguments = new ArgumentParser(args);
		InvertedIndex index = new InvertedIndex();

		if (args.length == 0) {
			System.out.println("Error: No arguments given.");

		} else {

			int thread = 5;

			if (arguments.hasFlag("-t") && arguments.hasValidInteger("-t")) {
				thread = arguments.getInteger("-t");
			}

			WorkQueue workers = new WorkQueue(thread);
			MultithreadedIndexBuilder traverse = new MultithreadedIndexBuilder(
					workers);

			if (arguments.hasFlag("-u") && arguments.hasURL("-u")) {
				traverse.buildIndex(arguments.getValue("-u"), index);
			}

			if (arguments.hasFlag("-p") && arguments.hasPort("-p")) {
				PORT = Integer.parseInt(arguments.getValue("-p"));
			}

			if (arguments.hasFlag("-i")) {
				String filename = "invertedindex.txt";
				if (arguments.getValue("-i") != null) {
					filename = arguments.getValue("-i");
				}
				index.writeFile(filename);
			}
			traverse.shutdown();
		}

		Server server = new Server(PORT);

		ServletHandler handler = new ServletHandler();

		server.setHandler(handler);

		SearchServlet search = new SearchServlet(index);

		handler.addServletWithMapping(new ServletHolder(search), "/search");

		handler.addServletWithMapping(LoginUserServlet.class, "/login");
		handler.addServletWithMapping(LoginRegisterServlet.class, "/register");
		handler.addServletWithMapping(LoginWelcomeServlet.class, "/welcome");
		handler.addServletWithMapping(LoginRedirectServlet.class, "/*");
		handler.addServletWithMapping(SearchHistoryServlet.class, "/history");
		handler.addServletWithMapping(ChangePWServlet.class, "/pw");

		log.info("Starting server on port " + PORT + "...");

		try {
			server.start();
			server.join();

			log.info("Exiting...");
		} catch (Exception ex) {
			log.fatal("Interrupted while running server.", ex);
			System.exit(-1);
		}

	}
}