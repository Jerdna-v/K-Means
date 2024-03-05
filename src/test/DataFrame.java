package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DataFrame {
    private List<String> columns;
    private List<List<Object>> data;


    public DataFrame(String filePath) {
        jsonFile(filePath);
    }

//    public List<String> getColumns() {
//        return columns;
//    }
//
//    public List<List<Object>> getData() {
//        return data;
//    }

    public List<Object> getColumn(String columnName) {
        int columnIndex = columns.indexOf(columnName);
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Column '" + columnName + "' not found.");
        }

        List<Object> column = new ArrayList<>();
        for (List<Object> record : data) {
            column.add(record.get(columnIndex));
        }

        return column;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("test.DataFrame:\n");

        sb.append("Columns: ").append(columns).append("\n");
        System.out.println(data);
            for (List<Object> record : data) {
                sb.append(record).append("\n");
            }

        return sb.toString();
    }

    public void jsonFile(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(filePath));
            StringBuilder jsonContent = new StringBuilder();

            while (scanner.hasNextLine()) {
                jsonContent.append(scanner.nextLine());
            }

            String jsonString = jsonContent.toString();
            jsonString = jsonString.substring(1, jsonString.length() - 1);
            String[] records = jsonString.split("},\\{");

            String firstRecord = "{" + records[0] + "}";
            this.columns = getColumns(firstRecord);
            this.data = parseDataFrame(Arrays.asList(records));

            scanner.close();
        } catch (FileNotFoundException ignored) {  }
    }

    private List<String> getColumns(String recordStr) {
        List<String> columns = new ArrayList<>();
        String[] parts = recordStr.split(",");
        for (String part : parts) {
            String columnName = part.split(":")[0].replace("\"", "").trim();
            columns.add(columnName);
        }
        return columns;
    }

    private List<List<Object>> parseDataFrame(List<String> records) {
        List<List<Object>> data = new ArrayList<>();
        for (String recordStr : records) {
            recordStr = "{" + recordStr + "}";
            List<Object> record = parseRecord(recordStr);
            data.add(record);
        }
        return data;
    }

    private static List<Object> parseRecord(String recordStr) {
        List<Object> values = new ArrayList<>();
        String[] parts = recordStr.split(",");
        for (String part : parts) {
            String[] keyValue = part.split(":");
            String value = getValue(keyValue[1].trim());

            values.add(value);
        }
        return values;
    }

    private static String getValue(String part) {
        if (part.endsWith("}")) {
            part = part.substring(0, part.length() - 1);
        }

        return part.replace("\"", "").trim();
    }
}
