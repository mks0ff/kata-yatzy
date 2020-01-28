package toto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Yatzy {

    public static int chance(int d1, int d2, int d3, int d4, int d5) {
        return IntStream.of(d1, d2, d3, d4, d5)
                .reduce(0, Integer::sum);
    }

    public static int yatzy(int... dice) {
        return IntStream.of(dice).distinct().count() == 1 ? 50 : 0;
    }

    public static int ones(int d1, int d2, int d3, int d4, int d5) {
        int[] dice = {d1, d2, d3, d4, d5};
        return sum(dice, 1);
    }

    public static int twos(int d1, int d2, int d3, int d4, int d5) {
        int[] dice = {d1, d2, d3, d4, d5};
        return sum(dice, 2);
    }

    public static int threes(int d1, int d2, int d3, int d4, int d5) {
        int[] dice = {d1, d2, d3, d4, d5};
        return sum(dice, 3);
    }

    private int[] dice;

    public Yatzy(int d1, int d2, int d3, int d4, int _5) {
        dice = new int[5];
        dice[0] = d1;
        dice[1] = d2;
        dice[2] = d3;
        dice[3] = d4;
        dice[4] = _5;
    }

    public static int fours(int d1, int d2, int d3, int d4, int d5) {
        int[] dice = {d1, d2, d3, d4, d5};
        return sum(dice, 4);
    }

    public static int fives(int d1, int d2, int d3, int d4, int d5) {
        int[] dice = {d1, d2, d3, d4, d5};
        return sum(dice, 5);
    }

    public static int sixes(int d1, int d2, int d3, int d4, int d5) {
        int[] dice = {d1, d2, d3, d4, d5};
        return sum(dice, 6);
    }

    private static int sum(int[] dice, int add) {
        return IntStream.of(dice)
                .filter(a -> a == add)
                .reduce(0, (acc, value) -> acc + add);
    }

    private static List<Integer> zeros(int size) {
        return IntStream.rangeClosed(0, size - 1)
                .map(a -> 0)
                .boxed().collect(Collectors.toList());
    }

    private static List<Integer> inc(List<Integer> zeros, int ... values) {
        IntStream.of(values)
                .map(a -> a - 1)
                .forEach(a -> zeros.set(a, zeros.get(a) + 1));
        return new ArrayList<>(zeros);
    }

    public static int score_pair(int d1, int d2, int d3, int d4, int d5) {

        List<Integer> integers = zeros(6);
        List<Integer> list = inc(integers, d1, d2, d3, d4, d5);
        // reverse
        Collections.reverse(list);
        return list.stream()
                .filter(a -> a >= 2)
                .findFirst()
                .map(a -> (6 - list.indexOf(a)) * 2)
                .orElse(0);
    }

    public static int two_pair(int d1, int d2, int d3, int d4, int d5) {

        List<Integer> integers = zeros(6);
        List<Integer> list = inc(integers, d1, d2, d3, d4, d5);
        final AtomicInteger n1 = new AtomicInteger();
        final AtomicInteger score1 = new AtomicInteger();
        IntStream.range(0, 6)
                .filter(i -> list.get(6 - i - 1) >= 2)
                .forEach(i -> {
                    n1.getAndIncrement();
                    score1.addAndGet(6 - i);
                });

        return n1.get() == 2 ? score1.get() * 2 : 0;
    }

    public static int four_of_a_kind(int _1, int _2, int d3, int d4, int d5) {

        List<Integer> integers = zeros(6);
        List<Integer> list = inc(integers, _1, _2, d3, d4, d5);
        AtomicInteger result = new AtomicInteger();
        IntStream.range(0, 6)
                .filter(i -> list.get(i) >= 4)
                .findFirst()
                .ifPresent(a -> result.addAndGet((a + 1) * 4));

        return result.get();
    }

    public static int three_of_a_kind(int d1, int d2, int d3, int d4, int d5) {

        List<Integer> integers = zeros(6);
        List<Integer> list = inc(integers, d1, d2, d3, d4, d5);
        AtomicInteger result = new AtomicInteger();
        IntStream.range(0, 6)
                .filter(i -> list.get(i) >= 3)
                .findFirst()
                .ifPresent(a -> result.addAndGet((a + 1) * 3));

        return result.get();
    }

    public static int smallStraight(int d1, int d2, int d3, int d4, int d5) {

        List<Integer> integers = zeros(6);
        List<Integer> list = inc(integers, d1, d2, d3, d4, d5);
        list.remove(list.size() - 1); //remove Last
        return list.stream().allMatch(a -> a == 1) ? 15 : 0;
    }

    public static int largeStraight(int d1, int d2, int d3, int d4, int d5) {

        List<Integer> integers = zeros(6);
        List<Integer> list = inc(integers, d1, d2, d3, d4, d5);
        return list.stream().skip(1).allMatch(a -> a == 1) ? 20 : 0;
    }

    public static int fullHouse(int d1, int d2, int d3, int d4, int d5) {

        List<Integer> integers = zeros(6);
        List<Integer> list = inc(integers, d1, d2, d3, d4, d5);
        final AtomicBoolean found2 = new AtomicBoolean();
        final AtomicInteger nextOf2 = new AtomicInteger();
        list.stream()
                .filter(a -> a == 2)
                .reduce((a, b) -> b)
                .ifPresent(v -> {
                    found2.set(true);
                    nextOf2.addAndGet(list.indexOf(v) + 1);
                });

        final AtomicBoolean found3 = new AtomicBoolean();
        final AtomicInteger nextOf3 = new AtomicInteger();
        list.stream()
                .filter(a -> a == 3)
                .reduce((a, b) -> b)
                .ifPresent(v -> {
                    found3.set(true);
                    nextOf3.addAndGet(list.indexOf(v) + 1);
                });

        return found2.get() && found3.get() ? nextOf2.get() * 2 + nextOf3.get() * 3 : 0;
    }
}



