import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransportData {

    private List<Integer> data = new ArrayList<Integer>();
    JSONParser parser = new JSONParser();
    private int[] outputData;
    private int length;

    public TransportData(String filePath, String dataName) {
        getArrayFromJson(dataName, filePath, data);
        outputData = toIntArray();
        length = outputData.length;
    }

    public  int[] getData(){
        return outputData;
    }
    public int length(){
        return length;
    }

    private void getArrayFromJson(String objName, String path, List<Integer> arrayList) {
        try {
            Object obj = parser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray list = (JSONArray) jsonObject.get(objName);
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Object object = iterator.next();
                    arrayList.add(Integer.parseInt(object.toString()));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printData(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < outputData.length ; i++) {
            builder.append(outputData[i]);
            if(i < outputData.length -1) builder.append(", ");

        }
        String string = builder.toString();
        System.out.println(string);
    }

    private int[] toIntArray(){
        int tab[] = new int[data.size()];
        for (int i = 0; i < data.size() ; i++) {
            tab[i] = data.get(i);
        }
        return tab;
    }


}



