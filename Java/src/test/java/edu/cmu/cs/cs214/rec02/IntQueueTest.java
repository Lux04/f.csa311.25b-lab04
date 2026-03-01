package edu.cmu.cs.cs214.rec02;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class IntQueueTest {

    private IntQueue mQueue;
    private List<Integer> testList;

    @Before
    public void setUp() {
        mQueue = new ArrayIntQueue();
        testList = new ArrayList<>(List.of(1, 2, 3));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mQueue.isEmpty());
        assertEquals(0, mQueue.size());
        assertNull(mQueue.peek());
        assertNull(mQueue.dequeue());
    }

    @Test
    public void testNotEmpty() {
        assertTrue(mQueue.isEmpty());

        mQueue.enqueue(10);

        assertFalse(mQueue.isEmpty());
        assertEquals(1, mQueue.size());
        assertEquals(Integer.valueOf(10), mQueue.peek());
    }

    @Test
    public void testPeekEmptyQueue() {
        assertTrue(mQueue.isEmpty());
        assertNull(mQueue.peek());
    }

    @Test
    public void testPeekNoEmptyQueue() {
        mQueue.enqueue(1);
        mQueue.enqueue(2);

        assertEquals(Integer.valueOf(1), mQueue.peek());
        assertEquals(2, mQueue.size());
        assertEquals(Integer.valueOf(1), mQueue.peek());

        assertEquals(Integer.valueOf(1), mQueue.dequeue());
        assertEquals(Integer.valueOf(2), mQueue.peek());
        assertEquals(1, mQueue.size());
    }

    @Test
    public void testEnqueue() {
        for (int i = 0; i < testList.size(); i++) {
            mQueue.enqueue(testList.get(i));
            assertEquals(testList.get(0), mQueue.peek());
            assertEquals(i + 1, mQueue.size());
        }
    }

    @Test
    public void testDequeue() {
        assertNull(mQueue.dequeue());

        mQueue.enqueue(1);
        mQueue.enqueue(2);
        mQueue.enqueue(3);

        assertEquals(Integer.valueOf(1), mQueue.dequeue());
        assertEquals(Integer.valueOf(2), mQueue.dequeue());
        assertEquals(Integer.valueOf(3), mQueue.dequeue());

        assertTrue(mQueue.isEmpty());
        assertEquals(0, mQueue.size());
        assertNull(mQueue.dequeue());
    }

    @Test
    public void testContent() throws IOException {
        InputStream in = new FileInputStream("src/test/resources/data.txt");
        try (Scanner scanner = new Scanner(in)) {
            scanner.useDelimiter("\\s*fish\\s*");

            List<Integer> correctResult = new ArrayList<>();
            while (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                correctResult.add(input);
                System.out.println("enqueue: " + input);
                mQueue.enqueue(input);
            }

            for (Integer result : correctResult) {
                assertEquals(result, mQueue.dequeue());
            }
        }
    }

    @Test
    public void testEnsureCapacityWhenHeadNotZero() {
        for (int i = 0; i < 10; i++) {
            mQueue.enqueue(i);
        }

        for (int i = 0; i < 3; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }

        for (int i = 10; i < 13; i++) {
            mQueue.enqueue(i);
        }

        mQueue.enqueue(13);

        for (int expected = 3; expected <= 13; expected++) {
            assertEquals(Integer.valueOf(expected), mQueue.dequeue());
        }

        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testClear() {
        mQueue.enqueue(1);
        mQueue.enqueue(2);

        mQueue.clear();

        assertTrue(mQueue.isEmpty());
        assertEquals(0, mQueue.size());
        assertNull(mQueue.peek());
        assertNull(mQueue.dequeue());
    }

    @Test
    public void testDequeueEmpty() {
        assertNull(mQueue.dequeue());
    }

    @Test
    public void testResizeBranchFullyCovered() {
        for (int i = 0; i < 15; i++) {
            mQueue.enqueue(i);
        }

        for (int i = 0; i < 15; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
    }

    @Test
    public void testWrapAroundOrder() {
        for (int i = 0; i < 10; i++) mQueue.enqueue(i);

        for (int i = 0; i < 5; i++) assertEquals(Integer.valueOf(i), mQueue.dequeue());

        for (int i = 10; i < 15; i++) mQueue.enqueue(i);

        for (int i = 5; i < 15; i++) assertEquals(Integer.valueOf(i), mQueue.dequeue());
    }
}