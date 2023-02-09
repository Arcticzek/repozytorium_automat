public class Coin {
    private double value;
    private String name;


    public Coin() {

    }

    public Coin(double value, String name){
        this.value=value;
        this.name=name;
    }


    public double getValue(){
        return value;
    }
    public String name(){
        return name;
    }


}
