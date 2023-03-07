package domain;

public class Account {

    private int account;

    public Account(final int account) {
        this.account = account;
    }

    public int account() {
        return account;
    }

    public void bust() {
        account = 0;
    }

    public void addAccount(final int winningMoney) {
        this.account += winningMoney;
    }

    public void subAccount(final int losingMoney) {
        account -= losingMoney;
    }
}
