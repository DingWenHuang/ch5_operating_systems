import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadsCollaborationDemo {

    private static Account account = new Account();//從Account class做一個新物件account
    private static class Account {
        //field
        private int balance = 0;//設定初始餘額為0
        private static Lock lock = new ReentrantLock();//從java.util.concurrent.locks.ReentrantLock做一個lock物件
        private static Condition newDeposit = lock.newCondition();//做一個newDeposit這個Condition跟lock有掛鉤
        public int getBalance() {//獲得現在的餘額數值
            return this.balance;
        }

        public void deposit(int amount) {//存錢的method
            lock.lock();
            balance += amount;
            System.out.println("Deposit $ " + amount + " the remaining balance is " + getBalance());
            newDeposit.signalAll();//去叫醒所有在等待的thread
            lock.unlock();
        }

        public void withdraw(int amount) {//提款的method
            lock.lock();
            try {
                while (balance < amount) {//當提款金額大於餘額時
                    System.out.println("Try to withdraw " + amount+ " We are waiting for a deposit");
                    newDeposit.await();//讓提款的thread await
                }
                balance -= amount;//如果成功提款就把餘額扣掉提款的金額
                System.out.println("Withdraw " + amount + " the remaining balance is " + getBalance());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public static class WithdrawTask implements Runnable {

        @Override
        public void run() {
            while (true) {//讓它一直去提款
                account.withdraw((int)(Math.random() * 20) + 5);//讓提款的金額為一個亂數
            }
        }
    }

    public static class DepositTask implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {//讓它一直去存錢
                    account.deposit((int)(Math.random() * 20) + 5);//讓存的金額為一個亂數
                    Thread.sleep(500);//故意讓他休息0.5秒
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new DepositTask());
        executorService.execute(new WithdrawTask());
        executorService.shutdown();
    }
}