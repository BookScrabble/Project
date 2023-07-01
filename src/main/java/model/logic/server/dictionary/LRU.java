package model.logic.server.dictionary;

import java.util.LinkedHashSet;

public class LRU implements CacheReplacementPolicy {
    LinkedHashSet<String> words;


    /**
     * The LRU function takes in a string and adds it to the LinkedHashSet.
     * If the size of the set is greater than 10, then it removes the first element.
     */
    public LRU(){
        words = new LinkedHashSet<>();
    }


    /**
     * The add function adds a word to the list of words.
     * @param word word Add the word to the list of words
     */
    @Override
    public void add(String word) {
        words.remove(word);
        words.add(word);
    }


    /**
     * The remove function removes the first word in the list of words.
     * @return The word that was removed from the words set
     */
    @Override
    public String remove() {
        String word = words.iterator().next();
        words.remove(word);
        return word;
    }
}