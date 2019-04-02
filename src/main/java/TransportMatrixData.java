import javax.json.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TransportMatrixData {

    private List<String> data = new ArrayList<String>();
    private int row;
    private int col;
    private int[][] outputData;

    public TransportMatrixData(String filePath, String dataName, int row, int col) {
        this.row = row;
        this.col = col;
        getArrayFromJson(dataName, filePath, data);
        outputData = toIntMatrix();
    }

    public int[][] getData() {
        return this.outputData;
    }


    private void getArrayFromJson(String objName, String path, List<String> arrayList) {
        File jsonInputFile = new File(path);
        InputStream is;
        try {
            is = new FileInputStream(jsonInputFile);
            JsonReader reader = Json.createReader(is);
            JsonObject empObj = reader.readObject();
            reader.close();
            JsonArray arrObj = empObj.getJsonArray(objName);
            for (JsonValue value : arrObj) {
                arrayList.add(value.toString());
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private int[][] toIntMatrix() {
        int tab[][] = new int[row][col];

        for (int i = 0; i < data.size(); i++) {
            String row = data.get(i);
            int[] temp = getDataFromRow(row);
            tab[temp[0] - 1][temp[1] - 1] = temp[2];
        }
        return tab;

    }

    private int[] getDataFromRow(String row) {
        String data = row.substring(1, row.length() - 1);
        int[] dataTable = new int[data.length()];

        String[] parts = data.split(",");
        for (int i = 0; i < parts.length; i++) {
            dataTable[i] = Integer.parseInt(parts[i]);

        }
        return dataTable;

    }

    public void printData() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < outputData.length; i++) {
            for (int j = 0; j < outputData[i].length; j++) {
                builder.append(outputData[i][j]);
                if (j < outputData[i].length - 1) builder.append(", ");
            }
            builder.append("\n");
        }
        String string = builder.toString();
        System.out.println(string);
    }

}



