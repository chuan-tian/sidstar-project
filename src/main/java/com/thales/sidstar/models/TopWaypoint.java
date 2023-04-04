package com.thales.sidstar.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopWaypoint {
  private String name;
  private int count;
}
