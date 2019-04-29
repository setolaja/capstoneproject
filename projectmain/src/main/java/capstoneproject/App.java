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
        SpeechRecognizerMain main = new SpeechRecognizerMain();
        main.startResourcesThread();
        main.startSpeechRecognition();

        
    }



}
