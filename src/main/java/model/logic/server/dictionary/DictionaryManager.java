package model.logic.server.dictionary;


import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {
    private static DictionaryManager single_instance = null;
    private final Map<String, Dictionary> map = new HashMap<>();

    /**
     * The DictionaryManager class provides a Singleton object that manages multiple dictionaries.
     */
    private DictionaryManager() {
    }

    /**
     Provides access to the single instance of the DictionaryManager class.
     If the instance does not exist yet, it is created.
     @return the single instance of the DictionaryManager
     */
    public static DictionaryManager get() {
        if (single_instance == null)
            single_instance = new DictionaryManager();
        return single_instance;
    }

    /**
     Queries multiple dictionaries for the presence of a given word.
     @param args a variable number of String arguments representing the names of dictionaries to query and the word
     to search for.
     @return true if the word is found in at least one dictionary, false otherwise
     */
    public boolean query(String... args) {
        String word = args[args.length-1];
        boolean found = false;

        for (int i = 0; i < args.length-1; i++) {
            if (!map.containsKey(args[i])) {
                map.put(args[i], new Dictionary(args[i]));
            }
            Dictionary dictionary = map.get(args[i]);
            if (dictionary.query(word))
                found = true;
        }
        return found;
    }

    /**
     * Searches multiple dictionaries for the given word within their associated files.
     @param args a variable number of String arguments representing the names of dictionaries to search and the word
     to search for.
     @return true if the word is found in any of the files associated with at least one dictionary, false otherwise
     */
    public boolean challenge(String... args) {
        String word = args[args.length-1];

        for (int i = 0; i < args.length-1; i++) {
            if (map.get(args[i]).challenge(word))
                return true;
        }
        return false;
    }

    public int getSize() {
        return map.size();
    }
}
