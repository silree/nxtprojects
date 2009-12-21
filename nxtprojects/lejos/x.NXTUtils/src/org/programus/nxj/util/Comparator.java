package org.programus.nxj.util;

/**
 * Comparator. Just the same as java.util.Comparator
 * @author Programus
 *
 * @param <T> the type of objects that may be compared by this comparator
 */
public interface Comparator<T> {
    int compare(T o1, T o2);

    boolean equals(Object obj);
}
