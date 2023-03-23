package budget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

  static void createFile(File file) {
    try {
      file.createNewFile();
    } catch (IOException e) {
      System.out.println("Wrong file path for the purchases file: " + file.getAbsolutePath());
    }
  }

  static void saveToFile(String data, File file) throws IOException {
    try {
      FileWriter fileWriter = new FileWriter(file, false);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      bufferedWriter.write(data);
      bufferedWriter.close();
    } catch (IOException ex) {
      throw new IOException("Error writing to file '" + file.getAbsolutePath() + "'\n");
    }
  }

  static ArrayList<String> loadFromFile(File file) throws IOException {
    String line;
    ArrayList<String> data = new ArrayList<>();
    try {
      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      while ((line = bufferedReader.readLine()) != null) {
        data.add(line);
      }
      bufferedReader.close();
    } catch (IOException ex) {
      throw new IOException("Error reading file '" + file + "'");
    }
    return data;
  }
}
