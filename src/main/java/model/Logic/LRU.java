package model.Logic;

import java.util.LinkedHashSet;

public class LRU implements CacheReplacementPolicy {
    LinkedHashSet<String> words;
    public LRU(){
        words = new LinkedHashSet<String>();
    }
    @Override
    public void add(String word) {
        words.remove(word);
        words.add(word);
    }

    @Override
    public String remove() {
        String word = words.iterator().next();
        words.remove(word);
        return word;
    }
}