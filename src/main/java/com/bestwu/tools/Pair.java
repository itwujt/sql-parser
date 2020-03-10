package com.bestwu.tools;

/**
 * 键值对 工具模板
 * @author Best Wu
 */
public class Pair<F, S> {
    /**
     * key
     */
    private F first;

    /**
     * value
     */
    private S second;

    public F of() {
        return this.first;
    }

    public S next() {
        return this.second;
    }

    public void put(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Pair<F, S> newInstance() {
        return new Pair<>();
    }
}
