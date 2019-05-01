package capstoneproject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;

import org.json.simple.JSONObject;


public class SpeechRecognizerMain {
	
	// Necessary
	private LiveSpeechRecognizer recognizer;
	
	// Logger
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * This String contains the Result that is coming back from SpeechRecognizer
	 */
	private String speechRecognitionResult;
	
	//-----------------Lock Variables-----------------------------
	
	/**
	 * This variable is used to ignore the results of speech recognition cause actually it can't be stopped...
	 * 
	 * <br>
	 * Check this link for more information: <a href=
	 * "https://sourceforge.net/p/cmusphinx/discussion/sphinx4/thread/3875fc39/">https://sourceforge.net/p/cmusphinx/discussion/sphinx4/thread/3875fc39/</a>
	 */
	private boolean ignoreSpeechRecognitionResults = false;
	
	/**
	 * Checks if the speech recognise is already running
	 */
	private boolean speechRecognizerThreadRunning = false;
	
	/**
	 * Checks if the resources Thread is already running
	 */
	private boolean resourcesThreadRunning;

	//Flags
	private boolean wakeFlag;
	private boolean stopFlag;
	private boolean newsFlag;
	private boolean timerFlag;

	//News looping index
	private int newsIndex;

	//Json Object
	private JSONObject queryResponse;
	
	//Class declarations
	private StanfordNLP nl;
	private TextToSpeech tts;

	//Timer vars
	private Timer timer;
	private TimerTask task;
	
	/**
	 * This executor service is used in order the playerState events to be executed in an order
	 */
	private ExecutorService eventsExecutorService = Executors.newFixedThreadPool(2);
	
	//------------------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public SpeechRecognizerMain() {
		
		// Loading Message
		logger.log(Level.INFO, "Loading Speech Recognizer...\n");
		
		// Configuration
		Configuration configuration = new Configuration();

		//Construct needed classes for pipeline
		nl = new StanfordNLP();
		tts = new TextToSpeech();

		// Set initial speech recognition result to blank
		speechRecognitionResult = "";

		//Set wake word and stop word to false
		wakeFlag = false;
		stopFlag = false;
		newsFlag = false;
		timerFlag = false;

		//news index
		newsIndex = 0;

		//initialize query response
		queryResponse = new JSONObject();

		//initialize timer variables
		timer = new Timer();
		
		// Load model from the jar
		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		
		//====================================================================================
		//=====================READ THIS!!!===============================================
		//Uncomment this line of code if you want the recognizer to recognize every word of the language 
		//you are using , here it is English for example	
		//====================================================================================
		//configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
		
		//====================================================================================
		//=====================READ THIS!!!===============================================
		//If you don't want to use a grammar file comment below 3 lines and uncomment the above line for language model	
		//====================================================================================
		
		// Grammar
		configuration.setGrammarPath("./resources/grammars");
		configuration.setGrammarName("grammar");
		configuration.setUseGrammar(true);
		
		try {
			recognizer = new LiveSpeechRecognizer(configuration);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, null, ex);
		}
		
		// Start recognition process pruning previously cached data.
		// recognizer.startRecognition(true);
		
		//Check if needed resources are available
		//startResourcesThread();
		//Start speech recognition thread
		//startSpeechRecognition();
	}
	
	//-----------------------------------------------------------------------------------------------
	
	/**
	 * Starts the Speech Recognition Thread
	 */
	public synchronized void startSpeechRecognition() {
		
		//Check lock
		if (speechRecognizerThreadRunning) {
			logger.log(Level.INFO, "Speech Recognition Thread already running...\n");
		}
		else
			//Submit to ExecutorService
			eventsExecutorService.submit(() -> {
				
				//locks
				speechRecognizerThreadRunning = true;
				ignoreSpeechRecognitionResults = false;
				
				//Start Recognition
				recognizer.startRecognition(true);

				//This runs Derek's and TJ's piece to initiliaze so the program runs faster
				nl.GetInputText("what is the time");
				NLPinfo info = nl.OutputNLPinfo();
				QueryRunner qr = QueryRunner.getInstance();
				JSONObject queryResponse = qr.nlpTransform(info);
				
				//Information			
				logger.log(Level.INFO, "You can start to speak...\n");
				
				try {
					while (speechRecognizerThreadRunning) {
						/*
						 * This method will return when the end of speech is reached. Note that the end pointer will determine the end of speech.
						 */
						SpeechResult speechResult = recognizer.getResult();
						
						//Check if we ignore the speech recognition results
						if (!ignoreSpeechRecognitionResults) {
							
							//Check the result
							if (speechResult == null) {
								logger.log(Level.INFO, "I can't understand what you said.\n");
							}
							else {
								
								//Get the hypothesis
								speechRecognitionResult = speechResult.getHypothesis();

								//Check if input is wake word or stop word
								if (speechRecognitionResult.equals("hey lehigh")) {
									System.out.println("Wake word detected.");
									wakeFlag = true;
								}

								if (speechRecognitionResult.equals("stop listening")) {
									System.out.println("Stop word detected.");
									stopFlag = true;
								}
								
								//Call the appropriate method
								if (wakeFlag || stopFlag) {
									System.out.println("You said: [" + speechRecognitionResult + "]\n");

									makeDecision(speechRecognitionResult, speechResult.getWords());
								}

							}
						} else {
							logger.log(Level.INFO, "Ingoring Speech Recognition Results...");
						}
						
					}
				} catch (Exception ex) {
					logger.log(Level.WARNING, null, ex);
					speechRecognizerThreadRunning = false;
				}
				
				logger.log(Level.INFO, "SpeechThread has exited...");
			});
			eventsExecutorService.shutdown();
	}
	
	/**
	 * Stops ignoring the results of SpeechRecognition
	 */
	public synchronized void stopIgnoreSpeechRecognitionResults() {
		
		//Stop ignoring speech recognition results
		ignoreSpeechRecognitionResults = false;
	}
	
	/**
	 * Ignores the results of SpeechRecognition
	 */
	public synchronized void ignoreSpeechRecognitionResults() {
		
		//Instead of stopping the speech recognition we are ignoring it's results
		ignoreSpeechRecognitionResults = true;
		
	}
	
	//-----------------------------------------------------------------------------------------------
	
	/**
	 * Starting a Thread that checks if the resources needed to the SpeechRecognition library are available
	 */
	public void startResourcesThread() {
		
		//Check lock
		if (resourcesThreadRunning) {
			logger.log(Level.INFO, "Resources Thread already running...\n");
		}
		else
			//Submit to ExecutorService
			eventsExecutorService.submit(() -> {
				try {
					
					//Lock
					resourcesThreadRunning = true;
					
					// Detect if the microphone is available
					while (true) {
						
						//Is the Microphone Available
						if (!AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
							logger.log(Level.INFO, "Microphone is not available.\n");
						}
						
						// Sleep some period
						Thread.sleep(350);
					}
					
				} catch (InterruptedException ex) {
					logger.log(Level.WARNING, null, ex);
					resourcesThreadRunning = false;
				}
			});
	}
	
	/**
	 * Takes a decision based on the given result
	 * 
	 * @param speechWords
	 */
	public void makeDecision(String speech , List<WordResult> speechWords) {

		if (speech.equalsIgnoreCase("stop listening")) {
			speechRecognizerThreadRunning = false;
		}

		else if (speech.equalsIgnoreCase("<unk>")) {
			tts.speak("Im sorry I did not understand the question, can you repeat it?", 2, false, true);
		}

		else if (speech.equalsIgnoreCase("hey lehigh")) {
			//do nothing
		}

		else if (speech.equalsIgnoreCase("set a timer")) {
			tts.speak("How many seconds do you want to set the timer for?", 2, false, true);
			timerFlag = true;
		}

		else if (timerFlag) {
			int val = 0;
			if (speech.equalsIgnoreCase("one"))
				val = 1;
			else if (speech.equalsIgnoreCase("two"))
				val = 2;
			else if (speech.equalsIgnoreCase("three"))
				val = 3;
			else if (speech.equalsIgnoreCase("four"))
				val = 4;
			else if (speech.equalsIgnoreCase("five"))
				val = 5;
			else if (speech.equalsIgnoreCase("six"))
				val = 6;
			else if (speech.equalsIgnoreCase("seven"))
				val = 7;
			else if (speech.equalsIgnoreCase("eight"))
				val = 8;
			else if (speech.equalsIgnoreCase("nine"))
				val = 9;
			else if (speech.equalsIgnoreCase("ten"))
				val = 10;
			setTimer();
			timer.schedule(task, val*1000);
			timerFlag = false;
			wakeFlag = false;
			tts.speak("Okay, your timer is set.", 2, false, true);
		}

		else {
			if (newsFlag) {
				if (speech.equalsIgnoreCase("next headline")) {
					String[] responseArray = (String[]) queryResponse.get("response");
					if (newsIndex < 5)
						newsIndex++;
					tts.speak(responseArray[newsIndex], 2, false, true);
					tts.speak("Would you like to hear more, or hear the next headline?", 2, false, true);
				}
				else if (speech.equalsIgnoreCase("more")) {
					String[] responseArray = (String[]) queryResponse.get("description");
					tts.speak(responseArray[newsIndex], 2, false, true);
					tts.speak("Would you like to stop, or hear the next headline?", 2, false, true);
				}
				else if (speech.equalsIgnoreCase("stop")) {
					newsIndex = 0;
					newsFlag = false;
				}
				return;
			}

			nl.GetInputText(speech);

			//NLPinfo info = nl.OutputNLPinfo();
			NLPinfo info = new NLPinfo();
			info.Query = NLPinfo.Queries.OHours;
			info.RelevantInfo = "Montella";
			//System.out.println(info1.getQuery());
	
			QueryRunner qr = QueryRunner.getInstance();
	
			queryResponse = qr.nlpTransform(info);
			//System.out.println(queryResponse);

			tts.speak(tts.cannedResponse(queryResponse), 2, false, true);

			wakeFlag = false;

			if (speech.equalsIgnoreCase("brief me the news")) {
				newsFlag = true;
				tts.speak("Would you like to hear more, or hear the next headline?", 2, false, true);
				wakeFlag = true;
			}
		}
	}
	
	public boolean getIgnoreSpeechRecognitionResults() {
		return ignoreSpeechRecognitionResults;
	}
	
	public boolean getSpeechRecognizerThreadRunning() {
		return speechRecognizerThreadRunning;
	}
	
	public String getSpeechRecognitionResult() {
		return speechRecognitionResult;
	}

	public void setTimer() {
		task = new TimerTask() {
			public void run() {
				tts.speak("Time is up.", 2, false, true);
			}
		};
	}
}