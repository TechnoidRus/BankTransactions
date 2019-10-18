
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;


public class BankTest extends TestCase {

  private Bank bank;
  private HashMap<Integer, Account> accounts = new HashMap<>();
  private Account a1;
  private Account a2;
  private Account a3;
  private Account a4;
  private Account a5;

  @Override
  @After
  public void setUp() throws Exception {

    bank = new Bank();
    a1 = new Account(new AtomicLong(50000L), 1);
    a2 = new Account(new AtomicLong(50000L), 2);
    a3 = new Account(new AtomicLong(50000L), 3);
    a4 = new Account(new AtomicLong(60000L), 4);
    a5 = new Account(new AtomicLong(60000L), 5);
    accounts.put(1, a1);
    accounts.put(2, a2);
    accounts.put(3, a3);
    accounts.put(4, a4);
    accounts.put(5, a5);

    bank.setAccounts(accounts);

  }

  @Before
  public void clearBank() {
    bank = null;
  }

  public void testTransferOneThread() throws InterruptedException {
    bank.transfer(1, 2, 1000);
    long actualFrom = a1.getBalance();
    long expectedFrom = 49000;
    long actualTo = a2.getBalance();
    long expectedTo = 51000;
    assertEquals(expectedFrom, actualFrom);
    assertEquals(expectedTo, actualTo);
  }

  public void testTransferManyThread() throws InterruptedException {
    for (int i = 0; i < 10; i++) {
      new Thread(() -> {
        try {
          bank.transfer(1, 2, 1000);
          bank.transfer(1, 3, 1000);
          bank.transfer(3, 1, 1000);
          bank.transfer(3, 2, 1000);
          bank.transfer(2, 1, 1000);
          bank.transfer(2, 3, 1000);
          bank.transfer(1, 3, 1000);
          bank.transfer(2, 3, 1000);

        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    }
    Thread.sleep(100);
    long actualA1 = a1.getBalance();
    long expectedA1 = 40000;
    long actualA2 = a2.getBalance();
    long expectedA2 = 40000;
    long actualA3 = a3.getBalance();
    long expectedA3 = 70000;

    assertEquals(expectedA1, actualA1);
    assertEquals(expectedA2, actualA2);
    assertEquals(expectedA3, actualA3);
  }

  public void testTransferManyThreadRevers() throws InterruptedException {
    for (int i = 0; i <= 30; i++) {
      Thread t = new Thread(() -> {
        try {
          bank.transfer(1, 2, 1000);
          bank.transfer(2, 1, 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      t.start();
      t.join();
    }
    long actualFrom = a1.getBalance();
    long expectedFrom = 50000;
    long actualTo = a2.getBalance();
    long expectedTo = 50000;
    assertEquals(expectedFrom, actualFrom);
    assertEquals(expectedTo, actualTo);
  }

  public void testTransferBlock() throws InterruptedException {
    long balance = a1.getBalance();
    a1.setIsBlocked(new AtomicBoolean(true));
    bank.transfer(1, 2, 1000);
    bank.transfer(1, 2, 1000);
    bank.transfer(1, 2, 1000);
    long actualA1 = bank.getBalance(1);

    assertEquals(balance, actualA1);
  }

  public void testIfFraud() throws InterruptedException {
    for (int i = 0; i < 4; i++) {
      Thread t = new Thread(() -> {
        try {
          bank.transfer(4, 5, 51000);
          bank.transfer(5, 4, 51000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      t.start();
      t.join();
    }
    boolean actualFrom = a4.isBlocked();
    boolean actualTo = a5.isBlocked();

    assertTrue(actualFrom);
    assertTrue(actualTo);
  }

  public void testTransferOverLimit() throws InterruptedException {
    bank.transfer(1, 2, 100000);
    long actualFrom = a1.getBalance();
    long expectedFrom = 50000;
    long actualTo = a2.getBalance();
    long expectedTo = 50000;
    assertEquals(expectedFrom, actualFrom);
    assertEquals(expectedTo, actualTo);
  }

  public void testDoubleWithdrawal() throws InterruptedException {
    for (int i = 0; i < 4; i++) {
      Thread t = new Thread(() -> {
        try {
          bank.transfer(1, 2, 50000);
          bank.transfer(1, 2, 50000);
          bank.transfer(1, 2, 50000);
          bank.transfer(1, 2, 50000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      t.start();
      t.join();

      long expected = a1.getBalance();
      long actual = 0;
      assertEquals(expected,actual);
    }
  }


}
