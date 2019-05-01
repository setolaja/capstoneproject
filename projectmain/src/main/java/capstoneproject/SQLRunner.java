package capstoneproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.json.simple.JSONObject;

public class SQLRunner extends AbstractRunner {

    Database db;

    private final String[] QUERIES = {"ohours","bclose"};
    public SQLRunner(){
        types=new HashSet<>(Arrays.asList(QUERIES));
        db=Database.getDatabase();
    }

    @Override
    public JSONObject response(NLPinfo input) {
        JSONObject toReturn = new JSONObject();

        switch(input.getQuery().toString().toLowerCase()){
            case "ohours":
                return db.getOfficeHours(input.RelevantInfo);
            case "bclose":
                return db.getBuilingHours(input.RelevantInfo);
        }
        return null;
    }
}
