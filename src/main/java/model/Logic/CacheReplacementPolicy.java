package model.Logic;

public interface CacheReplacementPolicy{
	void add(String word);
	String remove(); 
}
