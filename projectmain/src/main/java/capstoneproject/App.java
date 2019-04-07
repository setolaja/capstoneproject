package capstoneproject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import java.util.Arrays;
import java.util.List;

import marytts.signalproc.effects.JetPilotEffect;
import marytts.signalproc.effects.LpcWhisperiserEffect;
import marytts.signalproc.effects.RobotiserEffect;
import marytts.signalproc.effects.StadiumEffect;
import marytts.signalproc.effects.VocalTractLinearScalerEffect;
import marytts.signalproc.effects.VolumeEffect;
import org.json.simple.JSONObject;

public class App 
{
    public static void main( String[] args )
    {
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
        //System.out.println("Working Dir: " + System.getProperty("user.dir"));
        SpeechRecognizerMain main = new SpeechRecognizerMain();
        main.startResourcesThread();
        main.startSpeechRecognition();
        System.out.println("Output: " + main.getSpeechRecognitionResult());
        //QueryRunner qr = QueryRunner.getInstance();

        //JSONObject queryResponse = qr.nlpTransform("weather");
        //TextToSpeech tts = new TextToSpeech();
        //tts.speak(tts.cannedResponse(queryResponse),2,false,true);

    }



}
