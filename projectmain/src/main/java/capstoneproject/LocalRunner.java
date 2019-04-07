package capstoneproject;

import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

public class LocalRunner extends AbstractRunner{
    private final String[] LOCALQ = {"coin","random","time"};
    public LocalRunner(){
        types=new HashSet<>(Arrays.asList(LOCALQ));
    }

    public JSONObject returnable(String data, String info){
        JSONObject toReturn = new JSONObject();
        switch(data)
        {
            case "coin":
                toReturn.put("type","coin");
                if((int)(Math.random()*2)==1){
                    toReturn.put("response","heads");
                }
                else{
                    toReturn.put("response","tails");
                }
                return toReturn;
            case "random":
                toReturn.put("type","random");
                int val=(int)(Math.random()*100);
                toReturn.put("response",val);
                return toReturn;
            case "time":
                toReturn.put("type", "time");
                String theTime=java.time.LocalTime.now().toString();
                theTime=theTime.substring(0,5);
                toReturn.put("response",theTime);
                return toReturn;

        }
        return null;
    }

    @Override
    public JSONObject response(NLPinfo input) {
        return returnable(input.getQuery().toString().toLowerCase(), input.RelevantInfo);
    }
}
