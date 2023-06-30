package model.logic.server.dictionary;

import java.util.HashSet;
import java.util.Objects;
/**
 * The CacheManager class represents a cache manager that stores words in a cache with a specified capacity.
 * It uses a cache replacement policy (CRP) to manage the cache and determine which words to remove when the cache is full.
 */
public class CacheManager {
    private final int size; // cache capacity
    private final CacheReplacementPolicy crp;
    private final HashSet<String> wordsCache = new HashSet<String>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheManager that = (CacheManager) o;
        return Objects.equals(wordsCache, that.wordsCache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordsCache);
    }

    /**
     * Constructs a new CacheManager with the specified cache size and cache replacement policy (CRP).
     * @param size The maximum number of words that the cache can hold.
     * @param crp The cache replacement policy to use for managing the cache.
     */
    public CacheManager(int size, CacheReplacementPolicy crp) {
        this.size = size;
        this.crp = crp;
    }

    /**
     * Checks if the specified word is present in the cache.
     * @param word The word to check.
     * @return true if the word is present in the cache, false otherwise.
     */
    public boolean query(String word) {
        return wordsCache.contains(word);
    }

    /**
     Adds the specified word to the cache. If the cache is full, the least recently used word is removed from the cache.
     The method also updates the cache replacement policy (CRP) by adding the new word and removing the least recently used one.
     @param word the word to be added to the cache.
     */
    public void add (String word) {
        if (wordsCache.size() >= this.size) {
            wordsCache.remove(crp.remove());
        }
        wordsCache.add(word);
        crp.add(word);
    }
}
