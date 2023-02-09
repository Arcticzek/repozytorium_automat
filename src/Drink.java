import java.util.List;

public class Drink {
    private String drinkName;
    private double price;


    public Drink(String drinkName, double price){
        this.drinkName=drinkName;
        this.price=price;

    }
    private int quantity;
    public Drink(String drinkName, double price, int quantity){
        this.drinkName=drinkName;
        this.price=price;
        this.quantity=quantity;
    }

    public String getNname() {
        return drinkName;
    }

    public double getPrice() {
        return price;
    }


    @Override
    public String toString() {
        return
                "Drink: " + drinkName +
                ", price: " + price + "zł";
    }




}
