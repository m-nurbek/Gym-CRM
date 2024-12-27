package com.epam.gym.util;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicBigInteger is designed to be thread-safe, allowing multiple threads
 * to safely modify its value without the need for external synchronization
 * mechanisms like synchronized blocks or ReentrantLocks. Operations like
 * increment, decrement, add, and compare-and-set (CAS) are atomic, meaning
 * they complete in a single, indivisible operation, which prevents race conditions.
 */
public final class AtomicBigInteger {
    private final AtomicReference<BigInteger> valueHolder = new AtomicReference<>();

    public AtomicBigInteger(BigInteger bigInteger) {
        valueHolder.set(bigInteger);
    }

    public BigInteger incrementAndGet() {
        for (; ; ) {
            BigInteger current = valueHolder.get();
            BigInteger next = current.add(BigInteger.ONE);
            if (valueHolder.compareAndSet(current, next)) {
                return next;
            }
        }
    }
}