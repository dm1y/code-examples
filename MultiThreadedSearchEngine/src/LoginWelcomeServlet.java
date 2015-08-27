import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginWelcomeServlet extends BaseServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String user = getUsername(request);

		if (user != null) {
			prepareResponse("The Searcher", response);

			PrintWriter out = response.getWriter();
			out.println("<p>Hello " + user + "! Welcome to The Searcher!</p>");
			out.println("<p><a href=\"/login?logout\">(logout)</a></p>");

			finishResponse(request, response);
		} else {
			response.sendRedirect("/login");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doGet(request, response);
	}
}