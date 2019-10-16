import java.lang.management.ManagementFactory;
import java.util.HashMap;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    System.out.println(ManagementFactory.getThreadMXBean().getThreadCount());
    Bank bank = startProcessing();
    Thread.sleep(10000);
    System.out.println(ManagementFactory.getThreadMXBean().getThreadCount());
    print(bank);
  }

  private static Bank startProcessing() {
    Bank bank = new Bank();
    for (int i = 1; i < 1000; i++) {

      int from = (int) (1 + 50 * Math.random());
      int to = (int) (50 + 45 * Math.random());
      int amount = (int) (40000 + 11000 * Math.random());
      new Thread(() -> {
        try {
          bank.transfer(from, to, amount);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    }
    return bank;
  }

  private static void print(Bank bank) {
    HashMap<Integer, Account> accounts = bank.getAccounts();

    for (Integer i : accounts.keySet()) {
      Account account = accounts.get(i);
      if (!account.IsBlocked())
        System.out.printf("Счет %s\nБаланс счета -> %d\n\n",account.getAccNumber(),bank.getBalance(account.getAccNumber()));
      else
        System.out.printf("Cчет %s\nСчет заблокирован за сомнительные операции, обратитесь в банк.\n\n",account.getAccNumber());
    }
  }
}



