
package com.google.sps.servlets;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/comments")
public class CommentServlet extends HttpServlet {

    private CommentSection section = new CommentSection();

    @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    String json = new Gson().toJson(section);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get the input from the form.
    int input = getContent(request);

    section.takePlayerTurn(content);

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }

  /** Returns the choice entered by the player, or -1 if the choice was invalid. */
  private String getContent(HttpServletRequest request) {
    // Get the input from the form.
    String contentString = request.getParameter("input-string");

    return contentString;
  }
}
   