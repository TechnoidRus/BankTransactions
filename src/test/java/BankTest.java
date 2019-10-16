import java.lang.annotation.Repeatable;
import java.util.HashMap;
import java.util.Set;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BankTest extends TestCase {
  Bank bank;
  HashMap<Integer, Account> acccounts = new HashMap<>();
  Account a1;
  Account a2;

  @Override
  @After
  protected void setUp() throws Exception {

   bank = new Bank();
   a1 = new Account(50000L,1);
   a2 = new Account(50000L,2);
   acccounts.put(1,a1);
   acccounts.put(2,a2);
   bank.setAccounts(acccounts);

  }

  @Before

  public void clearBank(){
    bank = null;
  }

  public void testTransferOneThread() throws InterruptedException {
    bank.transfer(1,2,1000);
    long actualFrom = a1.getBalance();
    long expectedFrom = 49000;
    long actualTo = a2.getBalance();
    long expectedTo = 51000;
    assertEquals(expectedFrom,actualFrom);
    assertEquals(expectedTo,actualTo);
  }

  public void testTransferManyThread() throws InterruptedException {
    for (int i = 0; i < 20; i++) {
      new Thread(() -> {
        try {
          bank.transfer(1, 2, 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    }
    Thread.sleep(50);
    long actualFrom = a1.getBalance();
    long expectedFrom = 30000;
    long actualTo = a2.getBalance();
    long expectedTo = 70000;
    assertEquals(expectedFrom,actualFrom);
    assertEquals(expectedTo,actualTo);
  }
  public void testTransferManyThreadRevers() {
    for (int i = 0; i <= 30; i++) {
      new Thread(() -> {
        try {
          bank.transfer(1, 2, 1000);
          bank.transfer(2, 1, 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    }
    long actualFrom = a1.getBalance();
    long expectedFrom = 50000;
    long actualTo = a2.getBalance();
    long expectedTo = 50000;
    assertEquals(expectedFrom,actualFrom);
    assertEquals(expectedTo,actualTo);
  }
}
