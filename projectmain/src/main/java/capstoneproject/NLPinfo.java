package capstoneproject;
public class NLPinfo {

    //our 10 Querries
    enum Queries {
        Weather,
        Coin,
        OHours,
        News,
        Time,
        Schedule,
        LNews,
        Levent,
        Calc,
        Bclose,
        random,
        NULL
    }

    public Queries Query;
    public String RelevantInfo;

    public Queries getQuery()
    {
        return Query;
    }

    private void setQuery(Queries Q1)
    {
        Query = Q1;
    }


    //Testing for TJ skips the NLP and generates the expected output for the query, What Time is It?
    public void setQuerytoWeather()
    {
        Query = Queries.Weather;
    }

    public void setQueryToTime()
    {
        Query = Queries.Time;
    }

    public void setQueryToCoin()
    {
        Query = Queries.Coin;
    }
}