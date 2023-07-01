package model.logic.host.data;


import java.io.Serializable;
import java.util.*;

public class Tile implements Serializable {
    public final int score;
    public final char letter;

    /**
     * The equals function is used to compare two objects.
     * @param o o Compare the current object to another object
     * @return True if the two tiles have the same letter and score
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return score == tile.score && letter == tile.letter;
    }

    /**
     * The hashCode function is used to generate a unique hash code for each object.
     * This is useful when storing objects in data structures such as HashMaps, where
     * the hashCode of an object can be used to determine its location in the map.
     * @return The hashcode of the score and letter
     */
    @Override
    public int hashCode() {
        return Objects.hash(score, letter);
    }


    /**
     * The Tile function is a constructor that creates a Tile object with the given score and letter.
     * @param score score Assign the score of each tile
     * @param letter letter Assign the letter to the tile
     */
    private Tile(int score, char letter) {
        this.score = score;
        this.letter = letter;
    }

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
        static Map<Character, Integer> letterIndexMap = new HashMap<>();

        /**
         * The Bag function is a constructor that creates an empty bag.
         */
        private Bag() {}

        /**
         * The getBag function is a static function that returns the single instance of the Bag class.
         * If there is no instance, it creates one and initializes its letterIndexDictionary variable.
         * @return A bag object
         */
        public static Bag getBag() {
            if (single_instance == null) {
                initializeLetterIndexDictionary();
                single_instance = new Bag();
            }
            return single_instance;
        }


        /**
         * The initializeLetterIndexDictionary function initializes the letterIndexMap dictionary.
         * The key is a character in the alphabet array, and the value is an integer representing its index in that array.
         */
        private static void initializeLetterIndexDictionary() {
            for (int i = 0; i < alphabet.length; i++) {
                letterIndexMap.put(alphabet[i], i);
            }
        }


        /**
         * The getRand function returns a random tile from the bag.
         * @return A random tile from the bag
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
         * The getTile function takes a character as an argument and returns the Tile object
         * associated with that letter. If there are no more tiles of that type, it returns null.
         * @param letter letter Determine which tile to return
         * @return A tile with the given letter
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
         * The put function adds a tile to the bag.
         * @param tile tile Add a tile to the bag
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
         * The size function returns the number of letters remaining in the bag.
         * @return The sum of the remaining letters array
         */
        public int size() {
            return Arrays.stream(remainingLetters).sum();
        }

        /**
         * The getQuantities function returns a copy of the remainingLetters array.
         * @return A copy of the remaining letters array
         */
        public int [] getQuantities() {
            int[] remainingLettersCopy = new int[26];
            System.arraycopy(remainingLetters, 0, remainingLettersCopy, 0, remainingLetters.length);
            return remainingLettersCopy;
        }
    }
}
