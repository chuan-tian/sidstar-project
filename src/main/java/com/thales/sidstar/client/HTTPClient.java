package com.thales.sidstar.client;

import com.thales.sidstar.utils.Constants;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPClient {
  public static String getAll(String endpoint) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(Constants.AIRAC_URL + endpoint))
        .header("api-key", Constants.API_KEY)
        .GET()
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    return response.body();
  }
}
