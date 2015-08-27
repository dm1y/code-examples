import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

@SuppressWarnings("serial")
public class SearchServlet extends BaseServlet {

	private final InvertedIndex index;
	private Map<String, String> cookies;

	public SearchServlet(InvertedIndex index) {
		this.index = index;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		prepareResponse("Search Engine", response);

		PrintWriter out = response.getWriter();
		String error = request.getParameter("error");

		if (error != null) {
			String errorMessage = StringUtilities.getStatus(error).message();
			out.println("<p style=\"color: red;\">" + errorMessage + "</p>");
		}

		printForm(out);
		finishResponse(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		prepareResponse("Search Engine", response);

		PrintWriter out = response.getWriter();

		String line = request.getParameter("search");
		line = StringEscapeUtils.escapeHtml4(line);

		ArrayList<String> wordlist = new ArrayList<String>();

		if (line != null && !line.isEmpty()) {

			String[] words = line.split("\\s");

			for (String word : words) {
				word = MultithreadedIndexBuilder.normalizeWord(word);

				if (MultithreadedIndexBuilder.isWord(word)) {
					wordlist.add(word);
				}
			}

			// Status status = db.addHistory(wordlist.toString().replace("[",
			// "")
			// .replace("]", "").replace(",", ""));
			//
			// if (status == Status.OK) {
			// response.addCookie(new Cookie("history", wordlist.toString()
			// .replace("[", "").replace("]", "").replace(",", "")));
			// }

			ArrayList<WordFrequency> result = this.index
					.partialSearch(wordlist);

			out.printf("%n%n<h1>Search Results</h1>%n%n");

			out.printf("<p><b>%d search results for %s</b></p>", result.size(),
					wordlist.toString().replace("[", "").replace("]", "")
							.replace(",", ""));

			for (WordFrequency w : result) {
				out.printf("%s<br><br>", w.getSourcepath());
				out.println();
			}

		} else {

			out.printf("<br><br><font color = red>Please enter a query.</font>");

			printForm(out);

		}

		finishResponse(request, response);

	}

	private void printForm(PrintWriter out) {
		out.println("<form action=\"/search\" method=\"post\">");
		out.println("<table border=\"0\">");
		out.println("\t<tr>");
		out.println("\t\t<td>Search:</td>");
		out.println("\t\t<td><input type=\"text\" name=\"search\" size=\"30\"></td>");
		out.println("\t</tr>");
		out.println("</table>");
		out.println("<p><input type=\"submit\" value=\"Search\"></p>");
		out.println("</form>");
	}
}
