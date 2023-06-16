package model.logic.host.data;


import java.io.Serializable;
import java.util.*;

public class Tile implements Serializable {
    public final int score;
    public final char letter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return score == tile.score && letter == tile.letter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, letter);
    }

    /**
     * Constructor for tile
     * @param score tile score
     * @param letter letter that will be represented by the tile
     */
    private Tile(int score, char letter) {
        this.score = score;
        this.letter = letter;
    }

    /**
     * Bag is a public static inner class and will be the only class with the ability to create tiles.
     */
    public static class Bag implements Serializable {

        private static Bag single_instance = null;
        // This array represents remaining letters by index.
        // index 0 = A, index 1 = B, ..... , index 25 = Z
        //These are the default values.
        //                                         A  B  C  D   E  F  G  H  I  J  K  L  M  N  O  P  Q  R  S  T  U  V  W  X  Y  Z
        final static int[] DEFAULT_LETTER_COUNT = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        private final int[] remainingLetters = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        private final Tile[] tiles = {
                new Tile(1, 'A'), new Tile(3, 'B'), new Tile(3, 'C'), new Tile(2, 'D'),
                new Tile(1, 'E'), new Tile(4, 'F'), new Tile(2, 'G'), new Tile(4, 'H'),
                new Tile(1, 'I'), new Tile(8, 'J'), new Tile(5, 'K'), new Tile(1, 'L'),
                new Tile(3, 'M'), new Tile(1, 'N'), new Tile(1, 'O'), new Tile(3, 'P'),
                new Tile(10, 'Q'), new Tile(1, 'R'), new Tile(1, 'S'), new Tile(1, 'T'),
                new Tile(1, 'U'), new Tile(4, 'V'), new Tile(4, 'W'), new Tile(8, 'X'),
                new Tile(4, 'Y'), new Tile(10, 'Z')};
        private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        //This map holds letter-index pairs.
        // A = 0, B = 1, ... , Z = 25.
        static Map<Character, Integer> letterIndexMap = new HashMap<Character, Integer>();

        private Bag() {
        }

        public static Bag getBag() {
            if (single_instance == null) {
                initializeLetterIndexDictionary();
                single_instance = new Bag();
            }
            return single_instance;
        }

        /**
         * initialize the letter index dictionary by referencing 'a' to 0, 'b' to 1, ... 'z' to 25.
         */
        private static void initializeLetterIndexDictionary() {
            for (int i = 0; i < alphabet.length; i++) {
                letterIndexMap.put(alphabet[i], i);
            }
        }

        /**
         * This function returns a random tile.
         * It works by checking if there are any tiles left using the remainingLetters array, then chooses a random tile and checks
         * if the letter is available. if yes, returns it, else chooses a random tile again.
         * @return random tile.
         */
        public Tile getRand() {
            if (size() == 0)
                return null;
            int rnd, chosenTileLetterIndex;
            Tile chosenTile;
            rnd = new Random().nextInt(tiles.length);
            chosenTile = tiles[rnd];
            chosenTileLetterIndex = letterIndexMap.get(chosenTile.letter);
            while (remainingLetters[chosenTileLetterIndex] == 0) {
                rnd = new Random().nextInt(tiles.length);
                chosenTile = tiles[rnd];
                chosenTileLetterIndex = letterIndexMap.get(chosenTile.letter);
            }
            remainingLetters[chosenTileLetterIndex]--;
            return chosenTile;
        }

        /**
         * @param letter Character that represents a letter.
         * @return Tile corresponding to a given letter.
         */
        public Tile getTile(char letter) {
            if (!Character.isUpperCase(letter))
                return null;
            int letterIndex = letterIndexMap.get(letter);
            if(remainingLetters[letterIndex] == 0)
                return null;
            remainingLetters[letterIndex]--;
            return tiles[letterIndex];
        }

        /**
         * This function puts a tile back inside the bag, functionally only adds 1 to the counter of specific letter.
         * @param tile Tile.
         */
        public void put(Tile tile) {
            if (tile == null)
                return;
            int letterIndex = letterIndexMap.get(tile.letter);
            if (remainingLetters[letterIndex] + 1 <= DEFAULT_LETTER_COUNT[letterIndex]) {
                remainingLetters[letterIndex]++;
            }
        }

        /**
         * @return returns the amount of tiles left in the bag.
         */
        public int size() {
            return Arrays.stream(remainingLetters).sum();
        }

        /**
         * @return returns a copy of the remaining letters array.
         */
        public int [] getQuantities() {
            int[] remainingLettersCopy = new int[26];
            System.arraycopy(remainingLetters, 0, remainingLettersCopy, 0, remainingLetters.length);
            return remainingLettersCopy;
        }

        /**
         * @param letter letter
         * @return returns the amount of tiles left of a specific letter.
         */
        public int getRemainingOfLetter(char letter) {
            return remainingLetters[letterIndexMap.get(letter)];
        }
    }
}
