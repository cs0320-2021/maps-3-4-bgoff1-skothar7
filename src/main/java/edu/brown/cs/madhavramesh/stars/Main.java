package edu.brown.cs.madhavramesh.stars;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.List;

import com.google.gson.Gson;
import edu.brown.cs.madhavramesh.maps.MapTriggerAction;
import edu.brown.cs.madhavramesh.maps.NearestTriggerAction;
import edu.brown.cs.madhavramesh.maps.RouteTriggerAction;
import edu.brown.cs.madhavramesh.maps.WaysTriggerAction;
import edu.brown.cs.madhavramesh.mockaroo.MockTriggerAction;
import edu.brown.cs.madhavramesh.repl.REPL;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;
import org.json.JSONObject;
import org.json.JSONArray;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();

  private static final List<TriggerAction> ACTIONS = Arrays.asList(
      new StarsTriggerAction(),
      new NaiveNeighborsTriggerAction(),
      new NeighborsTriggerAction(),
      new NaiveRadiusTriggerAction(),
      new RadiusTriggerAction(),
      new MockTriggerAction(),
      new WaysTriggerAction(),
      new RouteTriggerAction(),
      new NearestTriggerAction(),
      new MapTriggerAction());

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    // Process commands in a REPL
    REPL.run(ACTIONS);
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/stars", new FrontHandler(), freeMarker);
    Spark.post("/input", new InputHandler(), freeMarker);
    Spark.post("/results", new SubmitHandler(), freeMarker);
    Spark.post("/ways", new WaysHandler());
    Spark.post("/route", new RouteHandler());
  }

  /**
   * Handle requests to the front page of our Stars website.
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of(
          "title", "Stars: Query the database");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  private static class InputHandler implements TemplateViewRoute {
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

  private static class SubmitHandler implements TemplateViewRoute {
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

      TriggerActionExecutor getResults = new TriggerActionExecutor(ACTIONS);
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

  private static class RouteHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      double sLat = data.getDouble("srclat");
      double sLon = data.getDouble("srclong");
      double dLat = data.getDouble("destlat");
      double dLon = data.getDouble("destlong");

      String src = Double.toString(sLon);
      String dest = Double.toString(dLon);
      String rand1 = Double.toString(Math.random()*100);


      TriggerActionExecutor getResults = new TriggerActionExecutor(ACTIONS);
      String[] results =
          getResults.executeTriggerAction("route",
              new String[] {Double.toString(sLat), Double.toString(sLon), Double.toString(dLat),Double.toString(dLon)},
              false).split("\n");


      Map<String, Object> variables = ImmutableMap.of("route", results);
      return GSON.toJson(variables);

    }
  }

  /**
   * Handles requests made for Ways.
   */
  private static class WaysHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      double sLat = data.getDouble("srclat");
      double sLon = data.getDouble("srclong");
      double dLat = data.getDouble("destlat");
      double dLon = data.getDouble("destlong");

      TriggerActionExecutor getResults = new TriggerActionExecutor(ACTIONS);
      String[] results =
          getResults.executeTriggerAction("ways",
              new String[] {Double.toString(sLat), Double.toString(sLon), Double.toString(dLat),Double.toString(dLon)},
              false).split("\n");


      Map<String, Object> variables = ImmutableMap.of("ways", results);
      return GSON.toJson(variables);

    }
  }


  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
