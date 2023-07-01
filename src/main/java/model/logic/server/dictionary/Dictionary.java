package model.logic.server.dictionary;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary {
    private final CacheManager LRU = new CacheManager(400, new LRU());
    private final CacheManager LFU = new CacheManager(100, new LFU());
    private final BloomFilter bloomFilter = new BloomFilter(32768,"MD5","SHA1","MD2","SHA256","SHA512");
    private final ArrayList<String> fileNames = new ArrayList<>();


    /**
     * The Dictionary function takes in a list of file names and adds the words from each file to the bloom filter.
     * @param fileNames fileNames Allow the user to pass in an arbitrary number of file names
     */
    public Dictionary(String... fileNames) {
        for (String fileName : fileNames) {
            addWordsToBloomFilter(fileName);
            this.fileNames.add(fileName);
        }
    }


    /**
     * The addWordsToBloomFilter function takes in a file name and adds all the words from that file to the bloom filter.
     * @param fileName fileName Specify the text file that is being read
     */
    private void addWordsToBloomFilter(String fileName) {
        try {
            Scanner textScanner = new Scanner(new File(fileName));
            while (textScanner.hasNextLine()) {
                String line = textScanner.nextLine();
                for (String word : line.split("\\W+")) {
                    bloomFilter.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File was not found");
        }
    }


    /**
     * The query function takes in a string and returns true if the word is in the dictionary, false otherwise.
     * @param word word Query the bloom filter
     * @return True if the word is in the data structure
     */
    public boolean query(String word) {
        if (LRU.query(word))
            return true;
        if (LFU.query(word))
            return false;
        if (bloomFilter.contains(word)) {
            LRU.add(word);
            return true;
        }
        else {
            LFU.add(word);
            return false;
        }
    }


    /**
     * The challenge function takes in a word and checks if it is in the dictionary.
     * If it is, then the word will be added to LRU cache. Otherwise, it will be added to LFU cache.
     * @param word word Search for the word in the files
     * @return True if the word is found in any of the files, and false otherwise
     */
    public boolean challenge(String word) {
        for (String fileName : fileNames) {
            if (IOSearcher.search(word, fileName)) {
                LRU.add(word);
                return true;
            }
        }
        LFU.add(word);
        return false;
    }
}
