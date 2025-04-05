package first_task;

public class EvenOddPrinter {
    private static final Object lock = new Object();
    private static int count = 1; // Начнем с 1 для вывода нечетных чисел

    public static void main(String[] args) {
        Thread evenThread = new Thread(new EvenPrinter());
        Thread oddThread = new Thread(new OddPrinter());

        evenThread.start();
        oddThread.start();
    }

    static class EvenPrinter implements Runnable {
        @Override
        public void run() {
            while (count <= 20) { // Выводить числа до 20
                synchronized (lock) {
                    if (count % 2 == 0) {
                        System.out.println(count++);
                    }
                    lock.notify(); // Уведомляем другой поток
                    try {
                        lock.wait(); // Ожидаем уведомления от другого потока
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    static class OddPrinter implements Runnable {
        @Override
        public void run() {
            while (count <= 20) { // Выводить числа до 20
                synchronized (lock) {
                    if (count % 2 != 0) {
                        System.out.println(count++);
                    }
                    lock.notify(); // Уведомляем другой поток
                    try {
                        lock.wait(); // Ожидаем уведомления от другого потока
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}