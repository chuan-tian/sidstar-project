package com.thales.sidstar;

import java.io.IOException;
import com.thales.sidstar.server.HTTPServer;
import com.thales.sidstar.service.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

/**
 * App
 *
 * @throws IOException
 * @throws InterruptedException
 */
public class App {
    private static HTTPServer httpServer;
    private static Service service;
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        // Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();
        LOG.info("Starting...");

        // Get all airports
        service = new Service();
        List<String> airports = service.getAllAirports();
        LOG.info("All available airports: " + airports);

        // Start HTTP
        httpServer = new HTTPServer();
        httpServer.start(service, airports);
    }
}
