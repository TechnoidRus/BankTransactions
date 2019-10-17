
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Bank {

  private HashMap<Integer, Account> accounts;
  private final Random random = new Random();

  {
    accounts = fillAccounts();
  }

  private synchronized boolean isFraud(int fromAccountNum, int toAccountNum, long amount)
      throws InterruptedException {
    Thread.sleep(1000);
    return random.nextBoolean();
  }

  public void transfer(int fromAccountNum, int toAccountNum, int amount)
      throws InterruptedException {
    Account fromAccount = accounts.get(fromAccountNum);
    Account toAccount = accounts.get(toAccountNum);

    if (fromAccount.isBlocked() || toAccount.isBlocked()) {
      return;
    }

    transaction(amount, fromAccount, toAccount);

    if (amount > 50000) {
      if (isFraud(fromAccountNum, toAccountNum, amount)) {
        transaction(amount, toAccount, fromAccount);
        fromAccount.blockAccount();
        toAccount.blockAccount();
      }

    }
  }

  private void transaction(int amount, Account fromAccount, Account toAccount) {
    if (fromAccount.withdrawMoney(amount)) {
      toAccount.putMoney(amount);
    }
  }

  /**
   * TODO: реализовать метод. Возвращает остаток на счёте.
   */
  public long getBalance(int accountNum) {
    Account account = accounts.get(accountNum);
    return account.getBalance();
  }

  public void setAccounts(HashMap<Integer, Account> accounts) {
    this.accounts = accounts;
  }

  private static HashMap<Integer, Account> fillAccounts() {
    HashMap<Integer, Account> accountMap = new HashMap<>();
    for (int i = 1; i <= 100; i++) {
      long initialValue = (long) (80000 + 20000 * Math.random());
      Account account = new Account(new AtomicLong(initialValue), i);
      accountMap.put(i, account);
    }
    return accountMap;
  }
}
