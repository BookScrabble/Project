package model.logic.server.dictionary;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IOSearcher {

    /**
     * Searches for a specific word in one or more files.
     * @param word the word to search for
     * @param fileNames an array of file names to search in
     * @return true if the word is found in at least one of the files, false otherwise
     * @throws RuntimeException if any of the files cannot be found
     */
    public static boolean search(String word, String... fileNames) {
        for (String fileName : fileNames) {
            try {
                Scanner textScanner = new Scanner(new File(fileName));
                while (textScanner.hasNextLine()) {
                    String line = textScanner.nextLine();
                    for(String w : line.split("[\\s,?!.]+")) {
                        if (w.equals(word))
                            return true;
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("File was not found");
            }
        }
        return false;
    }
}
