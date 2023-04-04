package com.thales.sidstar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.thales.sidstar.client.HTTPClient;
import com.thales.sidstar.models.*;
import com.thales.sidstar.utils.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Service {
  private static final Logger LOG = LoggerFactory.getLogger(Service.class);

  public List<String> getAllAirports() throws IOException, InterruptedException {
    LOG.info("Getting list of airport");

    String body = HTTPClient.getAll(Constants.AIRPORT_ENDPOINT);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode airportsNode = mapper.readTree(body);
    List<String> icaoCodes = new ArrayList<>();
    Iterator<JsonNode> airportsIterator = airportsNode.elements();

    while (airportsIterator.hasNext()) {
      JsonNode airportNode = airportsIterator.next();
      String icaoCode = airportNode.get("icao").asText();
      icaoCodes.add(icaoCode);
    }

    return icaoCodes;
  }

  public SidStar getTopTwoWaypointsWithSIDS(String airport)
      throws IOException, InterruptedException {
    LOG.info("Getting SIDS for " + airport);

    return getTopTwoWaypoints(Constants.SIDS_ENDPOINT + airport, airport);
  }

  public SidStar getTopTwoWaypointsWithSTARS(String airport)
      throws IOException, InterruptedException {
    LOG.info("Getting STARS for " + airport);

    return getTopTwoWaypoints(Constants.STARS_ENDPOINT + airport, airport);
  }

  private SidStar getTopTwoWaypoints(String endpoint, String airport)
      throws IOException, InterruptedException {
    Map<String, Integer> waypointsMapCount = new HashMap<>();
    String body = HTTPClient.getAll(endpoint);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode SIDSNode = mapper.readTree(body);
    Iterator<JsonNode> SIDSIterator = SIDSNode.elements();

    while (SIDSIterator.hasNext()) {
      JsonNode SIDNode = SIDSIterator.next();
      Iterator<JsonNode> waypointsIterator = SIDNode.get("waypoints").elements();

      while (waypointsIterator.hasNext()) {
        JsonNode waypoint = waypointsIterator.next();
        String waypointName = waypoint.get("name").asText();

        // If waypointName do not exists, put 1 as value
        // Otherwise add 1 to the value linked to waypointName
        waypointsMapCount.merge(waypointName, 1, Integer::sum);
      }
    }

    LOG.debug("Waypoints count: " + waypointsMapCount);

    List<TopWaypoint> topWaypoints = getTopTwoValuesInHashMap(waypointsMapCount);
    SidStar waypoints = SidStar.builder().airport(airport).topWaypoints(topWaypoints).build();

    return waypoints;
  }

  private List<TopWaypoint> getTopTwoValuesInHashMap(Map<String, Integer> waypointsMapCount) {
    TopWaypoint top1Waypoint = TopWaypoint.builder().count(0).build();
    TopWaypoint top2Waypoint = TopWaypoint.builder().count(0).build();

    for (Map.Entry<String, Integer> set : waypointsMapCount.entrySet()) {
      if (set.getValue().compareTo(top1Waypoint.getCount()) > 0) {
        top2Waypoint = top1Waypoint;
        top1Waypoint = TopWaypoint.builder().name(set.getKey()).count(set.getValue()).build();
      } else if (set.getValue().compareTo(top2Waypoint.getCount()) > 0 && set.getKey() != top1Waypoint.getName()) {
        top2Waypoint = TopWaypoint.builder().name(set.getKey()).count(set.getValue()).build();
      }
    }

    List<TopWaypoint> topWaypoint = new ArrayList<>();
    topWaypoint.add(top1Waypoint);
    topWaypoint.add(top2Waypoint);

    return topWaypoint;
  }
}
