package test.testBackend;


import model.logic.server.dictionary.CacheReplacementPolicy;
import model.logic.server.dictionary.LRU;

public class TestLRU {

    public static void run() {
        testLRU();
    }

    private static void testLRU() {
        CacheReplacementPolicy lru=new LRU();
        lru.add("a");
        lru.add("b");
        lru.add("c");
        // LRU - a - b - c - MRU
        lru.add("a");
        // LRU - b - c - a - MRU
        if(!lru.remove().equals("b"))
            System.out.println("wrong implementation for LRU (-10)");
        // LRU - c - a - MRU
        lru.add("c");
        // LRU - a - c - MRU
        lru.add("d");
        // LRU - a - c - d - MRU
        if(!lru.remove().equals("a"))
            System.out.println("wrong implementation for LRU (-10)");
        // LRU - c - d - MRU
        lru.add("b");
        // LRU - c - d - b - MRU
        lru.add("c");
        // LRU - d - b - c - MRU
        if(!lru.remove().equals("d"))
            System.out.println("wrong implementation for LRU (-10)");
        // LRU - b - c - MRU
        if(!lru.remove().equals("b"))
            System.out.println("wrong implementation for LRU (-10)");
        // LRU - c - MRU
        if(!lru.remove().equals("c"))
            System.out.println("wrong implementation for LRU (-10)");
        // LRU - MRU

        System.out.println("LRU works");
    }
}
