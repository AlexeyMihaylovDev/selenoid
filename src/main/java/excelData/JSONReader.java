package excelData;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class JSONReader {

    private JSONObject jsonObject;

    public JSONReader(String JsonPath) {
        JSONParser parser = new JSONParser();
        try {
            BufferedReader buff= new BufferedReader(new InputStreamReader(new FileInputStream(JsonPath), "UTF-8"));

            Object obj = parser.parse(buff);
            jsonObject = (JSONObject) obj;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public String readData(String tag) {

        String data = null;
        try {
            data = (String) jsonObject.get(tag);
        } catch (Exception e) {

        }
        return data;

    }

    public String JSONArrayDate(String Arrname, String tag) {
        JSONArray array = (JSONArray) jsonObject.get(Arrname);
        String link = null;
        for (int i = 0; i < array.size(); i++) {
            JSONObject links = (JSONObject) array.get(i);
            link = (String) links.get(tag);
        }
        return link;
    }



}

