package test;

import model.Logic.MainTrain3;
import test.testBackend.*;

public class TestMain {
    public static void main(String[] args) {
        //MileStone 1
        TestBag.run();
        TestBoard.run();

        //MileStone 2
        TestLRU.run();
        TestLFU.run();
        TestCacheManager.run();
        TestBloomFilter.run();
        TestIOSearcher.run();
        TestDictionary.run();

        //MileStone 3
        if(MainTrain3.testServer()) {
            MainTrain3.testDM();
            MainTrain3.testBSCH();
        }

        System.out.println("done");
    }
}