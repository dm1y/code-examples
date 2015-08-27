import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SearchHistoryServlet extends BaseServlet {

	private Map<String, String> cookies;

	public SearchHistoryServlet() {
		super();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		prepareResponse("Search History", response);

		PrintWriter out = response.getWriter();
		String error = request.getParameter("error");

		if (error != null) {
			String errorMessage = StringUtilities.getStatus(error).message();
			out.println("<p style=\"color: red;\">" + errorMessage + "</p>");
		}

		cookies = getCookieMap(request);

		if (cookies.get("history").isEmpty() || cookies.get("history") == null) {
			String noCookies = "No cookies saved.";
			out.printf("%s", noCookies);
		} else if ((cookies.size() > 0)) {

			synchronized (cookies) {
				for (String cookie : cookies.keySet()) {
					out.printf("<p><b>%s:</b> %s </p>%n%n", cookie,
							cookies.get("history"));
				}
			}
		}

		printForm(out);
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
