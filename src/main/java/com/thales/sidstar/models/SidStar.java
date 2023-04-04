package com.thales.sidstar.models;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SidStar {
  private String airport;
  @JsonUnwrapped
  private List<TopWaypoint> topWaypoints;
}
