package capstoneproject;

import net.didion.jwnl.data.Exc;
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
    private final String NEWSKEY = "4111f4b77e6e4ad19c27a406590e77c1";//news API key

    private final String[] QUERIES = {"weather", "news"};
    public WebRunner(){
        types=new HashSet<>(Arrays.asList(QUERIES));
    }

    public JSONObject news(){
        URL newsUrl = null;
        try {
            newsUrl = new URL("https://newsapi.org/v2/top-headlines?country=us&apiKey="+NEWSKEY);
        } catch (Exception e){
            System.out.println("news: malformed url exception");
            return null;
        }

        URLConnection newsCon=null;
        try{
            newsCon=newsUrl.openConnection();
        }catch(Exception e){
            System.out.println("cannot open connection: news");
            return null;
        }

        InputStream nwsIs=null;
        try{
            nwsIs=newsCon.getInputStream();
        }catch(Exception e){
            System.out.println("cannot open input stream: news");
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(nwsIs));
        String data = "";
        String line = null;

        try{
            while((line=br.readLine())!=null){
                data+=line;
            }
        }catch(Exception e){
            System.out.println("could not read from writer: news");
        }
        System.out.println(data);
        String[] titles = data.split("\"title\":");
        String[] ret = new String[titles.length];
        for(int i=0;i<titles.length;i++){
            ret[i]=titles[i].split(",\"description")[0];
            //System.out.println(ret[i]);
        }
        String[] descs = new String[5];
        for(int i=0;i<descs.length;i++){
            descs[i]=data.split("description\":\"")[i+1].split("\",\"url")[0];
        }

        String[] response = new String[5];
        for(int i=1;i<6;i++){
            response[i-1] = ret[i];
        }

        JSONObject retval = new JSONObject();
        retval.put("type","news");
        retval.put("response",response);
        retval.put("description",descs);

        return retval;
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
       // System.out.println(data);
        //System.out.println();
        return toReturn;
    }

    @Override
    public JSONObject response(NLPinfo input) {
        switch (input.getQuery().toString().toLowerCase()){
            case "weather":
                return weather();
            case "news":
                return news();
        }


        return null;
    }
}
