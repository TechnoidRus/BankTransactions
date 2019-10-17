import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Account {

  private AtomicLong balance;
  private int accNumber;
  private AtomicBoolean isBlocked = new AtomicBoolean(false);

  public Account(AtomicLong balance, int accNumber) {
    this.balance = balance;
    this.accNumber = accNumber;
  }

  public long getBalance() {
    return balance.get();
  }

  public void setIsBlocked(AtomicBoolean isBlocked) {
    this.isBlocked = isBlocked;
  }

  public boolean isBlocked() {
    return isBlocked.get();
  }

  public synchronized boolean withdrawMoney(long money) {
    if (balance.get() >= money) {
      balance.updateAndGet(b -> b - money);
      return true;
    }
    return false;
  }


  public synchronized void putMoney(long money) {
    balance.updateAndGet(b -> b + money);
  }

  public void blockAccount() {
    isBlocked.getAndSet(true);
  }
}
