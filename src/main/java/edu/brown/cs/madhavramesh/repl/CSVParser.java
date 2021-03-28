package edu.brown.cs.madhavramesh.repl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;

/** Stores a CSV file as a List of List of Strings. */
public class CSVParser {
  private List<List<String>> rows;

  /** Loads in a CSV file using a comma as the separator.
   * @param path path to the CSV file
   * @throws IOException if file not found
   */
  public CSVParser(String path)
      throws IOException {
    try (BufferedReader br = new BufferedReader(
        new FileReader(path))) {
      rows = new ArrayList<>();
      String row;
      while ((row = br.readLine()) != null) {
        List<String> rowData = Arrays.asList(row.split(","));
        rows.add(rowData);
      }
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("ERROR: File not found");
    } catch (IOException e) {
      throw new IOException("ERROR: Unable to read file");
    }
  }

  /** Checks if the header of the CSV file matches a given
   * List of Strings.
   * @param headers List of Strings representing the correct Header
   *                for the CSV file
   * @return true if headers matches CSV file header, false otherwise
   */
  public boolean isCorrectHeader(List<String> headers) {
    return (rows.get(0).equals(headers));
  }

  /** Removes the Header of the loaded CSV file. */
  public void removeHeader() {
    rows.remove(0);
  }

  /** Checks if all rows in the CSV file are of a certain length.
   * @param size Correct length of rows in CSV file
   * @return true if rows are all of a certain length, false otherwise
   */
  public boolean isCorrectRowLength(int size) {
    for (List<String> row : rows) {
      if (row.size() != size) {
        return false;
      }
    }
    return true;
  }

  /** Searches for a given String in a given column index.
   * @param val String to search for
   * @param searchCol Column index to search in
   * @return Row index of entry if found, error otherwise
   */
  public int findEntry(String val, int searchCol)
    throws IllegalArgumentException {
    for (int i = 0; i < rows.size(); i++) {
      if (rows.get(i).get(searchCol).equals(val)) {
        return i;
      }
    }
    throw new IllegalArgumentException("ERROR: Given name could not be found");
  }

  /** Returns the row corresponding to the given index.
   * @param r Row index to find
   * @return List of Strings representing row
   */
  public List<String> get(int r) {
    return new ArrayList<>(rows.get(r));
  }

  /** Returns the List of List of Strings representing CSV file.
   * @return List of List of Strings representing CSV file
   */
  public List<List<String>> getData() {
    return new ArrayList<>(rows);
  }

  /** Returns number of rows in CSV file.
   * @return Number of rows
   */
  public int size() {
    return rows.size();
  }
}
