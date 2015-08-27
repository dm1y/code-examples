import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangePWServlet extends BaseServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		prepareResponse("Change Password", response);

		PrintWriter out = response.getWriter();

		String error = request.getParameter("error");

		int code = 0;

		if (error != null) {
			try {
				code = Integer.parseInt(error);
			} catch (Exception ex) {
				code = -1;
			}

			String errorMessage = StringUtilities.getStatus(code).message();
			out.println("<p style=\"color: red;\">" + errorMessage + "</p>");
		}

		if (request.getParameter("newpass") != null) {
			out.println("<p>Password change was successful!");
			out.println("Login with your username and new password below.</p>");
		}

		if (request.getParameter("logout") != null) {
			clearCookies(request, response);
			out.println("<p>Successfully logged out.</p>");
		}

		printForm(out);
		finishResponse(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");
		String newpass = request.getParameter("newpass");

		Status status = db.changePassword(user, pass, newpass);

		try {
			if (status == Status.OK) {
				clearCookies(request, response);
				response.addCookie(new Cookie("login", "true"));
				response.addCookie(new Cookie("name", user));
				response.sendRedirect(response.encodeRedirectURL("/welcome"));
			} else {
				response.addCookie(new Cookie("login", "false"));
				response.addCookie(new Cookie("name", ""));
				response.sendRedirect(response
						.encodeRedirectURL("/login?error=" + status.ordinal()));
			}
		} catch (Exception ex) {
			log.error("Unable to change password.", ex);
		}

		finishResponse(request, response);
	}

	private void printForm(PrintWriter out) {
		out.println("<form action=\"/login\" method=\"post\">");
		out.println("<table border=\"0\">");
		out.println("\t<tr>");
		out.println("\t\t<td>Usename:</td>");
		out.println("\t\t<td><input type=\"text\" name=\"user\" size=\"30\"></td>");
		out.println("\t</tr>");
		out.println("\t<tr>");
		out.println("\t\t<td>Old Password:</td>");
		out.println("\t\t<td><input type=\"password\" name=\"pass\" size=\"30\"></td>");
		out.println("</tr>");
		out.println("\t<tr>");
		out.println("\t\t<td>New Password:</td>");
		out.println("\t\t<td><input type=\"password\" name=\"newpass\" size=\"30\"></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<p><input type=\"submit\" value=\"Submit\"></p>");
		out.println("</form>");

		out.println("<p>(<a href=\"/register\">new user? register here.</a>)</p>");
	}
}
