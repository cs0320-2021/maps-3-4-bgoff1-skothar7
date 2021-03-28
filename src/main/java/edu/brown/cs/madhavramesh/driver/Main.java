package edu.brown.cs.madhavramesh.driver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import edu.brown.cs.madhavramesh.checkins.CheckinThread;
import edu.brown.cs.madhavramesh.checkins.UserCheckin;
import edu.brown.cs.madhavramesh.maps.MapTriggerAction;
import edu.brown.cs.madhavramesh.maps.NearestTriggerAction;
import edu.brown.cs.madhavramesh.maps.RouteTriggerAction;
import edu.brown.cs.madhavramesh.maps.WaysTriggerAction;
import edu.brown.cs.madhavramesh.mockaroo.MockTriggerAction;
import edu.brown.cs.madhavramesh.repl.REPL;
import edu.brown.cs.madhavramesh.stars.NaiveNeighborsTriggerAction;
import edu.brown.cs.madhavramesh.stars.NaiveRadiusTriggerAction;
import edu.brown.cs.madhavramesh.stars.NeighborsTriggerAction;
import edu.brown.cs.madhavramesh.stars.RadiusTriggerAction;
import edu.brown.cs.madhavramesh.stars.StarsTriggerAction;
import edu.brown.cs.madhavramesh.stars.TriggerAction;
import edu.brown.cs.madhavramesh.stars.TriggerActionExecutor;
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

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();
  private static double mostRecentTime = 0;
  private static CheckinThread ct;
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
    ct = new CheckinThread();
    ct.start();
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
    Spark.get("/stars", new SparkHandlers.FrontHandler(), freeMarker);
    Spark.post("/input", new SparkHandlers.InputHandler(), freeMarker);
    Spark.post("/results", new SparkHandlers.SubmitHandler(), freeMarker);
    Spark.post("/ways", new SparkHandlers.WaysHandler());
    Spark.post("/route", new SparkHandlers.RouteHandler());
    Spark.post("/checkin", new SparkHandlers.CheckinHandler());
    Spark.post("/user", new SparkHandlers.UserHandler());
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

  public static Gson getGSON() {
    return GSON;
  }

  public static List<TriggerAction> getACTIONS() {
    return ACTIONS;
  }

  public static CheckinThread getCt() {
    return ct;
  }

}
