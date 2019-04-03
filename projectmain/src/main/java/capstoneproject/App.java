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
        //System.out.println("Working Dir: " + System.getProperty("user.dir"));
        new SpeechRecognizerMain();

        //testTextToSpeech();
    }

    public static void testTextToSpeech() {
        //Create TextToSpeech
        TextToSpeech tts = new TextToSpeech();

        //=========================================================================
        //======================= Print available AUDIO EFFECTS ====================
        //=========================================================================

        //Print all the available audio effects
        tts.getAudioEffects().stream().forEach(audioEffect -> {
            System.out.println("-----Name-----");
            System.out.println(audioEffect.getName());
            System.out.println("-----Examples-----");
            System.out.println(audioEffect.getExampleParameters());
            System.out.println("-----Help Text------");
            System.out.println(audioEffect.getHelpText() + "\n\n");

        });

        //=========================================================================
        //========================= Print available voices =========================
        //=========================================================================

        //Print all the available voices
        tts.getAvailableVoices().stream().forEach(voice -> System.out.println("Voice: " + voice));
    }

}
