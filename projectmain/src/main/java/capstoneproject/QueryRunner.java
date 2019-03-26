package capstoneproject;


import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

/*
* Singleton Design pattern for the overarching class that separates the types of queries
*/
public class QueryRunner {
    private LocalRunner locR;//runner for queries on local machine
    private WebRunner wR;//runner for web queries


    private final String[] LOCALQ = {"coin","random","time"};//different types of queries for local runner
    private HashSet<String> localQueries;//put in a hash set for constant lookup time


    //singleton stuff
    private static final QueryRunner INSTANCE = new QueryRunner();
    private QueryRunner(){
        wR=new WebRunner();
        locR = new LocalRunner();
        localQueries=new HashSet<>(Arrays.asList(LOCALQ));
    }

    public static QueryRunner getInstance(){
        return INSTANCE;
    }
    //end of singleton stuff

    public JSONObject output(String input){
        if(localQueries.contains(input)){
            return locR.returnable(input);
        }
        else if(input.equalsIgnoreCase("weather")){
            return wR.weather();
        }
        return null;
    }

}
