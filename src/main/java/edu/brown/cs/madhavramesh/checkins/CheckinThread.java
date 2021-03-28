package edu.brown.cs.madhavramesh.checkins;

import edu.brown.cs.madhavramesh.maps.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * thread that continuously communicates with checkin server.
 */
public final class CheckinThread extends Thread {
  private long last = 0;
  private Map checkins;
  private boolean pause = false;
  static final long MSCONVERSION = 1000;

  public CheckinThread() {
    System.out.println("hello");
    checkins = Collections.synchronizedMap(new HashMap<>());
  }

  /**
   * runs the thread by querying the url for information on user checkins.
   */
  public synchronized void run() {
    PreparedStatement prep;
    Connection conn = null;
    try {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:data/maps/checkins.sqlite3";
    conn = DriverManager.getConnection(urlToDB);
      prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS map_checkin ("
          + "id INTEGER,"
          + "name TEXT,"
          + "ts DOUBLE,"
          + "lat DOUBLE,"
          + "lon DOUBLE);");
      prep.executeUpdate();
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    List<List<String>>updates = null;

    long lastSec = 0;

    while (true) {
      long sec = System.currentTimeMillis() / MSCONVERSION;
      if (sec != lastSec && !pause) {
        try {
          updates = this.update();
        } catch (IOException e) {
          e.printStackTrace();
        }

        if (updates != null && !updates.isEmpty()) {
          for (List<String> el : updates) {
            int id = Integer.parseInt(el.get(1));
            String name = el.get(2);
            double timestamp = Double.parseDouble(el.get(0));
            double lat = Double.parseDouble(el.get(3));
            double lon = Double.parseDouble(el.get(4));

            // put in concurrent hashmap
            UserCheckin uc = new UserCheckin(id, name, timestamp, lat, lon);
            checkins.put(timestamp, uc);
            try {
              prep = conn.prepareStatement("INSERT INTO map_checkin VALUES (" + id + ", \'" + name + "\', " + timestamp + ", " + lat + ", " + lon + ");");
              prep.executeUpdate();
            } catch (SQLException e) {
              e.printStackTrace();
            }

          }
        }
        lastSec = sec;
      }
    }
  }
  private synchronized List<List<String>> update() throws IOException {
    URL serverURL = new URL("http://localhost:8080?last=" + last);
    last = Instant.now().getEpochSecond();

    HttpURLConnection conn = (HttpURLConnection) serverURL.openConnection();
    conn.setRequestMethod("GET");

    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    Pattern pattern = Pattern.compile("\\[(.*?)\\, (.*?)\\, \"(.*?)\", (.*?)\\, (.*?)\\]");
    String line;

    List<List<String>> output = new ArrayList<>();
    while ((line = br.readLine()) != null) {
      Matcher matcher = pattern.matcher(line);
      while (matcher.find()) {
        List<String> data = new ArrayList<>();
        String parsedTimestamp = matcher.group(1);
        if (parsedTimestamp.charAt(0) == '[') {
          data.add(parsedTimestamp.substring(1));
        } else {
          data.add(parsedTimestamp);
        }
        data.add(matcher.group(2));
        data.add(matcher.group(3));
        data.add(matcher.group(4));
        data.add(matcher.group(5));
        output.add(data);
      }
    }
    return output;
  }


  /**
   * gets the latest checkin updates. Refreshes hashmap so only new
   * checkin updates are returned next time.
   * @return map from a string to a double of timestamps to checkin objects
   */
  public Map<Double, UserCheckin> getLatestCheckins() {
    pause = true;
    Map<Double, UserCheckin> temp = checkins;
    checkins = Collections.synchronizedMap(new HashMap<>());
    pause = false;
    return temp;
  }
}