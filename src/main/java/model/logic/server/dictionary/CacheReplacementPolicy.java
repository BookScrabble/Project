package model.logic.server.dictionary;

public interface CacheReplacementPolicy{
	void add(String word);
	String remove(); 
}
