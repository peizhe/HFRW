package com.kol.recognition.perceptualHash.bitsChain;

import com.google.common.base.Splitter;
import com.kol.recognition.Utils;

public final class BitsChainIntToString implements BitsChainToString {
    @Override
    public String toString(final String bitsChain) {
        final String bits;
        final int length = bitsChain.length();
        final int mod = length % INT_BITS_SIZE;
        if(mod != 0) {
            bits = Utils.leadingZeros(bitsChain, length + (INT_BITS_SIZE - mod));
        } else {
            bits = bitsChain;
        }

        final StringBuilder sb = new StringBuilder();
        final Iterable<String> iterable = Splitter.fixedLength(INT_BITS_SIZE).split(bits);
        for (String el : iterable) {
            sb.append(Integer.toHexString(Integer.parseInt(el, BINARY_RADIX)));
        }
        return sb.toString();
    }
}
