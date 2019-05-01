package capstoneproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.json.simple.JSONObject;

public class SQLRunner extends AbstractRunner {

    //SQL Query to get a professor's office hours
    final String ohQuery = "SELECT P_name, Building_Name, RoomN, StartT, EndT, DayOfWeek " +
                            "FROM officehour NATURAL JOIN building NATURAL JOIN professor " +
                            "WHERE P_name = ";

    //SQL query to get a building's hours
    final String bhQuery = "";//TODO fill with query for building hours that ends with "WHERE Building_Name = "

    private final String[] QUERIES = {"ohours","bclose"};
    public SQLRunner(){types=new HashSet<>(Arrays.asList(QUERIES));}

    @Override
    public JSONObject response(NLPinfo input) {
        JSONObject toReturn = new JSONObject();

        switch(input.getQuery().toString().toLowerCase()){
            case "ohours":
                Database db = Database.getDatabase();
                return db.getOfficeHours(input.RelevantInfo);
            case "bclose":
                return null;
        }
        return null;
    }
}
