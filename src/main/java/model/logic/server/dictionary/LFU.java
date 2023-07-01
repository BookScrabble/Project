package model.logic.server.dictionary;

import java.util.LinkedHashMap;

public class LFU implements CacheReplacementPolicy {
    LinkedHashMap<String, Integer> words;


    /**
     * The LFU function takes in a string and adds it to the LinkedHashMap words.
     * If the word is already in the map, then its value is incremented by 1.
     */
    public LFU(){
        words = new LinkedHashMap<>();
    }


    /**
     * The add function adds a word to the map. If the word is already in the map,
     * it increments its count by 1. Otherwise, it adds a new entry with value 1.
     * @param word word Add a word to the map
     */
    @Override
    public void add(String word) {
        Integer a = words.remove(word);
        if (a != null){
            words.put(word, a+1);
        }
        else{
            words.put(word, 1);
        }
    }


    /**
     * The remove function removes the least frequently used word from the cache.
     * @return The key with the lowest value
     */
    @Override
    public String remove() {
        String[] temp = new String[2];
        words.forEach((key,val) -> {
            if(temp[0] == null){
                temp[0] = key;
                temp[1] = String.valueOf(val);
            }
            else{
                if(Integer.parseInt(temp[1]) > val){
                    temp[0] = key;
                    temp[1] = String.valueOf(val);
                }
            }
        });
        words.remove(temp[0]);
        return temp[0];
    }
}