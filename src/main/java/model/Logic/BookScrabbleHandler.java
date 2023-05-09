package model.Logic;


import java.io.*;

public class BookScrabbleHandler implements ClientHandler {
    public BookScrabbleHandler() {
    }

    /**
     Handles communication with a client by reading input from the client and writing output to the client
     based on the client's request. The method expects the input stream to contain a line starting with either
     "Q" or "C" followed by a comma-separated list of words. "Q" represents a query request and "C" represents
     a challenge request.
     @param inFromClient the input stream from the client.
     @param outToClient the output stream to the client.
     */
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        DictionaryManager dictionaryManager = DictionaryManager.get();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inFromClient));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outToClient));
            String line = br.readLine();
            String filesAndQuery = line.substring(2);
            String[] clientWords = filesAndQuery.split(",");
            if (line.charAt(0) == 'Q') {
                if (dictionaryManager.query(clientWords))
                    bw.write("true\n");
                else
                    bw.write("false\n");
            }
            if (line.charAt(0) == 'C') {
                if (dictionaryManager.challenge(clientWords))
                    bw.write("true\n");
                else
                    bw.write("false\n");
            }
//            bw.flush();
            bw.close();
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {

    }
}
