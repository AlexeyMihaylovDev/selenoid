package base;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JSONDataProviders {


    @DataProvider(name = "JsonData")
    public Object[] readJson() throws IOException, ParseException {
        JSONParser jsonParser=new JSONParser();
        BufferedReader buff= new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/sivan.json"), "UTF-8"));
        Object obj=jsonParser.parse(buff);
        JSONObject searchingJeson=(JSONObject) obj;
        JSONArray searchingJesonArray=(JSONArray)searchingJeson.get("searching_orders");

        String arr[]=new String[searchingJesonArray.size()];
        for(int i=0; i<searchingJesonArray.size();i++){

            JSONObject search=(JSONObject)searchingJesonArray.get(i);
            String name_search=(String)search.get("order");
            String results=(String)search.get("result");
            arr[i]=name_search+","+results;

        }
        return arr;
    }
}
