package Model.Logic;


import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {
    private static DictionaryManager single_instance = null;
    private final Map<String, Dictionary> map = new HashMap<>();

    private DictionaryManager() {
    }

    public static DictionaryManager get() {
        if (single_instance == null)
            single_instance = new DictionaryManager();
        return single_instance;
    }

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
