package com.richcode.support;

import lombok.Builder;

/**
 * Stores the right and left edges of the range.
 * Range is inclusive on the both bounds.
 * Can not be empty.
 *
 * @param from left bound of the range - inclusive
 * @param to right bound of the range - inclusive
 * @param <T> class of the range
 */
@Builder
public record SearchRange<T>(T from, T to) {

    public static <T> SearchRange<T> range(T from, T to) {
        return new SearchRange<>(from, to);
    }

    public static <T> SearchRange<T> from(T from) {
        return new SearchRange<>(from, null);
    }

    public static <T> SearchRange<T> to(T to) {
        return new SearchRange<>(null, to);
    }

    public boolean isEmpty() {
        return from == null && to == null;
    }

    public static <T> boolean isEmpty(SearchRange<T> range) {
        return range == null || range.isEmpty();
    }

}
