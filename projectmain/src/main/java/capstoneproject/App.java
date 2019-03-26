package capstoneproject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        // get the Postgres configuration from the environment
        // Map<String, String> env = System.getenv();
        // String ip = env.get("POSTGRES_IP");
        // String port = env.get("POSTGRES_PORT");
        // String user = env.get("POSTGRES_USER");
        // String pass = env.get("POSTGRES_PASS");

        // Get a fully-configured connection to the database, or exit 
        // immediately
        // Database db = Database.getDatabase(ip, port, user, pass);
        // if (db == null)
        //     return;
        System.out.println("Working Dir: " + System.getProperty("user.dir"));
        new SpeechRecognizerMain();
    }

}
