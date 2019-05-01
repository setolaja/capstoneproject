package capstoneproject;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;
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

        for (int i = 0; i < document.sentences().get(0).posTags().size(); i++)
        {
            //System.out.println(document.sentences().get(0).posTags().get(i));
        }

        Boolean calc = isCalc(document.sentences().get(0).posTags());
        //just most redumentary until nlp is back online
        for (int i = 0; i < SentanceSize; i++) {

            if (Sentance[i].contains("Weather") || Sentance[i].contains("weather")) {
                retval.Query = NLPinfo.Queries.Weather;
                return retval;
            } else if (Sentance[i].contains("Coin") || Sentance[i].contains("coin")) {
                retval.Query = NLPinfo.Queries.Coin;
                return retval;
            } else if (Sentance[i].contains("Time") || Sentance[i].contains("time")) {
                retval.Query = NLPinfo.Queries.Time;
                return retval;
            } else if (Sentance[i].contains("Random") || Sentance[i].contains("random")) {
                retval.Query = NLPinfo.Queries.random;
                return retval;
            }
            else if (Sentance[i].contains("news") || Sentance[i].contains("News")) {
                retval.Query = NLPinfo.Queries.News;
                return retval;
            }
            else if (Sentance[i].contains("Professor") || Sentance[i].contains("professor"))
            {
                retval.Query = NLPinfo.Queries.OHours;
                System.out.println("1" + retval.Query.toString());
                for(int j = 0; j < SentanceSize; j++)
                {
                    System.out.println("2" + Sentance[j]);
                    //System.out.println(document.sentences().get(0).posTags().get(j));
                    if(document.sentences().get(0).posTags().get(j).toString().equals("NNP"))
                    {
                        //System.out.println("2" + Sentance[j]);
                        retval.RelevantInfo = Sentance[j].substring(0,Sentance[j].length()-2);
                    }
                }
                return retval;
            }

        }



        //NLPinfo is the class that holds all the info to be sent to TJ
        //It will grow as we handle more and more complex queries
        retval.Query = NLPinfo.Queries.NULL;
        return retval;
    }

    public static boolean isCalc(List<String> POS)
    {

        return true;
    }

    public static void main(String[] args)
    {
        StanfordNLP N1 = new StanfordNLP();
        N1.GetInputText("when does packard lab close");
        NLPinfo info = N1.OutputNLPinfo();
        System.out.println(info.getQuery().toString() + info.RelevantInfo);



    }
}