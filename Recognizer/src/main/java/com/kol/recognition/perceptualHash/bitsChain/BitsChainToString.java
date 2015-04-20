package com.kol.recognition.perceptualHash.bitsChain;

@FunctionalInterface
public interface BitsChainToString {

    int HEX_RADIX = 16;
    int BINARY_RADIX = 2;
    int INT_BITS_SIZE = 31;

    String toString(String bitsChain);
}
