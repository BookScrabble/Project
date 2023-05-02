package model.Logic;

import java.util.HashSet;
import java.util.Objects;

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

    public CacheManager(int size, CacheReplacementPolicy crp) {
        this.size = size;
        this.crp = crp;
    }

    public boolean query(String word) {
        return wordsCache.contains(word);
    }

    public void add (String word) {
        if (wordsCache.size() >= this.size) {
            wordsCache.remove(crp.remove());
        }
        wordsCache.add(word);
        crp.add(word);
    }
}
