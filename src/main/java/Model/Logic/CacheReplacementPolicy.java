package Model.Logic;

public interface CacheReplacementPolicy{
	void add(String word);
	String remove(); 
}
