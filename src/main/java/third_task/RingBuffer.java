package third_task;

import java.util.Arrays;

public class RingBuffer<T> {
    private final T[] buffer;
    private int head;
    private int tail;
    private int count;

    @SuppressWarnings("unchecked")
    public RingBuffer(int size) {
        this.buffer = (T[]) new Object[size];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (count == buffer.length) {
            wait(); // Ожидание, пока не появится место
        }

        buffer[tail] = item;
        tail = (tail + 1) % buffer.length;
        count++;
        notifyAll(); // Уведомление ожидающих потоков
    }

    public synchronized T take() throws InterruptedException {
        while (count == 0) {
            wait(); // Ожидание, пока не появятся данные
        }

        T item = buffer[head];
        head = (head + 1) % buffer.length;
        count--;
        notifyAll(); // Уведомление ожидающих потоков
        return item;
    }

    public synchronized int size() {
        return count;
    }

    public synchronized boolean isEmpty() {
        return count == 0;
    }

    public synchronized boolean isFull() {
        return count == buffer.length;
    }

    @Override
    public String toString() {
        return "RingBuffer{" +
                "buffer=" + Arrays.toString(buffer) +
                ", head=" + head +
                ", tail=" + tail +
                ", count=" + count +
                '}';
    }
}
