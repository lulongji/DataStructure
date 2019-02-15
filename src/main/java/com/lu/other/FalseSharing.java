package com.lu.other;

/**
 * Created by lu on 2019/2/15.
 */
public final class FalseSharing implements Runnable {

    /**
     * cpu核数
     */
    public final static int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public final static long ITERATIONS = 500L * 1000L * 1000L;

    private final int arrayIndex;

    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

    static {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
    }

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }


    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }

    @Override
    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;

        }
    }

    public final static class VolatileLong {
        public volatile long value = 0L;
        public long p1, p2, p3, p4, p5, p6;
    }

    /**
     * jdk7 解决方案
     */

    /**
     * public static class VolatileLong extends AtomicLong {
     * <p>
     * public volatile long p1, p2, p3, p4, p5, p6 = 7L;
     * }
     * <p>
     * <p>
     * abstract class AbstractPaddingObject {
     * protected long p1, p2, p3, p4, p5, p6;
     * }
     * <p>
     * public class VolatileLong extends AbstractPaddingObject {
     * public volatile long value = 0L;
     * }
     **/

    public static void main(final String[] args) throws Exception {
        final long start = System.nanoTime();
        runTest();
        System.out.println("duration = " + (System.nanoTime() - start));
    }
}