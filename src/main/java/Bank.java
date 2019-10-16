
import java.util.HashMap;
import java.util.Random;

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

  /**
   * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
   * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
   * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
   * усмотрение)
   */
  public void transfer(int fromAccountNum, int toAccountNum, int amount)
      throws InterruptedException {
    Account fromAccount = accounts.get(fromAccountNum);
    Account toAccount = accounts.get(toAccountNum);

    if (fromAccount.IsBlocked() || toAccount.IsBlocked()) {
      return;
    }

    if (fromAccount.withdrawMoney(amount)) {
      toAccount.putMoney(amount);
    }

    if (amount > 50000 && isFraud(fromAccountNum, toAccountNum, amount)) {
        fromAccount.blockAccount();
        toAccount.blockAccount();
    }
  }

  /**
   * TODO: реализовать метод. Возвращает остаток на счёте.
   */
  public long getBalance(int accountNum) {
    Account account = accounts.get(accountNum);
    return account.getBalance();
  }

  public HashMap<Integer, Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(HashMap<Integer, Account> accounts) {
    this.accounts = accounts;
  }

  private static HashMap<Integer, Account> fillAccounts() {
    HashMap<Integer, Account> accountMap = new HashMap<>();
    for (int i = 1; i <= 100; i++) {
      long initialValue = (long) (20000 + 80000 * Math.random());
      Account account = new Account(initialValue, i);
      accountMap.put(i, account);
    }
    return accountMap;
  }
}
