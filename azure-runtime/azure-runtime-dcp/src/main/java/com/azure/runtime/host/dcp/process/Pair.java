package com.azure.runtime.host.dcp.process;

/**
 * A utility class to hold a pair of objects.
 *
 * @param <A> The first object type.
 * @param <B> The second object type.
 */
public record Pair<A, B>(A first, B second) {
}