package edu.brown.cs.madhavramesh.mockaroo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/** Person with first name, last name, date, email, gender, and
 * street address information.
 */
public class MockPerson {
  private Map<String, String> person;
  private final Pattern genderPattern = Pattern.compile("[a-zA-Z]*");
  private final Pattern datetimePattern =
      Pattern.compile("(\\d{1,2}\\/\\d{1,2}\\/\\d{4})");
  private final Pattern emailPattern = Pattern.compile(".*@.*");

  /** Constructs a person from the given information.
   * @param firstName First name
   * @param lastName Last name
   * @param datetime Date
   * @param email Email
   * @param gender Gender
   * @param streetAddress Street address
   */
  public MockPerson(String firstName, String lastName, String datetime,
                    String email, String gender, String streetAddress) {
    person = new LinkedHashMap<>();
    person.put("First Name: ", firstName);
    person.put("Last Name: ", lastName);

    Matcher datetimeMatcher = datetimePattern.matcher(datetime);
    if (datetimeMatcher.find() || datetime.equals("")) {
      person.put("Date and Time: ", datetime);
    } else {
      person.put("Date and Time: ", "Unrecognized date and time format");
    }

    Matcher emailMatcher = emailPattern.matcher(email);
    if (emailMatcher.find() || email.equals("")) {
      person.put("Email: ", email);
    } else {
      person.put("Email: ", "Unrecognized email format");
    }

    Matcher genderMatcher = genderPattern.matcher(gender);
    if (genderMatcher.find() || gender.equals("")) {
      person.put("Gender: ", gender);
    } else {
      person.put("Gender: ", "Unrecognized gender format");
    }

    person.put("Street Address: ", streetAddress);
  }

  /** Returns a String representation of a person with fields separated
   * by commas and Unknown replacing a field if it's empty.
   * @return String representation of person
   */
  @Override
  public String toString() {
    String output = "";
    for (Map.Entry field : person.entrySet()) {
      if (field.getValue().equals("")) {
        output += (String) field.getKey() + "Unknown, ";
      } else {
        output += (String) field.getKey() + field.getValue() + ", ";
      }
    }
    return output;
  }
}
