
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RaceConditionDemo {

    private static Account account = new Account();
    private static class Account {
        private int balance = 0;

        public int getBalance() {
            return this.balance;
        }

        public void deposit() {
            int newBalance = this.balance + 1 ;
            try {
                Thread.sleep(3);//休眠3毫秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.balance = newBalance;
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

    }
}
