public class Main {

  public static void main(String[] args) {

    Bank bank = new Bank();
    for (int i = 1; i < 100; i++) {
      int from = (int) (1 + 50 * Math.random());
      int to = (int) (50 + 45 * Math.random());
      int amount = (int) (10000 + 45000 * Math.random());
      Thread t = new Thread(() -> {
        try {
          bank.transfer(from, to, amount);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      t.start();
    }
  }
}



