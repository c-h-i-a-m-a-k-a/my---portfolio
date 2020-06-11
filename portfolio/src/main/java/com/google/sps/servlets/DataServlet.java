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

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Entity;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Document;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    int maxComments = 10;

    try {
    String getMax = request.getParameter("max");
    maxComments = Integer.parseInt(getMax);
    } 
    catch (NumberFormatException e) {

    }

    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asList(FetchOptions.Builder.withLimit(maxComments))) {
      /**

      Need to find some way to define comment class and entity
      before comments list is initialized

      class Comment { 
          public String content = (String) entity.getProperty("content");
          public String commentScore = String.valueOf(entity.getProperty("score"));
          public String time = (String) entity.getProperty("timestamp");
      }
      **/

      //String comment = (String) entity.getProperty("content");
      //String commentWithScore = comment+"  "+String.valueOf(entity.getProperty("score"));
      
      //Comment commentClass = Comment();
      comments.add(entity.getProperty("commentClass"));

      //comments.add(commentWithScore);

    }

    String conversion = convertToJsonUsingGson(comments);
    response.setContentType("application/json");
    response.getWriter().println(conversion);

  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{

    // Get the input from the form.
    String input = request.getParameter("input-string");

    Document doc =
        Document.newBuilder().setContent(input).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("content", input);
    commentEntity.setProperty("timestamp", System.currentTimeMillis());
    commentEntity.setProperty("score",score);

    class Comment { 
          public String content = (String) commentEntity.getProperty("content");
          public String commentScore = String.valueOf(commentEntity.getProperty("score"));
          public String time = (String) commentEntity.getProperty("timestamp");
    }
    Comment commentClass = Comment();

    commentEntity.setProperty("commentClass",commentClass);

    datastore.put(commentEntity);

    // Redirect back to the HTML page.

    response.sendRedirect("/index.html");
  }

    private String convertToJsonUsingGson(List<String> messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }

 
}