/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverhttp;

import java.io.IOException;
import java.sql.SQLException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import oracle.jdbc.driver.OracleDriver;
import com.sun.net.httpserver.HttpServer;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author Corailla20
 */
public class Main {

    
    public static final String TOKEN = "************************************";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException, java.sql.SQLException, ClassNotFoundException {

         System.out.println("Connecting to Oracle ...");

         Class.forName("oracle.jdbc.driver.OracleDriver");
        /*
         * Create the database connection (don't catch the SQLException to warn user on startup if there is an error)
         */
        DatabaseConnection database = new DatabaseConnection("***.***.***.**", "1521", "*", "***", "***");

        System.out.println("Creating HTTP server ...");

        /*
         * Create the HTTP server (don't catch the IOException to warn user on startup if there is an error)
         */
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // The handler itself will catch SQL errors to avoid a server stop is the SQL is wrong
        server.createContext("/query", new QueryHttpHandler(database));

        System.out.println("Starting HTTP server ...");

        // Start the server
        server.setExecutor(null);
        server.start();

        System.out.println("HTTP server listening ...");
     }
}
