package model.logic.server.dictionary;

import java.util.HashSet;
import java.util.Objects;

public class CacheManager {
    private final int size;
    private final CacheReplacementPolicy crp;
    private final HashSet<String> wordsCache = new HashSet<>();

    /**
     * The equals function is used to compare two objects.
     * @param o o Compare the current object with another object
     * @return True if the words cache are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheManager that = (CacheManager) o;
        return Objects.equals(wordsCache, that.wordsCache);
    }

    /**
     * The hashCode function is used to generate a unique hash code for each instance of the class.
     * This is useful when storing instances in HashMaps or HashSets, as it allows us to quickly
     * determine if two objects are equal by comparing their hash codes. If two objects have the same
     * hash code, they are not guaranteed to be equal (as there may be collisions), but if they do not
     * have the same hash code, then we know that they cannot possibly be equal. Thus, we can avoid having
     * to compare them at all! The default implementation of this function in Java uses each field in an object's
     * @return The hashcode of the words cache object
     */
    @Override
    public int hashCode() {
        return Objects.hash(wordsCache);
    }

    /**
     * The CacheManager function is responsible for managing the cache.
     * It will add a new entry to the cache if it does not exist, or update an existing one.
     * If there is no space in the cache, it will remove an entry according to its replacement policy and then add/update as needed.
     * @param size size Set the size of the cache
     * @param crp crp Determine which cache replacement policy to use
     */
    public CacheManager(int size, CacheReplacementPolicy crp) {
        this.size = size;
        this.crp = crp;
    }

    /**
     * The query function takes in a string and returns true if the word is in the dictionary, false otherwise.
     * @param word word Check if the word is in the cache
     * @return A boolean value indicating if the word is in the trie
     */
    public boolean query(String word) {
        return wordsCache.contains(word);
    }


    /**
     * The add function adds a word to the cache. If the cache is full, it removes
     * the least recently used word from both wordsCache and crp. Then, it adds
     * the new word to both wordsCache and crp.
     * @param word word Add a word to the cache
     */
    public void add (String word) {
        if (wordsCache.size() >= this.size) {
            wordsCache.remove(crp.remove());
        }
        wordsCache.add(word);
        crp.add(word);
    }
}
