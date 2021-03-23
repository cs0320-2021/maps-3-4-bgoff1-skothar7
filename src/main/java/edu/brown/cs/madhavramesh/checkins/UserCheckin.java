package edu.brown.cs.madhavramesh.checkins;

/**
 * class for passing user checkin data.
 */
public class UserCheckin {

  private int id;
  private String name;
  private double ts;
  private double lat;
  private double lon;

  public UserCheckin(
      int userId,
      String username,
      double timestamp,
      double latitude,
      double longitude) {
    id = userId;
    name = username;
    ts = timestamp;
    lat = latitude;
    lon = longitude;
  }

  public int getId() {return id;}

  public String getName() {return name;}

  public double getTimestamp() {return ts;}

  public double getLat() {return lat;}

  public double getLon() {return lon;}
}
