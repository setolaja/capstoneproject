package capstoneproject;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashSet;

public class WebRunner extends AbstractRunner{
    private final String APIKEY = "ee58613a61258127b50bce1cf101d68f";//weather API Key

    private final String[] QUERIES = {"weather"};
    public WebRunner(){
        types=new HashSet<>(Arrays.asList(QUERIES));
    }



    public JSONObject weather(){
        //creates a reader for the weather website
        URL weatherUrl=null;
        try{
            weatherUrl = new URL("http://api.openweathermap.org/data/2.5/weather?zip=18015,us&APPID="+APIKEY);
        }catch(Exception e){
            System.out.println("malformed url exception: weather");
            return null;
        }
        URLConnection wtrCon=null;
        try{
            wtrCon=weatherUrl.openConnection();
        } catch(Exception e){
            System.out.println("cannot open connection: weather");
            return null;
        }
        InputStream wtrIs = null;
        try{
            wtrIs=wtrCon.getInputStream();
        }catch(Exception e){
            System.out.println("cannot open input stream : weather");
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(wtrIs));


        //adds all data to a single string
        String data = "";
        String line=null;
        try{
            while ((line=br.readLine())!=null){
                data+=line;
            }
        }catch(IOException e){
            System.out.print("could not read from writer: weather");
        }
        JSONObject toReturn=new JSONObject();
        toReturn.put("type","weather");

        JSONObject wtrData = new JSONObject();

        String[] p1=data.split("\"main\":\"");
        String theWeather=p1[1].split("\",\"")[0];//splits string at the current weather condition
        String temp=p1[1].split("\"temp\":")[1].split("\\.")[0];
        int theTemp=Integer.parseInt(temp)-273;
        theTemp=(int)(theTemp*1.8) + 32;
        String location=data.split("\"name\":\"")[1].split("\",\"")[0];

        wtrData.put("weather",theWeather);
        wtrData.put("temp",theTemp);
        wtrData.put("location",location);
        toReturn.put("response",wtrData);
        System.out.println(data);
        System.out.println();
        return toReturn;
    }

    @Override
    public JSONObject response(String input) {
        return weather();
    }
}
