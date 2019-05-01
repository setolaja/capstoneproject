package capstoneproject;

import org.json.simple.JSONObject;

import java.sql.*;

import java.util.ArrayList;

public class Database {
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectOfficeHrs;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTable;

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its fields.  In the context of this Database, RowData 
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database.  RowData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static class RowData {

        String professorName;
        /**
         * The ID of this row of the database
         */
        String buildingName;
        /**
         * The subject stored in this row
         */
        int roomNumber;
        /**
         * The message stored in this row
         */

        Time startT;

        Time endT;

        String dayOfWeek;


        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowData(String professorName, String buildingName, int roomNumber, Time startT, Time endT, String dayOfWeek) {
            this.professorName = professorName;
            this.buildingName = buildingName;
            this.roomNumber = roomNumber;
            this.startT = startT;
            this.endT = endT;
            this.dayOfWeek = dayOfWeek;
        }
    }

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private Database() {
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase() {
        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            db.mConnection = DriverManager.getConnection("jdbc:mysql://localhost/lehigh","root", "");
            System.out.print("Database is connected !");
        }

        catch (SQLException e){
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        }
        catch(Exception e)
        {
            System.out.print("Do not connect to DB - Error:"+e);
        }

        // Attempt to create all of our prepared statements.  If any of these 
        // fail, the whole getDatabase() call should fail
        try {
            db.mSelectOfficeHrs = db.mConnection.prepareStatement(" SELECT P_name, Building_Name, RoomN, StartT, EndT, DayOfWeek " +
                    "FROM officehour NATURAL JOIN building NATURAL JOIN professor " +
                    "WHERE P_name = ?");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * FROM messageData");

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * TJ Fogarty
     * Insert a row into the database
     * 
     * @param ProfessorName professor name passed by Derek
     * 
     * @return SHOULD BE JSON CHANGE
     */
    JSONObject getOfficeHours(String ProfessorName) {
        int count = 0;
        JSONObject jsonObject = new JSONObject();
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
           mSelectOfficeHrs.setString(1, ProfessorName);
           ResultSet rs = mSelectOfficeHrs.executeQuery();
            while (rs.next()) {
                res.add(new RowData(ProfessorName, rs.getString("Building_name"), rs.getInt("RoomN"), rs.getTime("StartT"), rs.getTime("EndT"), rs.getString("DayOfWeek")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jsonObject.put("type","officehours");
        jsonObject.put("response", res);

        return jsonObject;
    }

//    /**
//     * Query the database for a list of all subjects and their IDs
//     *
//     * @return All rows, as an ArrayList
//     */
//    ArrayList<RowData> selectAll() {
//        ArrayList<RowData> res = new ArrayList<RowData>();
//        try {
//            ResultSet rs = mSelectAll.executeQuery();
//            while (rs.next()) {
//                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), null));
//            }
//            rs.close();
//            return res;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * Get all data for a specific row, by ID
//     *
//     * @param id The id of the row being requested
//     *
//     * @return The data for the requested row, or null if the ID was invalid
//     */
//    RowData selectOne(int id) {
//        RowData res = null;
//        try {
//            mSelectOne.setInt(1, id);
//            ResultSet rs = mSelectOne.executeQuery();
//            if (rs.next()) {
//                res = new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return res;
//    }
//
//    /**
//     * Delete a row by ID
//     *
//     * @param id The id of the row to delete
//     *
//     * @return The number of rows that were deleted.  -1 indicates an error.
//     */
//    int deleteRow(int id) {
//        int res = -1;
//        try {
//            mDeleteOne.setInt(1, id);
//            res = mDeleteOne.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return res;
//    }
//
//    /**
//     * Update the message for a row in the database
//     *
//     * @param id The id of the row to update
//     * @param message The new message contents
//     *
//     * @return The number of rows that were updated.  -1 indicates an error.
//     */
//    int updateOne(int id, String message) {
//        int res = -1;
//        try {
//            mUpdateOne.setString(1, message);
//            mUpdateOne.setInt(2, id);
//            res = mUpdateOne.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return res;
//    }
//
//    /**
//     * Create tblData.  If it already exists, this will print an error
//     */
//    void createTable() {
//        try {
//            mCreateTable.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Remove tblData from the database.  If it does not exist, this will print
//     * an error.
//     */
//    void dropTable() {
//        try {
//            mDropTable.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}