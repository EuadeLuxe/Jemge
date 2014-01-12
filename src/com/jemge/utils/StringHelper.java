package com.jemge.utils;

public class StringHelper {

    /**
     * Faster then String.equals()
     * @param s1
     * @param s2
     * @return
     */
    public static boolean equals(final String s1, final String s2) {
        return s1 != null && s2 != null && s1.hashCode() == s2.hashCode()
                && s1.equals(s2);
    }
}
