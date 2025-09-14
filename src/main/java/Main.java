import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static java.nio.file.Files.writeString;

public class Main {
    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))){
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json) {
        try (FileWriter fileWriter = new FileWriter("data.json")) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
