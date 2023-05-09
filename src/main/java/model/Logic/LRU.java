package model.Logic;

import java.util.LinkedHashSet;

public class LRU implements CacheReplacementPolicy {
    LinkedHashSet<String> words;

    /**
     * Constructs a new LRU cache object with an empty word set.
     */
    public LRU(){
        words = new LinkedHashSet<String>();
    }

    /**
     * Adds a new word to the cache
     * @param word the word to add to the cache
     */
    @Override
    public void add(String word) {
        words.remove(word);
        words.add(word);
    }

    /**
     Removes and returns the least recently used word from the cache. This is
     always the first element in the set.
     @return the least recently used word in the cache
     */
    @Override
    public String remove() {
        String word = words.iterator().next();
        words.remove(word);
        return word;
    }
}