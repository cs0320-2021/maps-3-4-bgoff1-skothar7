package edu.brown.cs.madhavramesh.mockaroo;

import edu.brown.cs.madhavramesh.repl.CSVParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Contains methods that can be applied to Mockpersons. */
public class MockApplications {
  private final List<String> header =
      Arrays.asList("first_name", "last_name", "datetime", "email",
          "gender", "street_address");
  private CSVParser peopleCSV;
  private List<MockPerson> mockPeople;

  /** Loads a CSV file and stores it.
   * @param path path to CSV file
   * @throws IllegalArgumentException if header or width of rows incorrect
   * @throws IOException if not found
   */
  public void csvParser(String path)
      throws IllegalArgumentException, IOException {
    peopleCSV = new CSVParser(path);
    if (!peopleCSV.isCorrectHeader(header)) {
      throw new IllegalArgumentException("ERROR: Invalid header");
    } else if (!peopleCSV.isCorrectRowLength(header.size())) {
      throw new IllegalArgumentException("ERROR: Number of elements "
          + "in each row do not match");
    }

    peopleCSV.removeHeader();
    mockPeople = new ArrayList<>();
    convertCSVtoMockPeople();
  }

  /** Converts each row in the CSV file to a MockPerson object. */
  private void convertCSVtoMockPeople() {
    for (List<String> row : peopleCSV.getData()) {
      String firstName = row.get(0);
      String lastName = row.get(1);
      String datetime = row.get(2);
      String email = row.get(3);
      String gender = row.get(4);
      String streetAddress = row.get(5);

      MockPerson person = new MockPerson(firstName, lastName, datetime,
          email, gender, streetAddress);
      mockPeople.add(person);
    }
  }

  /** Returns a List of List of Strings representation of CSV file.
   * @return List of List of Strings representing CSV file
   */
  public CSVParser getCSV() {
    return peopleCSV;
  }

  /** Returns a List of MockPersons representing the data from the
   * CSV file.
   * @return List of Mockpersons
   */
  public List<MockPerson> getMockPeople() {
    return mockPeople;
  }
}
