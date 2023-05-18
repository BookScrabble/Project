package model.logic.host.data;


import java.util.Arrays;
import java.util.Objects;

public class Word {
    private Tile[] tiles;
    private int row;
    private int col;
    private boolean vertical;

    /**
     * Constructor for word
     * @param tiles tiles that make up the word
     * @param row first tile row
     * @param col first time column
     * @param vertical true / false if word is vertical or not
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
     * Setter for tiles
     */
    public void setTiles(Tile[] tiles) {
        Tile[] temp = new Tile[tiles.length];
        System.arraycopy(tiles, 0, temp, 0, tiles.length);
        this.tiles = temp;
    }

    /**
     * Getter for tiles
     */
    public Tile[] getTiles() {
        return tiles;
    }

    /**
     * Getter for row
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for column
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns if the word is vertical or not
     */
    public boolean isVertical() {
        return vertical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return row == word.row && col == word.col && vertical == word.vertical && Arrays.equals(tiles, word.tiles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(row, col, vertical);
        result = 31 * result + Arrays.hashCode(tiles);
        return result;
    }

    /**
     * Checks if the word has any null tiles
     * @return true if null tile was found, false otherwise
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
     * Checks if the word is composed of all null tiles
     * @return true if all tiles are null, false otherwise
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
