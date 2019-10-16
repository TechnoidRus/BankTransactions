import java.util.concurrent.atomic.AtomicBoolean;

public class Account {

  private Long balance;
  private int accNumber;
  private volatile AtomicBoolean isBlocked = new AtomicBoolean(false);

  public Account(Long balance, int accNumber) {
    this.balance = balance;
    this.accNumber = accNumber;
  }

  public long getBalance() {
    return balance;
  }

  public int getAccNumber() {
    return accNumber;
  }

  public boolean IsBlocked() {
    return isBlocked.get();
  }

  public synchronized boolean withdrawMoney(long money) {
    if (balance > money) {
      balance = balance - money;
      return true;
    }
    return false;
  }


  public synchronized void putMoney(long money) {
    balance += money;
  }

  public void blockAccount() {
    this.isBlocked.compareAndSet(false,true);
  }
}
