package model.logic.server.dictionary;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;


public class BloomFilter {
    private final BitSet bitset;
    private final ArrayList<MessageDigest> messageDigesters = new ArrayList<>();


    /**
     * A Bloom filter is a probabilistic data structure used for efficient set membership testing.
     * This constructor initializes a new Bloom filter with the specified size and hashing algorithms.
     * @param size the size of the bitset used by the Bloom filter.
     * @param hashingAlgorithms the hashing algorithms to be used by the Bloom filter.
     * @throws RuntimeException if an unknown hash function is provided.
     */
    public BloomFilter(int size, String... hashingAlgorithms) {
        this.bitset = new BitSet(size);
        for (String hashAlgorithm: hashingAlgorithms) {
            try {
                this.messageDigesters.add(MessageDigest.getInstance(hashAlgorithm));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(
                        "Unknown hash function provided. Use MD2, MD5, SHA-1, SHA-256, SHA-384 or SHA-512.");
            }
        }
    }

    /**
     * Adds a new word to the Bloom filter by hashing it using the hashing algorithms provided during initialization.
     * @param word the word to be added to the Bloom filter.
     */
    public void add(String word) {
        byte[] bytes;
        for (MessageDigest md : this.messageDigesters) {
            bytes = md.digest(word.getBytes());
            BigInteger bigInt = new BigInteger(bytes);
            this.bitset.set(Math.abs(bigInt.intValue()%bitset.size()));
        }
    }

    /**
     Tests if the Bloom filter contains the specified word by checking if the bits at the positions determined
     by the hashes of the word using the hashing algorithms provided during initialization are all set.
     @param word the word to be checked for membership in the Bloom filter.
     @return true if the word is possibly contained in the Bloom filter, false if the word is definitely not contained.
     */
    public boolean contains(String word) {
        byte[] bytes;
        for (MessageDigest md : this.messageDigesters) {
            bytes = md.digest(word.getBytes());
            BigInteger bigInt = new BigInteger(bytes);
            int index = Math.abs(bigInt.intValue()%bitset.size());
            if (!this.bitset.get(index))
                return false;
        }
        return true;
    }

    /**
     @return a string representation of the bitset, where each bit is represented by either 1 or 0.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i<bitset.length(); i++) {
            if (bitset.get(i)) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }
        }
        return sb.toString();
    }
}
