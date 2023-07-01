package model.logic.server.dictionary;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IOSearcher {


    /**
     * The search function takes a word and an array of file names as parameters.
     * It then searches each file for the given word, returning true if it is found in any of them.
     * @param word word Specify the word that is being searched for
     * @param fileNames fileNames Pass in an array of strings
     * @return A boolean, so it can be used in an if statement
     */
    public static boolean search(String word, String... fileNames) {
        for (String fileName : fileNames) {
            try {
                Scanner textScanner = new Scanner(new File(fileName));
                while (textScanner.hasNextLine()) {
                    String line = textScanner.nextLine();
                    for(String w : line.split("[\"\\s,?!.-]+")) {
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
