package test.testBackend;


import model.logic.server.dictionary.Dictionary;

public class TestDictionary {

    public static void run() {
        testDictionary();
    }

    private static void testDictionary() {
        Dictionary d = new Dictionary("text1.txt","text2.txt");
        if(!d.query("is"))
            System.out.println("problem with dictionary in query (-5)");
        if(!d.challenge("lazy"))
            System.out.println("problem with dictionary in challenge (-5)");

        System.out.println("Dictionary works");
    }
}
