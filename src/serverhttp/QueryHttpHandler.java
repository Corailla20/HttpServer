package serverhttp;

/**
 * Created by Corailla20 on 03/03/2016.
 */
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Oracle queries HTTP handler for the proxy
 */
public class QueryHttpHandler implements HttpHandler {

    private DatabaseConnection db;

    public QueryHttpHandler(DatabaseConnection db) {
        this.db = db;
    }

    public void handle(HttpExchange t) throws IOException {
        String response;

        try {

            // Accept only POST requests
            if (t.getRequestMethod().equals("POST")) {

                // Token for a bit of security
                String token = t.getRequestHeaders().getFirst("X-Auth-Token");

                if (token != null && token.equals(Main.TOKEN)) {

                    // Retrieve SQL from the request body
                    BufferedReader input = new BufferedReader(new InputStreamReader(t.getRequestBody()));
                    String sql = input.readLine();

                    System.out.println("Valid request received: " + sql);

                    // Execute the query (return a custom object able to generate JSON from the result)
                    DatabaseSet resultSet = this.db.query(sql);                
                    response = resultSet.generateJSON();
                    
                    System.out.println(response);

                    t.getResponseHeaders().add("Content-type", "application/json");
                    t.sendResponseHeaders(200, response.getBytes("UTF-8").length);
                    resultSet.closeResultSet();
                    this.db.resultSet.close();
                    
                } else {
                    response = "Invalid authentication token";
                    t.sendResponseHeaders(403, response.length());

                    System.out.println("Invalid request received: " + response);
                }

            } else {
                response = "Method not allowed";
                t.sendResponseHeaders(405, response.length());

                System.out.println("Invalid request received: " + response);
            }

            // Catch SQL exception for better display of error
        } 
        catch (SQLException e) {
            response = "An SQL exception occured : ";
            response += e.getMessage() + ", " + e.getSQLState();
            response += " in " + e.getClass() + "\n";

            for (StackTraceElement line : e.getStackTrace()) {
                response += line.toString() + "\n";
            }

            t.sendResponseHeaders(500, response.length());

            System.out.println("Invalid request received:\n" + response);


            // Catch all errors to avoid global server failure
        } catch (Exception e) {
            response = "An error occured : ";
            response += e.getMessage();
            response += " in " + e.getClass() + "\n";

            for (StackTraceElement line : e.getStackTrace()) {
                response += line.toString() + "\n";
            }

            t.sendResponseHeaders(500, response.length());

            System.out.println("Invalid request received:\n" + response);
        }

        OutputStream os = t.getResponseBody();
        os.write(response.getBytes("UTF-8"));
        os.close();
    }

}