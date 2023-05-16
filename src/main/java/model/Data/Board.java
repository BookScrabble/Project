package model.Data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Board {
    private static Board single_instance = null;
    private final static int BOARD_HEIGHT = 15;
    private final static int BOARD_WIDTH = 15;
    private final Tile[][] board = new Tile[BOARD_HEIGHT][BOARD_WIDTH];
    private static final HashMap<String, String> boardScores = new HashMap<>();
    private final ArrayList<Word> foundWords = new ArrayList<>();


    private Board() {
        initBoardScores();
    }

    public static Board getBoard() {
        if (single_instance == null)
            single_instance = new Board();
        return single_instance;
    }

    /**
     * the board has special scores for different indexes, this function initializes special scores locations according to:
     * location on the board: "xxyy" with xx being the row and yy being the column.
     * 2W = double word score
     * 3W = triple word score
     * 2L = double letter score
     * 3L = triple letter score
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
     * This function returns the score of a word according to the tiles it's composed of.
     * The function checks each tile to see if it's on a special board index, and updates the score accordingly.
     *
     * @param word word
     * @return score total for all the tiles.
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
     * This function is used to create the string representation of a board location.
     *
     * @param r row
     * @param c column
     * @return returns a string in the shape of "xxyy" with xx being row and yy being column.
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
     * @return returns a 2d array copy of the board with the tiles that are currently placed.
     */
    public Tile[][] getTiles() {
        Tile[][] boardCopy = new Tile[BOARD_HEIGHT][BOARD_WIDTH];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(this.board[i], 0, boardCopy[i], 0, this.board[0].length);
        }
        return boardCopy;
    }

    /**
     * This function checks if a word is legal - basically checks if a word can be placed on the board.
     *
     * @param word word
     * @return true / false
     */
    public boolean boardLegal(Word word) {
        // If board is empty, the word must go on the star square but has to also stay in bounds.
        if (isBoardEmpty() && isWordOnStarSquare(word) && wordIsInBounds(word))
            return true;

        // If the board isn't empty, the word must be in bounds and have an adjacent or overlapping tile.
        // We must also check that there is no need to change any tiles.
        if (wordIsInBounds(word) && wordHasAdjacentOrOverlappingTile(word) && noChangeOfTilesNeeded(word))
            return true;

        return false;
    }

    /**
     * Checks if the board is empty or has any tiles on it
     *
     * @return true once a tile was found, false otherwise
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
     * This function checks if a word will be on the star square.
     * USE ONLY IF BOARD IS EMPTY.
     *
     * @param word word
     * @return true / false accordingly.
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
     * Checks if the word is in the boundaries of the board
     *
     * @param word word
     * @return true / false
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
     * Checks if the word has an adjacent or overlapping tile
     *
     * @param word word
     * @return true / false
     */
    private boolean wordHasAdjacentOrOverlappingTile(Word word) {
        if (word.isVertical())
            return checkAdjacencyForVertical(word);
        else
            return checkAdjacencyForHorizontal(word);
    }

    /**
     * Checks if the word has an adjacent tile anywhere
     *
     * @param word word
     * @return true / false
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
     * Checks if the word has an adjacent tile anywhere
     *
     * @param word word
     * @return true / false
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
     * This function checks if the word doesn't need to change a tile to be added.
     *
     * @param word word
     * @return true / false accordingly.
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
        if (wordLength == countOfSameLetters)
            return false;
        return true;
    }

    /**
     * Checks if the word is legal in the dictionary
     * TODO - Finish implementation of dictionaryLegal
     * @param word word
     * @return true / false
     */
    public boolean dictionaryLegal(Word word) {
        return true;
    }

    /**
     * Creates an arraylist of the newly created words from the placed word.
     *
     * @param word word
     * @return returns the arraylist of the new words.
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
     * Checks if the word is complete or has a null tile anywhere
     *
     * @param word word
     * @return true / false
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
     * This function will be used with getWords function.
     * It will add all the newly formed words around the added word.
     *
     * @param word  word
     * @param words arraylist for the new words.
     */
    private void findNewWords(Word word, ArrayList<Word> words) {
        if (word.isVertical())
            findHorizontalNewWords(word, words);

        if (!word.isVertical())
            findVerticalNewWords(word, words);
    }

    /**
     * This function finds all the new words that were created around the placement of a new word.
     * Used for a vertical word to find horizontal new words.
     *
     * @param word  word
     * @param words arraylist of new words
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
                for (int k = 0; k < length; k++) {
                    tiles[k] = boardCopy[row + i][wordStart + k];
                }
                Word newWord = new Word(tiles, row + i, wordStart, false);
                if (!wordWasAlreadyFound(newWord)) {
                    words.add(newWord);
                }
            }
        }
    }

    /**
     * This function finds all the new words that were created around the placement of a new word.
     * Used for a horizontal word to find vertical new words.
     *
     * @param word  word
     * @param words new words
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
     * Checks if a word is in the foundWords arraylist to see if it had been previously found.
     *
     * @param word word
     * @return true / false
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
     * Completes a word that has a null tile somewhere by checking the board to see
     * if there is a tile in the corresponding place.
     *
     * @param word word
     * @return the word with the board values for where the word had a tile that was null.
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
     * Tries to place the word in the board and returns the score the move.
     *
     * @param newWord new word to place
     * @return total score of the word + score of the additional words that were created.
     */
    public int tryPlaceWord(Word newWord) {
        if (!boardLegal(newWord))
            return 0;

        ArrayList<Word> createdWords = getWords(newWord);

        for (Word word : createdWords)
            if (!dictionaryLegal(word))
                return 0;
        addWordToBoard(newWord, this.board);
        int score = 0;
        for (Word word : createdWords) {
            score += getScore(word);
        }

        return score;
    }

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
     * This function adds a word to the board.
     *
     * @param word  word
     * @param board board
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
