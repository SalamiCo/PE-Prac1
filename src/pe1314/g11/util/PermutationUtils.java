package pe1314.g11.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Utilities used a lot in permutations.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 */
public final class PermutationUtils {

    private static final List<Set<List<Integer>>> CACHED_PERMS = new ArrayList<>();
    private static final List<Integer> NUMS = new ArrayList<>();
    
    static {
        firstN(100);
    }
    
    public static Integer wrapInt (int i) {
        return (i < NUMS.size()) ? NUMS.get(i) : Integer.valueOf(i);
    }
    
    public static List<Integer> firstN (int size) {
        while (NUMS.size() < size) {
            NUMS.add(Integer.valueOf(NUMS.size()));
        }

        return new ArrayList<>(NUMS.subList(0, size));
    }

    public static Iterable<List<Integer>> permutations (List<Integer> elems) {
        return new PermutationsIterable(elems);
    }

    private static Set<List<Integer>> computePermutations (List<Integer> elems) {
        // No elements: No permutations
        if (elems.size() == 0) {
            return Collections.emptySet();
        }

        // One element: A single permutation
        if (elems.size() == 1) {
            return Collections.singleton(elems);
        }

        // More elements: Remove one element, get permutations, restore element
        Set<List<Integer>> allPerms = new HashSet<>();
        List<Integer> elemsMinusI = new ArrayList<>(elems);
        Integer oldElem = elemsMinusI.remove(0);

        Set<List<Integer>> subPerms = computePermutations(elemsMinusI);
        for (List<Integer> subPerm : subPerms) {
            for (int i = 0; i <= subPerm.size(); i++) {
                subPerm.add(i, oldElem);
                allPerms.add(new ArrayList<>(subPerm));
                subPerm.remove(i);
            }
        }

        return allPerms;
    }

    /* package */static Set<List<Integer>> cachePermutations (int size) {
        if (size < 0) {
            // Base case
            return null;
        }

        cachePermutations(size - 1);
        if (CACHED_PERMS.size() == size) {
            CACHED_PERMS.add(computePermutations(firstN(size)));
        }

        return CACHED_PERMS.get(size);
    }

    private static class PermutationsIterable implements Iterable<List<Integer>> {

        private List<Integer> elements;

        /* package */PermutationsIterable (List<Integer> elements) {
            this.elements = elements;
        }

        @Override
        public Iterator<List<Integer>> iterator () {
            return new PermutationsIterator(elements);
        }

    }

    private static class PermutationsIterator implements Iterator<List<Integer>> {

        private List<Integer> elements;
        private Iterator<List<Integer>> permIt;

        private final List<Integer> permElems = new ArrayList<>();

        /* package */PermutationsIterator (List<Integer> elements) {
            permIt = cachePermutations(elements.size()).iterator();
            this.elements = elements;
        }

        @Override
        public boolean hasNext () {
            return permIt.hasNext();
        }

        @Override
        public List<Integer> next () {
            List<Integer> perm = permIt.next();

            permElems.clear();
            for (Integer i : perm) {
                permElems.add(elements.get(i.intValue()));
            }

            return permElems;
        }

        @Override
        public void remove () {
            throw new UnsupportedOperationException();
        }

    }
}
