package edu.brown.cs.madhavramesh.graph;

/**
 * Abstract class that handles calculations for maps functionality.
 */
public abstract class MapCalculations {
  private static final double EARTH_RADIUS = 6371;

  /**
   * Method to calculate the haversine distance given two pairs of coordinates.
   * @param lat1 First latitude point
   * @param lon1 First longitude point
   * @param lat2 Second latitude point
   * @param lon2 Second longitude point
   * @return double representing the haversine distance between the given points.
   */
  public static double haversine(double lat1, double lon1,
                                 double lat2, double lon2) {
    // distance between latitudes and longitudes
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    // convert to radians
    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);

    // apply formulae
    double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2)
        * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return Math.abs(EARTH_RADIUS * c);
  }
}