import java.util.ArrayList;
import java.util.List;

public class CoinHolder extends Coin {
    public List<Coin> coins = new ArrayList<>();
    public CoinHolder(){
        coins = new ArrayList<>();
    }

    public CoinHolder(double value, String name) {
        super(value, name);
    }

    public Double sumCoins(){
        double sumOfCoins=0;
        for (Coin c : coins){
            sumOfCoins=c.getValue();
        }
        return sumOfCoins;
    }

    public void clearCoins(){
        coins.clear();
    }

}
