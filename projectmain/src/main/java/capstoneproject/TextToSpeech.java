package capstoneproject;

import java.io.IOException;
import java.sql.Time;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.sound.sampled.AudioInputStream;
import javax.xml.crypto.Data;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;
import marytts.signalproc.effects.AudioEffect;
import marytts.signalproc.effects.AudioEffects;


public class TextToSpeech {

    private AudioPlayer tts;
    private MaryInterface marytts;

    /**
     * Constructor
     */
    public TextToSpeech() {
        try {
            marytts = new LocalMaryInterface();

        } catch (MaryConfigurationException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        getAvailableVoices().stream().forEach(voice -> System.out.println("Voice: " + voice));
        setVoice("cmu-slt-hsmm");
        

    }

    //----------------------GENERAL METHODS---------------------------------------------------//

    /**
     * Transform text to speech
     *
     * @param text
     *            The text that will be transformed to speech
     * @param daemon
     *            <br>
     *            <b>True</b> The thread that will start the text to speech Player will be a daemon Thread <br>
     *            <b>False</b> The thread that will start the text to speech Player will be a normal non daemon Thread
     * @param join
     *            <br>
     *            <b>True</b> The current Thread calling this method will wait(blocked) until the Thread which is playing the Speech finish <br>
     *            <b>False</b> The current Thread calling this method will continue freely after calling this method
     */
    public void speak(String text , float gainValue , boolean daemon , boolean join) {

        // Stop the previous player
        stopSpeaking();

        try (AudioInputStream audio = marytts.generateAudio(text)) {

            // Player is a thread(threads can only run one time) so it can be
            // used has to be initiated every time
            tts = new AudioPlayer();
            tts.setAudio(audio);
            tts.setGain(gainValue);
            tts.setDaemon(daemon);
            tts.start();
            if (join)
                tts.join();

        } catch (SynthesisException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error saying phrase.", ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "IO Exception", ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Interrupted ", ex);
            tts.interrupt();
        }
    }

    /**
     * Stop the MaryTTS from Speaking
     */
    public void stopSpeaking() {
        // Stop the previous player
        if (tts != null)
            tts.cancel();
    }

    //----------------------GETTERS---------------------------------------------------//

    /**
     * Available voices in String representation
     *
     * @return The available voices for MaryTTS
     */
    public Collection<Voice> getAvailableVoices() {
        return Voice.getAvailableVoices();
    }

    /**
     * @return the marytts
     */
    public MaryInterface getMarytts() {
        return marytts;
    }

    /**
     * Return a list of available audio effects for MaryTTS
     *
     * @return
     */
    public List<AudioEffect> getAudioEffects() {
        return StreamSupport.stream(AudioEffects.getEffects().spliterator(), false).collect(Collectors.toList());
    }

    //----------------------SETTERS---------------------------------------------------//

    /**
     * Change the default voice of the MaryTTS
     *
     * @param voice
     */
    public void setVoice(String voice) {
        marytts.setVoice(voice);
    }

    public static String cannedResponse(JSONObject query){

        String response = "";
        if(query == null){
            response += "I'm sorry, im not sure how to answer that.";
            return response;
        }
        switch (query.get("type").toString()){

            /**
             * If the query requested is for weather
             */
            case "weather": {
                JSONObject resp = (JSONObject) query.get("response");
                response = "Right now in " + resp.get("location") + ", it is " + resp.get("temp") + " degrees and there are " + resp.get("weather") + ".";
                break;
            }

            /**
             * If the query requested is for flip a coin
             */
            case "coin": {
                response = query.get("response") + ".";
                break;
            }

            /**
             * If the query requested is for calculator
             */
            case "calc": {
                response = query.get("query") + "is" + query.get("response").toString() + ".";
                break;
            }

            /**
             * If the query requested is for startup
             */
            case "startup": {
                response = query.get("response") + ".";
                break;
            }

            /**
             * If the query requested is for what time is it
             */
            case "time": {
                response = "The time is " + query.get("response") + ".";
                break;
            }

            /**
             * If the query requested is for brief me the news
             */
            case "news": {
                String[] responseArray = (String[]) query.get("response");
                response = responseArray[0] + ".";
                break;
            }

            /**
             * If the query requested is for random number
             */
            case "random": {
                response = query.get("response").toString() + ".";
                break;
            }

            /**
             * If the query requested is for a professors office hours
             */
            case "officehours": {
                Time startT;
                Time endT;
                String dayOfWeek;
                ArrayList<Database.RowData> aList = (ArrayList<Database.RowData>) query.get("response");
                /**
                 * Technical debt: building name and room number take from the first row only, so if they change it would let the user know
                 */

                response += "Office hours for " + aList.get(0).professorName + " are in " + aList.get(0).buildingName +
                " in room number " + aList.get(0).roomNumber;
                for(int i = 0; i < aList.size(); i++){
                    startT = aList.get(i).startT;
                    endT = aList.get(i).endT;
                    dayOfWeek = aList.get(i).dayOfWeek;
                    //Add data to response
                    if(i == aList.size() -1){
                        response += " on " + dayOfWeek + "'s from " + startT.toString().substring(0,5) + " to " + endT.toString().substring(0,5) + ".";
                    }
                    else {
                        response += " on " + dayOfWeek + "'s from " + startT.toString().substring(0,5) + " to " + endT.toString().substring(0,5) + ", and";
                    }
                }

                return response;
            }

            case "buildinghours": {
                ArrayList<Database.BuildingData> aList = (ArrayList<Database.BuildingData>) query.get("response");
                response += "The hours for " + aList.get(0).buildingName + " are from " + aList.get(0).startT.toString().substring(0,5) + " to " + aList.get(0).endT.toString().substring(0,5);
                response += ".";
            }

            /**
             * If the query requested hasnt been implemented
             */
            default: {
                response = "I'm sorry, im not sure how to answer that.";
                break;
            }

        }
        System.out.println(response);
        return response;

    }

}