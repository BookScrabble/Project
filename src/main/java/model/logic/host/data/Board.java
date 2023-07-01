package model.logic.host.data;


import model.logic.host.GameManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Board implements Serializable {
    private static Board single_instance = null;
    private final static int BOARD_HEIGHT = 15;
    private final static int BOARD_WIDTH = 15;
    private final Tile[][] board = new Tile[BOARD_HEIGHT][BOARD_WIDTH];
    private static final HashMap<String, String> boardScores = new HashMap<>();
    private final ArrayList<Word> foundWords = new ArrayList<>();


    /**
     * The Board function initializes the boardScores array to contain all zeros.
     */
    private Board() {
        initBoardScores();
    }

    /**
     * The getBoard function is a static function that returns the single instance of the Board class.
     * This is used to ensure that there are no multiple instances of the board, and thus no confusion
     * about which board we are referring to when we call functions on it.
     * @return A singleton instance of the board class
     */
    public static Board getBoard() {
        if (single_instance == null)
            single_instance = new Board();
        return single_instance;
    }

    /**
     * The initBoardScores function initializes the boardScores HashMap with all the
     * possible positions on a Scrabble board and their corresponding scores.
     */
    private void initBoardScores() {
        //STAR BLOCK
        boardScores.put("0707", "2W");

        //TRIPLE WORD
        boardScores.put("0000", "3W");
        boardScores.put("0700", "3W");
        boardScores.put("0007", "3W");
        boardScores.put("0014", "3W");
        boardScores.put("1400", "3W");
        boardScores.put("1407", "3W");
        boardScores.put("0714", "3W");
        boardScores.put("1414", "3W");

        //DOUBLE WORD
        boardScores.put("0101", "2W");
        boardScores.put("0202", "2W");
        boardScores.put("0303", "2W");
        boardScores.put("0404", "2W");
        boardScores.put("0113", "2W");
        boardScores.put("0212", "2W");
        boardScores.put("0311", "2W");
        boardScores.put("0410", "2W");
        boardScores.put("1301", "2W");
        boardScores.put("1202", "2W");
        boardScores.put("1103", "2W");
        boardScores.put("1004", "2W");
        boardScores.put("1010", "2W");
        boardScores.put("1111", "2W");
        boardScores.put("1212", "2W");
        boardScores.put("1313", "2W");

        //TRIPLE LETTER
        boardScores.put("0501", "3L");
        boardScores.put("0901", "3L");
        boardScores.put("0105", "3L");
        boardScores.put("0505", "3L");
        boardScores.put("0905", "3L");
        boardScores.put("1305", "3L");
        boardScores.put("0109", "3L");
        boardScores.put("0509", "3L");
        boardScores.put("0909", "3L");
        boardScores.put("1309", "3L");
        boardScores.put("0513", "3L");
        boardScores.put("0913", "3L");

        //DOUBLE LETTER
        boardScores.put("0300", "2L");
        boardScores.put("1100", "2L");
        boardScores.put("0602", "2L");
        boardScores.put("0802", "2L");
        boardScores.put("0003", "2L");
        boardScores.put("0703", "2L");
        boardScores.put("1403", "2L");
        boardScores.put("0206", "2L");
        boardScores.put("0606", "2L");
        boardScores.put("0806", "2L");
        boardScores.put("1206", "2L");
        boardScores.put("0307", "2L");
        boardScores.put("1107", "2L");
        boardScores.put("0208", "2L");
        boardScores.put("0608", "2L");
        boardScores.put("0808", "2L");
        boardScores.put("1208", "2L");
        boardScores.put("0011", "2L");
        boardScores.put("0711", "2L");
        boardScores.put("1411", "2L");
        boardScores.put("0612", "2L");
        boardScores.put("0812", "2L");
        boardScores.put("0314", "2L");
        boardScores.put("1114", "2L");
    }


    /**
     * The getScore function takes a Word object as an argument and returns the score of that word.
     * The function iterates through each tile in the word, checking if it is on a double or triple letter square.
     * If so, it multiplies the score of that tile by 2 or 3 respectively. It also checks if any tiles are on double
     * or triple word squares and multiplies the total score by 2 or 3 respectively for each one found. Finally, it adds up all of these scores to get a final value which is returned at the end of this function.
     * @param word word Get the row and column of the word
     * @return The score of the word played
     */
    private int getScore(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        int wordLength = word.getTiles().length;

        int score = 0;
        int wordMultiplier = 1;
        int tileMultiplier = 1;

        for (int i = 0; i < wordLength; i++) {
            String boardRef;
            if (word.isVertical()) {
                boardRef = getBoardRefString(row + i, col);
            }
            else {
                boardRef = getBoardRefString(row, col + i);
            }
            if (Objects.equals(boardScores.get(boardRef), "2W")) {
                wordMultiplier = wordMultiplier * 2;
            }
            if (Objects.equals(boardScores.get(boardRef), "3W")) {
                wordMultiplier = wordMultiplier * 3;
            }
            if (Objects.equals(boardScores.get(boardRef), "2L")) {
                tileMultiplier = 2;
            }
            if (Objects.equals(boardScores.get(boardRef), "3L")) {
                tileMultiplier = 3;
            }
            score += word.getTiles()[i].score * tileMultiplier;
            if (boardRef.equals("0707")) {
                boardScores.remove("0707");
            }
            tileMultiplier = 1;

        }
        score = score * wordMultiplier;
        return score;
    }


    /**
     * The getBoardRefString function takes in a row and column number, and returns the corresponding board reference string.
     * @param r r Represent the row of a cell on the board
     * @param c c Determine the column of the board
     * @return A string with the row and column numbers
     */
    private String getBoardRefString(int r, int c) {
        String row, col;
        if (r < 10)
            row = String.format("%02d", r);
        else
            row = String.valueOf(r);
        if (c < 10)
            col = String.format("%02d", c);
        else
            col = String.valueOf(c);

        return row + col;
    }


    /**
     * The getTiles function returns a copy of the board.
     * @return A copy of the board
     */
    public Tile[][] getTiles() {
        Tile[][] boardCopy = new Tile[BOARD_HEIGHT][BOARD_WIDTH];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(this.board[i], 0, boardCopy[i], 0, this.board[0].length);
        }
        return boardCopy;
    }


    /**
     * The boardLegal function checks if a word is legal to be placed on the board.
     * If the board is empty, then it must go on the star square and stay in bounds.
     * If not, then it must be in bounds and have an adjacent or overlapping tile.
     * @param word word Check if the word is in bounds
     * @return True if the board is empty and the word is on the star square
     */
    public boolean boardLegal(Word word) {
        if (isBoardEmpty() && isWordOnStarSquare(word) && wordIsInBounds(word))
            return true;
        return wordIsInBounds(word) && wordHasAdjacentOrOverlappingTile(word) && noChangeOfTilesNeeded(word);
    }


    /**
     * The isBoardEmpty function checks to see if the board is empty.
     * @return True if the board is empty and false otherwise
     */
    private boolean isBoardEmpty() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board[i][j] != null)
                    return false;
            }
        }
        return true;
    }


    /**
     * The isWordOnStarSquare function checks to see if the word is on a star square.
     * @param word word Get the row and column of the word, as well as its length
     * @return True if the word is on the star square
     */
    private boolean isWordOnStarSquare(Word word) {
        int wordLength = word.getTiles().length;
        if (word.isVertical()) {
            if (word.getCol() == 7)
                return word.getRow() + wordLength > 7;
        } else {
            if (word.getRow() == 7)
                return word.getCol() + wordLength > 7;
        }
        return false;
    }


    /**
     * The wordIsInBounds function checks to see if the word is within the bounds of the board.
     * @param word word Access the row and column of the word, as well as its length
     * @return True if the word is within the bounds of the board
     */
    private boolean wordIsInBounds(Word word) {
        if (word.allTilesAreNulls())
            return false;
        int row = word.getRow();
        int col = word.getCol();
        int wordLength = word.getTiles().length;

        if (row < 0 || row >= BOARD_HEIGHT || col < 0 || col >= BOARD_WIDTH)
            return false;

        if (word.isVertical())
            return word.getRow() + wordLength <= BOARD_HEIGHT;
        else
            return word.getCol() + wordLength <= BOARD_WIDTH;
    }


    /**
     * The wordHasAdjacentOrOverlappingTile function checks if the word has an adjacent or overlapping tile.
     * @param word word Determine if the word is vertical or horizontal
     * @return True if the word has
     */
    private boolean wordHasAdjacentOrOverlappingTile(Word word) {
        if (word.isVertical())
            return checkAdjacencyForVertical(word);
        else
            return checkAdjacencyForHorizontal(word);
    }


    /**
     * The checkAdjacencyForVertical function checks if the word has any adjacent tiles in a vertical direction.
     * @param word word Get the row and column of the word
     * @return True if the word has an adjacent tile from above or below
     */
    private boolean checkAdjacencyForVertical(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        int wordLength = word.getTiles().length;

        // If the word has an adjacent tile from above
        if (row - 1 >= 0 && board[row - 1][col] != null)
            return true;

        // If the word has an adjacent tile from below
        if (row + wordLength + 1 < BOARD_HEIGHT && board[row + wordLength + 1][col] != null)
            return true;

        for (int i = 0; i < wordLength; i++) {
            // If the word can have an adjacent tile from left or right
            if (col >= 1 && col <= 13) {
                if (board[row + i][col - 1] != null || board[row + i][col + 1] != null)
                    return true;
            }
            // If the word can have adjacent tiles only on left
            if (col == BOARD_WIDTH - 1) {
                if (board[row + i][col - 1] != null)
                    return true;
            }
            // If the word can have adjacent tiles only on right
            if (col == 0) {
                if (board[row + i][col + 1] != null)
                    return true;
            }
        }

        return false;
    }


    /**
     * The checkAdjacencyForHorizontal function checks if the word has any adjacent tiles from the left, right, above or below.
     * @param word word Get the row and column of the word
     * @return True if the word has an adjacent tile from the left, right or above/below
     */
    private boolean checkAdjacencyForHorizontal(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        int wordLength = word.getTiles().length;

        // If the word has an adjacent tile from the left
        if (col - 1 >= 0 && board[row][col - 1] != null)
            return true;

        // If the word has an adjacent tile from the right
        if (col + wordLength + 1 < BOARD_WIDTH && board[row][col + wordLength + 1] != null)
            return true;

        for (int i = 0; i < wordLength; i++) {
            // If the word can have adjacent tiles from above or below
            if (row >= 1 && row <= 13) {
                if (board[row - 1][col + i] != null || board[row + 1][col + i] != null)
                    return true;
            }
            // If the word can have adjacent tiles only from above
            if (row == BOARD_HEIGHT - 1) {
                if (board[row - 1][col + i] != null)
                    return true;
            }
            // If the word can have adjacent tiles only from below
            if (row == 0) {
                if (board[row + 1][col + i] != null)
                    return true;
            }
        }

        return false;
    }


    /**
     * The noChangeOfTilesNeeded function checks if the word that is being added to the board
     * will change any of the tiles on the board. If it does, then we know that this word can be
     * added to our list of words. If not, then we know that this word cannot be added because it
     * would not change anything on our current board and therefore would not give us a new state.
     * @param word word Pass the word to be added to the board
     * @return True if the word is placed on the board without changing any tiles
     */
    private boolean noChangeOfTilesNeeded(Word word) {
        Tile[][] boardCopy = getTiles();
        addWordToBoard(word, boardCopy);

        int row = word.getRow();
        int col = word.getCol();
        int wordLength = word.getTiles().length;
        int countOfSameLetters = 0;

        for (int i = 0; i < wordLength; i++) {
            if (word.isVertical()) {
                if (word.getTiles()[i] == null || board[row + i][col] == null)
                    continue;
                if (boardCopy[row + i][col] == board[row + i][col]) {
                    countOfSameLetters++;
                    continue;
                }
            } else {
                if (word.getTiles()[i] == null || board[row][col + i] == null)
                    continue;
                if (boardCopy[row][col + i] == board[row][col + i]) {
                    countOfSameLetters++;
                    continue;
                }
            }
            return false;
        }
        return wordLength != countOfSameLetters;
    }


    /**
     * The dictionaryLegal function checks if the word is in the dictionary.
     * @param word word Pass in the word that is being checked
     * @return True if the word is in the dictionary and false otherwise
     */
    public boolean dictionaryLegal(Word word) {
        GameManager gameManager = GameManager.get();
        String result = gameManager.query(word);
        return result.equals("true");
    }


    /**
     * The getWords function takes in a word and returns an ArrayList of all the words that can be formed
     * by adding to the given word. It does this by checking if there are any tiles on the board, and if so,
     * it checks whether they form a complete word with what has already been written. If so, it adds
     * that complete word to an ArrayList of words. Then it finds all other new words created by adding more letters
     * to existing ones (or creating new ones) and adds them as well. Finally, it returns this list of newly found
     * words for use elsewhere in
     * @param word word Find the word that is currently being written
     * @return An arraylist of words
     */
    private ArrayList<Word> getWords(Word word) {
        ArrayList<Word> newWords = new ArrayList<>();
        // If the board is empty, it's the only word
        if (isBoardEmpty()) {
            //If word isn't complete, we can't write it. Return empty arraylist.
            if (!isWordComplete(word)) {
                return newWords;
            }
            newWords.add(word);
            this.foundWords.add(word);
            return newWords;
        }

        // If the board isn't empty, it might be partially written.
        // Make sure that it's written correctly and add to the list.
        Word completeWord = findCompleteWord(word);
        if (completeWord.hasNullTiles()) {
            return newWords;
        }
        newWords.add(completeWord);

        //Check for all other newly created words.
        findNewWords(word, newWords);
        this.foundWords.addAll(newWords);

        return newWords;
    }


    /**
     * The isWordComplete function checks to see if the word is complete.
     * @param word word Access the tiles array in word
     * @return True if the word is complete, and false otherwise
     */
    private boolean isWordComplete(Word word) {
        int wordLength = word.getTiles().length;
        for (int i = 0; i < wordLength; i++) {
            if (word.getTiles()[i] == null)
                return false;
        }
        return true;
    }


    /**
     * The findNewWords function takes in a Word object and an ArrayList of Words.
     * It then checks if the word is vertical or horizontal, and calls the appropriate function to find new words.
     * @param word word Determine if the word is vertical or horizontal
     * @param words&lt;Word&gt; words Store the new words found
     */
    private void findNewWords(Word word, ArrayList<Word> words) {
        if (word.isVertical())
            findHorizontalNewWords(word, words);

        if (!word.isVertical())
            findVerticalNewWords(word, words);
    }


    /**
     * The findHorizontalNewWords function takes in a Word object and an ArrayList of Words.
     * It then creates a copy of the board, adds the given word to it, and checks for new words that are formed horizontally.
     * If any new words are found, they are added to the ArrayList of Words.
     * @param word word Add the word to the board-copy
     * @param words&lt;Word&gt; words Store the new words found
     */
    private void findHorizontalNewWords(Word word, ArrayList<Word> words) {
        Tile[][] boardCopy = getTiles();
        addWordToBoard(word, boardCopy);
        int row = word.getRow();
        int col = word.getCol();
        int wordLength = word.getTiles().length;
        int j, length, wordStart;

        for (int i = 0; i < wordLength; i++) {
            wordStart = col;
            j = 1;
            length = 1;
            while (col - j >= 0 && boardCopy[row + i][col - j] != null) {
                wordStart = col - j;
                j++;
                length++;
            }
            j = 1;
            while (col + j < BOARD_WIDTH && boardCopy[row + i][col + j] != null) {
                j++;
                length++;
            }
            if (length > 1) {
                Tile[] tiles = new Tile[length];
                System.arraycopy(boardCopy[row + i], wordStart, tiles, 0, length);
                Word newWord = new Word(tiles, row + i, wordStart, false);
                if (!wordWasAlreadyFound(newWord)) {
                    words.add(newWord);
                }
            }
        }
    }

    /**
     * The findVerticalNewWords function takes in a Word object and an ArrayList of Words.
     * It then creates a copy of the board, adds the given word to it, and checks for new words that are formed vertically.
     * If any new words are found, they are added to the ArrayList of Words.
     * @param word word Add the word to the board
     * @param words&lt;Word&gt; words Store the new words found
     */
    private void findVerticalNewWords(Word word, ArrayList<Word> words) {
        Tile[][] boardCopy = getTiles();
        addWordToBoard(word, boardCopy);
        int row = word.getRow();
        int col = word.getCol();
        int wordLength = word.getTiles().length;
        int j, length, wordStart;

        for (int i = 0; i < wordLength; i++) {
            wordStart = row;
            j = 1;
            length = 1;
            while (row - j >= 0 && boardCopy[row - j][col + i] != null) {
                wordStart = row - j;
                j++;
                length++;
            }
            j = 1;
            while (row + j < BOARD_HEIGHT && boardCopy[row + j][col + i] != null) {
                j++;
                length++;
            }
            if (length > 1) {
                Tile[] tiles = new Tile[length];
                for (int k = 0; k < length; k++) {
                    tiles[k] = boardCopy[wordStart + k][col + i];
                }
                Word newWord = new Word(tiles, wordStart, col + i, true);
                if (!wordWasAlreadyFound(newWord)) {
                    words.add(newWord);
                }
            }
        }
    }


    /**
     * The wordWasAlreadyFound function checks to see if the word that was just found has already been found.
     * It does this by checking the row, column, and length of each word in the list of words that have already been found.
     * If a match is made between any of these three values for two different words, then it returns true because we know
     * that one or more letters from those two words are overlapping. This function is called after every new word is added to
     * our list of foundWords, so we can make sure no duplicates are being added to our list.
     * @param word word Get the row, col and length of the word
     * @return True if the word has already been found
     */
    private boolean wordWasAlreadyFound(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        int wordLength = word.getTiles().length;

        for (Word foundWord : foundWords) {
            if (foundWord.getRow() == row && foundWord.getCol() == col && foundWord.getTiles().length == wordLength) {
                return true;
            }
        }
        return false;
    }


    /**
     * The findCompleteWord function takes in a Word object and returns the same Word object with its Tile array
     * filled out. If the word is vertical, it will fill out each tile from top to bottom. If horizontal, it will
     * fill out each tile from left to right. This function is used when we want to find all possible words that can be made
     * on the board given a certain set of tiles (i.e., rack). It allows us to check if any of those words are valid by
     * checking if they are contained in our dictionary or not (see findValidWords function).
     * @param word word Get the row and column of the word
     * @return A word object that has the same row and column as the inputted word, but with a new tiles array
     */
    private Word findCompleteWord(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        int wordLength = word.getTiles().length;
        Tile[] tiles = new Tile[wordLength];
        if (word.isVertical()) {
            for (int i = 0; i < wordLength; i++) {
                if (word.getTiles()[i] == null)
                    tiles[i] = board[row + i][col];
                else
                    tiles[i] = word.getTiles()[i];
            }
        } else {
            for (int i = 0; i < wordLength; i++) {
                if (word.getTiles()[i] == null)
                    tiles[i] = board[row][col + i];
                else
                    tiles[i] = word.getTiles()[i];
            }
        }
        word.setTiles(tiles);
        return word;
    }


    /**
     * The tryPlaceWord function takes a Word object as an argument and returns an integer.
     * The function checks if the word can be placed on the board, and if it can, adds it to the board.
     * If not, then - 1 is returned. If there are words created by placing this new word on the board that are not in our dictionary, 0 is returned.
     * Otherwise, we return a score for how many points were earned by placing this new word on our game-board (the sum of all scores of all words created).
     * @param newWord newWord Pass the word that is being placed on the board
     * @return - 1 if the word cannot be placed on the board
     */
    public int tryPlaceWord(Word newWord) {
        if (!boardLegal(newWord))
            return -1;

        ArrayList<Word> createdWords = getWords(newWord);

        for (Word word : createdWords)
            if (!dictionaryLegal(word))
                return 0;
        addWordToBoard(newWord, this.board);
        int score = 0;
        for (Word word : createdWords) {
            score += getScore(word);
        }
        System.out.println("Score -> " + score);
        return score;
    }


    /**
     * The printBoard function prints the board to the console.
     */
    public void printBoard() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (!Objects.isNull(this.board[i][j]))
                    System.out.print(board[i][j].letter + " ");
                else
                    System.out.print("- ");
            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * The addWordToBoard function takes a Word object and a Tile[][] board as parameters.
     * It then adds the tiles of the word to the board, depending on whether it is vertical or horizontal.
     * @param word word Get the row, column and tiles of the word
     * @param board board Represent the board
     */
    private void addWordToBoard(Word word, Tile[][] board) {
        int row = word.getRow();
        int col = word.getCol();
        int wordLength = word.getTiles().length;

        if (word.isVertical()) {
            for (int i = 0; i < wordLength; i++) {
                board[row + i][col] = word.getTiles()[i];
            }
        } else {
            for (int i = 0; i < wordLength; i++) {
                board[row][col + i] = word.getTiles()[i];
            }
        }
    }
}
