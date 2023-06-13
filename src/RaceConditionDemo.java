import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RaceConditionDemo {

    private static Account account = new Account();
    private static class Account {
        private int balance = 0;
        private static Lock lock = new ReentrantLock();
        public int getBalance() {
            return this.balance;
        }

        public void deposit() {
            lock.lock();
            int newBalance = this.balance + 1 ;
            try {
                Thread.sleep(3);
                this.balance = newBalance;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class AddMoneyTask implements Runnable {

        @Override
        public void run() {
            account.deposit();
        }
    }
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        //create 300 task
        for (int i = 0; i < 300; i++) {
            executorService.execute(new AddMoneyTask());
        }
        executorService.shutdown();//停止接收更多的任務
        while (!executorService.isTerminated()) {

        }

        System.out.println("The balance is " + account.getBalance());
        //經過上鎖的功能後，結果可以正確輸出300
    }
}