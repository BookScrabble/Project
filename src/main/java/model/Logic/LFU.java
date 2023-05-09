package model.Logic;

import java.util.LinkedHashMap;

public class LFU implements CacheReplacementPolicy {
    LinkedHashMap<String, Integer> words;

    /**
     * The LFU class is an implementation of the Least Frequently Used cache.
     * Constructor for the LFU class.
     * Initializes the words LinkedHashMap.
     */
    public LFU(){
        words = new LinkedHashMap<String, Integer>();
    }

    /**
     * Adds a word to the cache.
     * @param word the word to add to the cache
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
     * Removes the least frequently used word from the cache based on its frequency count.
     * @return the least frequently used word from the cache
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