package multi_threaded_programming;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerAndConsumerDemo {
    private static Buffer buffer = new Buffer();
    private static class Buffer {
        private static final int CAPACITY = 1;
        private LinkedList<Integer> queue = new LinkedList<>();
        private static Lock lock = new ReentrantLock();
        private static Condition notEmpty = lock.newCondition();
        private static Condition notFull = lock.newCondition();

        public void write(int value) {
            lock.lock();
            try {
                while (queue.size() == CAPACITY) {
                    notFull.await();
                }
                queue.offer(value);
                System.out.println("Producer write " + value);
                notEmpty.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void read() {
            lock.lock();
            int value;
            try {
                while (queue.isEmpty()) {
                    notEmpty.await();
                }
                value = queue.remove();
                System.out.println("Consumer read " + value);
                notFull.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }


    private static class ProduceTask implements Runnable {

        @Override
        public void run() {
            try {
                int i = 0;
                while (true) {
                    buffer.write(i++);
                    Thread.sleep((int)(Math.random() * 1000));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ConsumeTask implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    buffer.read();
                    Thread.sleep((int)(Math.random() * 3000));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.execute(new ConsumeTask());
        executorService.execute(new ProduceTask());
        executorService.shutdown();
    }
}