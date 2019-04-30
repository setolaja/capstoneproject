package capstoneproject.DatabaseFiles;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;

//This class just sets up a connection to our database.
//The only point of this class is so we don't have to repeat the following code block over and over.
public class DataBaseConnection {




        public Connection getConnection()
        {
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = null;
                conn = DriverManager.getConnection("jdbc:mysql://localhost/lehigh","root", "");
                System.out.print("Database is connected !");
                return conn;
            }
            catch(Exception e)
            {
                System.out.print("Do not connect to DB - Error:"+e);
            }


            return null;
        }


    }



