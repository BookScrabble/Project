package model.logic.server.dictionary;

import model.logic.client.ClientHandler;
import java.io.*;

public class BookScrabbleHandler implements ClientHandler {

    /**
     * The BookScrabbleHandler function takes in a Book object and returns an integer value.
     * The function calculates the scrabble score of the book by adding up all the individual word scores.
     */
    public BookScrabbleHandler() {
    }

    /**
     * The handleClient function is called by the server when a client connects to it.
     * The function takes two arguments: an InputStream and an OutputStream, which are used to communicate with the client.
     * The function reads from the input stream until it receives a line of text that begins with either 'Q' or 'C'.
     * If the line begins with 'Q', then this indicates that we should query our dictionary for whether a word exists in it.
     * If so, we write &quot;true&quot; back to the output stream; otherwise, we write &quot;false&quot;.  Note that if there is any error reading from/
     * @param inFromClient inFromClient Read the input from the client
     * @param outToClient outToClient Write the response to the client
     */
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        DictionaryManager dictionaryManager = DictionaryManager.get();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inFromClient));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outToClient));
            String line = br.readLine();
            if(line.equals("connectionCheck")) return;
            String filesAndWord = line.substring(2);
            String[] dictionariesAndWord = filesAndWord.split(",");
            for(int i = 0; i < dictionariesAndWord.length-1; i++){
                dictionariesAndWord[i] = "src/main/resources/Dictionaries/" + dictionariesAndWord[i];
            }
            if (line.charAt(0) == 'Q') {
                if (dictionaryManager.query(dictionariesAndWord))
                    bw.write("true\n");
                else
                    bw.write("false\n");
            }
            if (line.charAt(0) == 'C') {
                if (dictionaryManager.challenge(dictionariesAndWord))
                    bw.write("true\n");
                else
                    bw.write("false\n");
            }
            bw.close();
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The close function is used to close the connection between the client and server.
     * This function is called when a user logs out of their account or closes the program.
     */
    @Override
    public void close() {

    }
}
