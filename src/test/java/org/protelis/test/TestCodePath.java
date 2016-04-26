package org.protelis.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Random;
import java8.util.stream.IntStreams;

import org.junit.Test;
import org.protelis.vm.util.CodePath;

import gnu.trove.list.array.TByteArrayList;

/**
 *
 */
public class TestCodePath {

    private static final long[] MASKS = new long[] {
            0xFFL,
            0xFF00L,
            0xFF0000L,
            0xFF000000L,
            0xFF00000000L,
            0xFF0000000000L,
            0xFF000000000000L,
            0xFF00000000000000L };

    private static void initCodePathAndTest(final byte... input) {
        final CodePath underTest = new CodePath(new TByteArrayList(input));
        final long[] res = underTest.asLongArray();
        for (int i = 0; i < input.length; i++) {
            final int lidx = i / (Long.SIZE / Byte.SIZE);
            final int midx = i % (Long.SIZE / Byte.SIZE);
            assertEquals((byte) ((res[lidx] & MASKS[midx]) >>> (midx * 8)), input[i]);
        }
    }

    /**
     * 
     */
    @Test
    public void test() {
        final Random rnd = new Random(0);
        IntStreams.range(0, 1000).forEach(i -> {
            final byte[] test = new byte[i];
            for (int j = 0; j < test.length; j++) {
                test[j] = (byte) rnd.nextInt();
            }
            Arrays.toString(test);
            initCodePathAndTest(test);
        });
    }

}
