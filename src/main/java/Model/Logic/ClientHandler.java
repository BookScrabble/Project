package Model.Logic;

import java.io.InputStream;
import java.io.OutputStream;

public interface ClientHandler {
	void handleClient(InputStream inFromClient, OutputStream outToClient);
	void close();
}
