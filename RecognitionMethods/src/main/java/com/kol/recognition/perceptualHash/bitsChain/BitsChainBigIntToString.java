package com.kol.recognition.perceptualHash.bitsChain;

import java.math.BigInteger;

public final class BitsChainBigIntToString implements BitsChainToString {

    @Override
    public String toString(final String bitsChain) {
        return new BigInteger(bitsChain, BINARY_RADIX).toString(HEX_RADIX);
    }
}
