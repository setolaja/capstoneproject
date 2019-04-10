package capstoneproject;

import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

public class LocalRunner extends AbstractRunner{
    private final String[] LOCALQ = {"coin","random","time", "startup","calc"};
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
            case "startup":
                toReturn.put("type","startup");
                toReturn.put("response","hello");
                return toReturn;
            case "calc":
                return calc(info);
        }
        return null;
    }

    @Override
    public JSONObject response(NLPinfo input) {
        return returnable(input.getQuery().toString().toLowerCase(), input.RelevantInfo);
    }

    public JSONObject calc(String info){
        info=info.toLowerCase();

        JSONObject toReturn = new JSONObject();
        toReturn.put("type", "calc");
        String dubOp=null;
        String unOp=null;
        if(info.contains("plus")){
            dubOp = " plus ";
        }
        else if(info.contains("minus")){
            dubOp=" minus ";
        }
        else if(info.contains("times")){
            dubOp=" times ";
        }
        else if(info.contains("divide")){
            dubOp=" divided by ";
        }
        else if(info.contains("square root")){
            unOp="square root of ";
        }
        else if(info.contains("squared")){
            unOp=" squared";
        }
        else{return null;}


        if(dubOp!=null){
            String[] nums=info.split(dubOp);
            double ret=0;
            switch(dubOp){
                case " plus ":
                    ret=Integer.parseInt(nums[0])+Integer.parseInt(nums[1]);
                    break;
                case " minus ":
                    ret = Integer.parseInt(nums[0])-Integer.parseInt(nums[1]);
                    break;
                case " times ":
                    ret=Integer.parseInt(nums[0])*Integer.parseInt(nums[1]);
                    break;
                case " divided by ":
                    ret=(Integer.parseInt(nums[0])/1.0)/(Integer.parseInt(nums[1])/1.0);
                    break;
            }
            toReturn.put("response",ret);
            return toReturn;
        }
        if(unOp!=null){
            double val = 0;
            switch(unOp){
                case " squared":
                    val = Integer.parseInt(info.split(unOp)[0]);
                    val*=val;
                    break;
                case "square root of ":
                    val = Integer.parseInt(info.split(unOp)[1]);
                    val=Math.sqrt(val);
            }
            toReturn.put("response",val);
            return toReturn;
        }

        return null;
    }
}
