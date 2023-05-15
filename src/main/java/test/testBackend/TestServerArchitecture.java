package test.testBackend;

import model.Logic.*;

public class TestServerArchitecture {
    public static void main(String[] args) {
        MyServer mainServer = new MyServer(25565, new BookScrabbleHandler());
        mainServer.start();
        HostServer firstHost = new HostServer("LiorHost", 12345, new GuestHandler());
        HostServer secondHost = new HostServer("IdanHost", 23456, new GuestHandler());
        firstHost.start();
        secondHost.start();
        Client firstClient = new Client("localhost", 12345, "Lior");
        Client secondClient = new Client("localhost", 12345, "Idan");
        Client thirdClient = new Client("localhost", 23456, "Burger");
        Client fourthClient = new Client("localhost", 23456, "Liav");
    }
}
