package model.logic.server.dictionary;


import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {
    private static DictionaryManager single_instance = null;
    private final Map<String, Dictionary> map = new HashMap<>();


    /**
     * The DictionaryManager function is a singleton class that manages the dictionary.
     * It has two functions: loadDictionary and getInstance.
     * The loadDictionary function loads the dictionary from a file into an ArrayList of Strings,
     * which is then used to create a HashSet of Strings for faster lookup times.
     * The getInstance function returns an instance of DictionaryManager if one does not already exist, or it returns the existing instance if one does exist.
     */
    private DictionaryManager() {
    }


    /**
     * The get function is a static function that returns the singleton instance of the DictionaryManager class.
     * If no instance exists, it creates one and then returns it.
     * @return The singleton instance of the dictionary manager
     */
    public static DictionaryManager get() {
        if (single_instance == null)
            single_instance = new DictionaryManager();
        return single_instance;
    }


    /**
     * The query function takes in a String array of arguments and returns a boolean.
     * The last argument is the word to be searched for, while the rest are dictionaries
     * that will be searched through. If any of the dictionaries contain the word, then
     * true is returned; otherwise false is returned. This function also adds new entries
     * to map if they do not already exist there (i.e., if an entry does not exist in map,
     * it will create one).
     * @param args args Pass in a variable number of arguments
     * @return True if the word is found in any of the dictionaries,
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
     * The challenge function takes in a String array of words and returns true if any of the words
     * are anagrams of the last word. It does this by iterating through each word in the array,
     * excluding the last one, and checking to see if it is an anagram using its challenge function.
     * @param args args Pass in a variable number of arguments
     * @return True if any of the words in args are a valid
     */
    public boolean challenge(String... args) {
        String word = args[args.length-1];

        for (int i = 0; i < args.length-1; i++) {
            if (map.get(args[i]).challenge(word))
                return true;
        }
        return false;
    }

    /**
     * The getSize function returns the size of the map.
     * @return The size of the map
     */
    public int getSize() {
        return map.size();
    }
}
