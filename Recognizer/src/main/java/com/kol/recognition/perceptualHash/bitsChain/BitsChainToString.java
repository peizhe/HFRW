package com.kol.recognition.perceptualHash.bitsChain;

@FunctionalInterface
public interface BitsChainToString {

    public static final int HEX_RADIX = 16;
    public static final int BINARY_RADIX = 2;
    public static final int INT_BITS_SIZE = 31;

    String toString(String bitsChain);
}
