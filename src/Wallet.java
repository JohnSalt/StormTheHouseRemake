public class Wallet {
    private int money;

    public Wallet() {
        this.money = 0;
    }



    public int getMoney() {
        return money;
    }

    public void increaseMoney(int amount) {
        money += amount;
    }
    public void decreaseMoney(int amount) {
        money -= amount;
    }
}
