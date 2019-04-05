package capstoneproject;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

public class StanfordNLP {

    public String inputText;

    public StanfordNLP() {
    }

    public void GetInputText(String input) {
        inputText = input;
    }

    public NLPinfo OutputNLPinfo() {
        //weather
        //time
        //coin
        //random


        Properties props = new Properties();
        // set the list of annotators to run
        // most important part of this constructur!
        props.setProperty("annotators", "tokenize,ssplit,pos,depparse");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // create a document object
        CoreDocument document = new CoreDocument(inputText);
        // annnotate the document
        pipeline.annotate(document);

        NLPinfo retval = new NLPinfo();


        //The size of the sentance
        int SentanceSize = document.tokens().size();

        //There should be only one sentance, this just gets all the tokens in the "docuement"
        String[] Sentance = new String[SentanceSize];
        CoreLabel token;
        for (int i = 0; i < SentanceSize; i++) {
            token = document.tokens().get(i);
            Sentance[i] = token.toString();

        }
        //just most redumentary until nlp is back online
        for (int i = 0; i < SentanceSize; i++) {
            if (Sentance[i].contains("Weather") || Sentance[i].contains("weather")) {
                retval.Query = NLPinfo.Queries.Weather;
            } else if (Sentance[i].contains("Coin") || Sentance[i].contains("coin")) {
                retval.Query = NLPinfo.Queries.Coin;
            } else if (Sentance[i].contains("Time") || Sentance[i].contains("time")) {
                retval.Query = NLPinfo.Queries.Time;
            } else if (Sentance[i].contains("Random") || Sentance[i].contains("random")) {
                retval.Query = NLPinfo.Queries.random;
            } else {
                retval.Query = NLPinfo.Queries.NULL;
            }
        }


        //NLPinfo is the class that holds all the info to be sent to TJ
        //It will grow as we handle more and more complex queries

        return retval;
    }
}
