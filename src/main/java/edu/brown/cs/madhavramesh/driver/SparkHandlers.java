package edu.brown.cs.madhavramesh.driver;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.madhavramesh.checkins.UserCheckin;
import edu.brown.cs.madhavramesh.maps.MapNode;
import edu.brown.cs.madhavramesh.maps.RouteTriggerAction;
import edu.brown.cs.madhavramesh.stars.TriggerActionExecutor;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateViewRoute;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SparkHandlers {

  /**
   * Handle requests to the front page of our Stars website.
   */
  protected static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of(
          "title", "Stars: Query the database");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  protected static class InputHandler implements TemplateViewRoute {
    private static String command;

    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      List<String> possibleButtonClicked = Arrays.asList(
          qm.value("naive-neighbors"),
          qm.value("neighbors"),
          qm.value("naive-radius"),
          qm.value("radius"));

      for (String button : possibleButtonClicked) {
        if (button != null) {
          command = button.toLowerCase().replace(" ", "_");
        }
      }

      Map<String, Object> variables = ImmutableMap.of(
          "title", "Stars: Query the database");
      return new ModelAndView(variables, "query.ftl");
    }

    public static String getCommand() {
      return command;
    }
  }

  protected static class SubmitHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) throws Exception {
      QueryParamsMap qm = req.queryMap();
      List<String> args = new ArrayList<>(Arrays.asList(
          qm.value("neighbors-or-radius"),
          qm.value("star-name"),
          qm.value("x"),
          qm.value("y"),
          qm.value("z")));
      args.removeIf(arg -> arg == null);
      String[] argsArray = args.toArray(new String[args.size()]);

      TriggerActionExecutor getResults = new TriggerActionExecutor(Main.getACTIONS());
      String results =
          getResults.executeTriggerAction(InputHandler.getCommand(), argsArray, false);
      results = results.replace("\n", "<br/>");

      Map<String, String> variables = ImmutableMap.of(
          "title", "Stars: Results from the database",
          "starResults", results);

      return new ModelAndView(variables, "results.ftl");
    }
  }

  /**
   * Handles requests made for a route.
   */

  protected static class RouteHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());

      String sLat = data.getString("srclat");
      String sLon = data.getString("srclong");
      String dLat = data.getString("destlat");
      String dLon = data.getString("destlong");

      TriggerActionExecutor getResults = new TriggerActionExecutor(Main.getACTIONS());

      boolean firstPairDoubles = false;
      try {
        Double.parseDouble(sLat);
        Double.parseDouble(sLon);
        firstPairDoubles = true;
        Double.parseDouble(dLat);
        Double.parseDouble(dLat);
        //all are numbers
      } catch (NumberFormatException e){
        if (firstPairDoubles) {
          MapNode temp = RouteTriggerAction.nearestToCoords(dLat, dLon);

          dLat = Double.toString(temp.getCoordinate(0));
          dLon = Double.toString(temp.getCoordinate(1));
          //find intersection coordinate of 3/4
        } else {
          try {
            Double.parseDouble(dLat);
            Double.parseDouble(dLat);

            MapNode temp = RouteTriggerAction.nearestToCoords(sLat, sLon);

            sLat = Double.toString(temp.getCoordinate(0));
            sLon = Double.toString(temp.getCoordinate(1));
            //find intersection coordinate of 1/2
          } catch (NumberFormatException ee){
            //all are roads
          }
        }

      }
      System.out.println(sLat+", "+sLon+", "+dLat+", "+dLon);
      String[] results =
          getResults.executeTriggerAction("route",
              new String[] {sLat, sLon, dLat, dLon},
              false).split(";");


      Map<String, Object> variables = ImmutableMap.of("route", Arrays.copyOfRange(results, 0, results.length/2));
      return Main.getGSON().toJson(variables);

    }
  }

  /**
   * Handles requests made for Ways.
   */
  protected static class WaysHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      double sLat = data.getDouble("srclat");
      double sLon = data.getDouble("srclong");
      double dLat = data.getDouble("destlat");
      double dLon = data.getDouble("destlong");

      TriggerActionExecutor getResults = new TriggerActionExecutor(Main.getACTIONS());
      String[] results =
          getResults.executeTriggerAction("ways",
              new String[] {Double.toString(sLat), Double.toString(sLon), Double.toString(dLat),Double.toString(dLon)},
              false).split("\n");


      Map<String, Object> variables = ImmutableMap.of("ways", results);
      return Main.getGSON().toJson(variables);

    }
  }

  /**
   * Handles requests made for Checkins.
   */
  protected static class CheckinHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      /*
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:data/maps/checkins.sqlite3";
      Connection conn = DriverManager.getConnection(urlToDB);
      PreparedStatement prep = conn.prepareStatement(
          "SELECT * FROM map_checkin WHERE (map_checkin.ts >= ?) ORDER BY map_checkin.ts ASC;");
      prep.setDouble(1, mostRecentTime);
      ResultSet rs = prep.executeQuery();
      */
      Map<Double, UserCheckin> rawResults = Main.getCt().getLatestCheckins();
      Set<Double> timestampSet = rawResults.keySet();
      List<String> results = new ArrayList<>();
      String id;
      String name;
      String ts/* = Double.toString(mostRecentTime)*/;
      String lat;
      String lon;
      UserCheckin currentCheckin;
      for (Double timestamp : timestampSet) {
        currentCheckin = rawResults.get(timestamp);
        id = Integer.toString(currentCheckin.getId());
        name = currentCheckin.getName();
        ts = Double.toString(currentCheckin.getTimestamp());
        lat = Double.toString(currentCheckin.getLat());
        lon = Double.toString(currentCheckin.getLon());
        results.add(id+","+name+","+ts+","+lat+","+lon);
      }
      /*
      while (rs.next()) {
        id = Integer.toString(rs.getInt(1));
        name = rs.getString(2);
        ts = Double.toString(rs.getDouble(3));
        lat = Double.toString(rs.getDouble(4));
        lon = Double.toString(rs.getDouble(5));
        results.add(id+","+name+","+ts+","+lat+","+lon);
      }
      mostRecentTime = Double.parseDouble(ts);
       */
      String[] resultsList = results.toArray(new String[0]);

      Map<String, Object> variables = ImmutableMap.of("checkins", resultsList);

      return Main.getGSON().toJson(variables);
      //return GSON.toJson(ct.getLatestCheckins());

    }
  }

  /**
   * Handles requests made for User Data.
   */
  protected static class UserHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {


      int targetID;
      JSONObject data = new JSONObject(request.body());
      try {
        targetID = Integer.parseInt(data.getString("id"));
      } catch (NumberFormatException e) {
        System.out.print("ERROR: Number Format Exception");

        Map<String, Object> variables = ImmutableMap.of("user",new ArrayList<>());
        return Main.getGSON().toJson(variables);
      }


      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:data/maps/checkins.sqlite3";
      Connection conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      PreparedStatement prep = conn.prepareStatement(
          "SELECT * FROM map_checkin WHERE (? = map_checkin.id);");
      prep.setInt(1, targetID);
      List<String> results = new ArrayList<>();
      ResultSet rs = prep.executeQuery();
      String id;
      String name;
      String ts;
      String lat;
      String lon;
      while (rs.next()) {
        id = Integer.toString(rs.getInt(1));
        name = rs.getString(2);
        ts = Double.toString(rs.getDouble(3));
        lat = Double.toString(rs.getDouble(4));
        lon = Double.toString(rs.getDouble(5));
        results.add(id+","+name+","+ts+","+lat+","+lon);
      }
      Map<String, Object> variables = ImmutableMap.of("user", results);
      return Main.getGSON().toJson(variables);

    }
  }
}
