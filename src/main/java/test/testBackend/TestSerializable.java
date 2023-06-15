package test.testBackend;

import model.logic.client.Client;
import model.logic.host.GameManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TestSerializable {

    public static void main(String[] args) {
        GameManager gameManager = GameManager.get();

        gameManager.setCalculationServerIp("HI");

        System.out.println("Local model -> " + gameManager.getCalculationServerIp());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            Socket pingCheck = new Socket("localhost", 20000);
            PrintWriter printWriter = new PrintWriter(pingCheck.getOutputStream(), true);
            printWriter.println("ping");
            ObjectInputStream objectInputStream = new ObjectInputStream(pingCheck.getInputStream());
            try {
                GameManager model = (GameManager) objectInputStream.readObject();
                System.out.println("Model received from server (IP) -> " + model.getCalculationServerIp());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            pingCheck.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        Client playerClient = new Client("localhost", 20000, "Lior");

        System.out.println("Main is dead!");

    }
}
