package Model.Logic;


import java.io.*;

public class BookScrabbleHandler implements ClientHandler {
    public BookScrabbleHandler() {
    }

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
