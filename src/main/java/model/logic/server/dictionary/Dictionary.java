package model.logic.server.dictionary;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary {
    private final CacheManager LRU = new CacheManager(400, new LRU());
    private final CacheManager LFU = new CacheManager(100, new LFU());
    private final BloomFilter bloomFilter = new BloomFilter(8192,"MD5","SHA1","MD2","SHA256","SHA512");
    private final ArrayList<String> fileNames = new ArrayList<>();

    /**
     Creates a new instance of the Dictionary class and adds the words from the specified files to the Bloom filter.
     @param fileNames the names of the files to be added to the
     */
    public Dictionary(String... fileNames) {
        for (String fileName : fileNames) {
            addWordsToBloomFilter(fileName);
            this.fileNames.add(fileName);
        }
    }

    /**
     Reads a file and adds all the words to the Bloom filter.
     @param fileName the name of the file to be added to the Bloom filter.
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
     Queries the dictionary to check whether it contains the specified word.
     @param word the word to be queried.
     @return true if the word is in the dictionary, false otherwise.
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
     * Checks whether the specified word is present in any of the files in the dictionary.
     * @param word the word to be searched in the files.
     * @return true if the word is found, false otherwise.
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
