package com.thales.sidstar.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thales.sidstar.service.Service;
import com.thales.sidstar.utils.Constants;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPServer {
  private static final Logger LOG = LoggerFactory.getLogger(HTTPServer.class);
  private static HttpServer server;
  private static Service service;

  /**
   * 
   * @throws IOException
   */
  public void start(Service serviceInst, List<String> airports) throws IOException {
    LOG.info("Starting HTTP server...");
    service = serviceInst;

    server = HttpServer.create(new InetSocketAddress(8080), 0);

    createMappings(airports);

    server.setExecutor(null);
    server.start();
  }

  /**
   * 
   */
  public void stop() {
    LOG.info("Stopping HTTP server...");

    server.stop(1);
  }

  private void createMappings(List<String> airports) {
    for (String airport : airports) {
      server.createContext("/sids/" + airport, exchange -> {
        if ("GET".equals(exchange.getRequestMethod())) {
          try {
            sendResponse(exchange, service.getTopTwoWaypointsWithSIDS(airport));
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } else {
          exchange.sendResponseHeaders(Constants.HTTP_METHOD_NOT_ALLOWED, -1);
        }
      });

      server.createContext("/stars/" + airport, exchange -> {
        if ("GET".equals(exchange.getRequestMethod())) {
          try {
            sendResponse(exchange, service.getTopTwoWaypointsWithSTARS(airport));
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } else {
          exchange.sendResponseHeaders(Constants.HTTP_METHOD_NOT_ALLOWED, -1);
        }
      });
    }
  }

  private <T> void sendResponse(HttpExchange exchange, T response) throws IOException {
    String responseText;
    int responseCode = Constants.HTTP_OK;

    if (response != null) {
      ObjectMapper mapper = new ObjectMapper();
      String jsonBody = mapper.writeValueAsString(response);
      responseText = jsonBody;
    } else {
      responseText = "Bad request\n";
      responseCode = Constants.HTTP_BAD_REQUEST;
    }

    // Send response
    LOG.info("Sending response");
    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    exchange.sendResponseHeaders(responseCode, responseText.getBytes().length);
    OutputStream output = exchange.getResponseBody();
    output.write(responseText.getBytes());
    output.flush();
  }
}
