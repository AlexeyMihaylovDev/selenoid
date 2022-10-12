package excelData;


import java.io.*;

public class ReadFile {


    public String IP_instance(String filepath ){
        try {
            FileReader fr = new FileReader(filepath);
            BufferedReader reader = new BufferedReader(fr);
            String line;
            while((line=reader.readLine())!=null) {
                System.out.println(line);
                return line;
            }
        }catch(IOException ex) {
            ex.printStackTrace();
        }
       return  null;
    }


}