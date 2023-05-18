package test.testBackend;

import model.logic.server.MyServer;
import model.logic.server.dictionary.BookScrabbleHandler;

public class TestServerArchitecture {
    public static void main(String[] args) {
        MyServer mainServer = new MyServer(25565, new BookScrabbleHandler());

    }
}
