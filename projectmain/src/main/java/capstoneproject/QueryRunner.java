package capstoneproject;


import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

/*
* Singleton Design pattern for the overarching class that separates the types of queries
*/
public class QueryRunner {
    private AbstractRunner[] aRlist;//list of the 3 runners for the different types of functions


    //singleton stuff
    private static final QueryRunner INSTANCE = new QueryRunner();
    private QueryRunner(){
        aRlist=new AbstractRunner[3];
        aRlist[0]=new LocalRunner();
        aRlist[1]=new WebRunner();
        aRlist[2]=new SQLRunner();
    }

    public static QueryRunner getInstance(){
        return INSTANCE;
    }
    //end of singleton stuff


    //this is what i'm gonna use
    public JSONObject nlpTransform(NLPinfo input){
        for(int i=0;i<aRlist.length;i++){
            if(aRlist[i].contains(input.getQuery().toString().toLowerCase())){
                return aRlist[i].response(input);
            }
        }
        return null;
    }

}
