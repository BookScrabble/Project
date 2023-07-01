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
     * The BloomFilter function is used to add elements to the filter.
     * @param size size Set the size of the bitset
     * @param hashingAlgorithms hashingAlgorithms Pass in a variable number of arguments
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
     * The add function takes a string and hashes it using the message digesters.
     * It then sets the bits in the bitset to 1 at each of those hash values.
     * @param word word Create a byte array that is then used to create a biginteger
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
     * The contains function takes a string and checks if it is in the Bloom Filter.
     * It does this by hashing the word with each of the hash functions, then taking
     * that hash value modulo bitset size to get an index. If any of those indices are not set,
     * then we know that word is not in our filter. Otherwise, we return true because there's a chance
     * that it could be in our filter (but also might have been inserted into another Bloom Filter).
     * @param word word Create a byte array, which is then used to generate the biginteger
     * @return True if the word is in the bloom filter, false otherwise
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
     * The toString function returns a string representation of the bitset.
     * @return A string representation of the bitset
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
