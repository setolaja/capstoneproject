package capstoneproject.DatabaseFiles;

import java.sql.Connection;

public class DatabaseRoutes {


    //Global variable for conn, so you don't need to set up a connection for every route.
    // the connection is setup in the constructor for this class.
    public Connection conn;
    public DatabaseRoutes()
    {
        DataBaseConnection D1 = new DataBaseConnection();
        conn = D1.getConnection();

    }


    //return type is void because I don't know what object the database will return, probably just a json.
    // The SQL Query to be implemented:
    //SELECT P_name, Building_Name, RoomN, StartT, EndT, DayOfWeek
    //FROM officehour NATURAL JOIN building NATURAL JOIN professor
    //WHERE P_name =  'Montella'
    public void queryOfficeHours(String ProfessorName)
    {

    }

    public void queryBuildingOpenT(String BuildingName)
    {

    }
    // the same as above but query for closing time.
    public void queryBuildingCloseT(String BuildingName)
    {

    }


    //main method to test methods of this class.
    public static void main(String[]args)
    {
      DatabaseRoutes n1 = new DatabaseRoutes();
    }
}
