package capstoneproject;

import org.json.simple.JSONObject;

import java.util.HashSet;

public abstract class AbstractRunner {
    HashSet<String> types;

    public boolean contains(String st){
        return types.contains(st);
    }

    public abstract JSONObject response(String input);
}
