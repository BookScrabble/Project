package model.Logic;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary {
    private final CacheManager LRU = new CacheManager(400, new LRU());
    private final CacheManager LFU = new CacheManager(100, new LFU());
    private final BloomFilter bloomFilter = new BloomFilter(256,"MD5","SHA1");
    private final ArrayList<String> fileNames = new ArrayList<>();

    public Dictionary(String... fileNames) {
        for (String fileName : fileNames) {
            addWordsToBloomFilter(fileName);
            this.fileNames.add(fileName);
        }
    }

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
