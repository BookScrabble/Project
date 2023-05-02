package Model.Logic;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;


public class BloomFilter {
    private final BitSet bitset;
    private final ArrayList<MessageDigest> messageDigesters = new ArrayList<>();

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

    public void add(String word) {
        byte[] bytes;
        for (MessageDigest md : this.messageDigesters) {
            bytes = md.digest(word.getBytes());
            BigInteger bigInt = new BigInteger(bytes);
            this.bitset.set(Math.abs(bigInt.intValue()%bitset.size()));
        }
    }

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
