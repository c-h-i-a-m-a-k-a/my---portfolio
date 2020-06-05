// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    ArrayList<String> messages = new ArrayList<String>();
    messages.add("Can you see me?");
    messages.add("Does this text appear?");
    messages.add("Are all the messages being displayed?");
    
    String json = convertToJsonUsingGson(messages);

    response.setContentType("text/html;");
    response.getWriter().println(json);
    response.getWriter().println("Hello Chiamaka");

  }
  
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException 
  
  
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // Get the input from the form.
    String input = getContent(req);

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("content", input);

    datastore.put(commentEntity);

    Query query = new Query("Comment");
    PreparedQuery results = datastore.prepare(query);

    ArrayList<String> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String content = (String) entity.getProperty("content");
      String comment = new String(content);
      System.out.println(comment);
      comments.add(comment);
    }

    // Redirect back to the HTML page.
    //Commenting out temporarrily to test if comments will load
    //res.sendRedirect("/index.html");
    Gson gson = new Gson();

    res.setContentType("text/html;");
    res.getWriter().println(gson.toJson(comments));
  }



    private String convertToJsonUsingGson(ArrayList<String> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }

 
}

