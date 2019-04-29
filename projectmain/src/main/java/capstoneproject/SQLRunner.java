package capstoneproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.json.simple.JSONObject;

public class SQLRunner extends AbstractRunner {

    final String ohQuery = "SELECT P_name, Building_Name, RoomN, StartT, EndT, DayOfWeek " +
                            "FROM officehour NATURAL JOIN building NATURAL JOIN professor " +
                            "WHERE P_name = ";

    private final String[] QUERIES = {"ohours","bclose"};
    public SQLRunner(){types=new HashSet<>(Arrays.asList(QUERIES));}

    @Override
    public JSONObject response(NLPinfo input) {
        JSONObject toReturn = new JSONObject();

        try{
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl=null;

            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl,"root","");
            Statement st=conn.createStatement();

            ResultSet rs=null;
            ArrayList<JSONObject> results = new ArrayList<JSONObject>();
            switch(input.getQuery().toString().toLowerCase()){
                case "ohours":
                    toReturn.put("type","ohours");
                    rs=st.executeQuery(ohQuery+input.RelevantInfo);
                    while(rs.next()){
                        JSONObject res=new JSONObject();
                        res.put("p_name",rs.getString("P_name"));
                        res.put("Building_name",rs.getString("Building_Name"));
                        res.put("RoomN",rs.getInt("RoomN"));
                        res.put("StartT",rs.getTime("StartT"));
                        res.put("EndT",rs.getTime("EndT"));
                        res.put("day",rs.getString("DayOfWeek"));
                        results.add(res);
                    }
                    toReturn.put("response",results);
                    return toReturn;
            }

        }catch(Exception e){
            toReturn.put("type","error");
            toReturn.put("response",e.getMessage());
            return toReturn;
        }

        return null;
    }
}
