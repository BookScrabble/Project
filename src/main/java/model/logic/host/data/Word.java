package model.logic.host.data;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Word implements Serializable {
    private Tile[] tiles;
    private final int row;
    private final int col;
    private final boolean vertical;


    /**
     * The Word function is a constructor that takes in the tiles, row, column and vertical of the word.
     * @param tiles tiles Store the tiles that make up the word
     * @param row row Set the row of the word
     * @param col col Set the column of the word
     * @param vertical vertical Determine whether the word is vertical or horizontal
     */
    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        Tile[] temp = new Tile[tiles.length];
        System.arraycopy(tiles, 0, temp, 0, tiles.length);
        this.tiles = temp;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }


    /**
     * The setTiles function is used to set the tiles of a given board.
     * @param tiles tiles Set the tiles array to the value of temp
     */
    public void setTiles(Tile[] tiles) {
        Tile[] temp = new Tile[tiles.length];
        System.arraycopy(tiles, 0, temp, 0, tiles.length);
        this.tiles = temp;
    }


    /**
     * The getTiles function returns the tiles array.
     * @return An array of tile objects
     */
    public Tile[] getTiles() {
        return tiles;
    }


    /**
     * The getRow function returns the row of the current position.
     * @return The row of the cell
     */
    public int getRow() {
        return row;
    }


    /**
     * The getCol function returns the column of the current position.
     * @return The column of the current position
     */
    public int getCol() {
        return col;
    }


    /**
     * The isVertical function returns a boolean value that indicates whether the
     * current instance of the class is vertical or not.
     * @return A boolean value that indicates if the line is vertical or not
     */
    public boolean isVertical() {
        return vertical;
    }

    /**
     * The equals function checks if two words are equal.
     * @param o o Compare the current object to another object
     * @return True if the two words are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return row == word.row && col == word.col && vertical == word.vertical && Arrays.equals(tiles, word.tiles);
    }

    /**
     * The hashCode function is used to generate a unique hash code for each object.
     * This function uses the row, col, vertical and tiles fields of the WordSearchPuzzle class
     * to generate a unique hash code for each object. The hashCode function is used in conjunction with
     * the equals method so that two objects can be compared by their contents rather than their memory location.
     * @return A hash code value for the object
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(row, col, vertical);
        result = 31 * result + Arrays.hashCode(tiles);
        return result;
    }


    /**
     * The hasNullTiles function checks to see if there are any null tiles in the array.
     * @return True if there is a null tile in the tiles array and false otherwise
     */
    public boolean hasNullTiles() {
        for (Tile tile : tiles) {
            if (tile == null) {
                return true;
            }
        }
        return false;
    }


    /**
     * The allTilesAreNulls function checks to see if all the tiles in a word are null.
     * @return True if all the tiles are nulls
     */
    public boolean allTilesAreNulls() {
        int wordLength = getTiles().length;
        for (int i = 0; i < wordLength; i++) {
            if (getTiles()[i] != null)
                return false;
        }
        return true;
    }

}
