import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachine extends CoinHolder{
    public List<Drink> drinks = new ArrayList<>();
    private List<Coin> coins = new ArrayList<>();
    private List<String> Sdrinks = new ArrayList<>();
    private Drink selectedDrink;
    private double rest;
    private double rest2;
    private Drink dr;
    private String adminMenuID = "haslo123";
    private String chosedDrink="";


    public VendingMachine() {
    this.drinks=new ArrayList<>();

    }

    //Pobiera listę dostępnych napojów i zapisuje je w liście
    public void getDatabaseDrinks(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection =
                    DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/automat-baza", "root", "");
            if (connection==null){
                System.out.println("Brak połączenia z BD!");
            }
            else {

                statement = connection.createStatement();
                String sql = "SELECT * FROM available_drinks";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String drinkName = resultSet.getString("drinkName");
                    Double drinkPrice = resultSet.getDouble("drinkPrice");
                    this.drinks.add(new Drink(drinkName, drinkPrice));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                statement.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void convertToStringList(List<Drink> drinks){
        for (Drink d : this.drinks){
            Sdrinks.add(d.getNname());
        }
    }

    //Wyświetla listę dostępnych napojów
    public void showDrinks(){
        convertToStringList(this.drinks);
        for (Drink d : drinks){
            System.out.println(d.toString());
        }

    }
    public void greetings(){
        System.out.println("Witaj w automacie z napojami!\nOto cennik:\n");
    }

    //Prosi o wybranie napoju z listy
    public void getDrinkFromUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWybierz napój:");
        String choice=scanner.nextLine();
        if(adminMenuID.equals(choice)){
            chosedDrink=choice;
            return;
        }
        if(Sdrinks.contains(choice)){
            for (Drink d : this.drinks){
                if(d.getNname().equals(choice)){
                    System.out.println("Świetnie! Twój wybrany napój to: "+choice + ". Jego cena wynosi: "+d.getPrice()+" zł");
                    setSelectedDrink(d);
                    dr=new Drink(d.getNname(), d.getPrice());

                }
            }
        }
        else {
            while (!Sdrinks.contains(choice)){
                System.out.println("Taki napój jest niedostępny! Podaj nowy: ");
                choice=scanner.nextLine();
                for (Drink d : this.drinks){
                    if(d.getNname().equals(choice)){
                        System.out.println("Świetnie! Twój wybrany napój to: "+choice + ". Jego cena wynosi: "+d.getPrice()+" zł");
                        setSelectedDrink(d);
                        dr=new Drink(d.getNname(), d.getPrice());
                    }
                }
            }
        }
    }
    public void insertCoin() {
        //Przygotowanie danych
        Scanner scanner = new Scanner(System.in);
        System.out.println("Możesz wrzucić monetę z zakresu od 10 groszy do 5 złotych ");
        List<Coin> coins = new ArrayList<>();
        List<Integer> possZ = new ArrayList<>();
        List<Integer> possG = new ArrayList<>();
        List<String> currency = new ArrayList<>();
        possZ.add(1);
        possZ.add(2);
        possZ.add(5);
        possG.add(10);
        possG.add(20);
        possG.add(50);
        currency.add("grosz");
        currency.add("zloty");

        // Sprawdzenie czy została wprowadzona wystarczająca ilość monet
        if(coins.stream().mapToDouble(Coin::getValue).sum()<selectedDrink.getPrice()) {
            System.out.println("Suma wprowadzonych monet: " + coins.stream().mapToDouble(Coin::getValue).sum());
            while (coins.stream().mapToDouble(Coin::getValue).sum()<selectedDrink.getPrice()) {

        //Implementacja waluty
        System.out.println("Podaj walutę (grosz lub zloty):");
        String userType = scanner.nextLine();
        if (!currency.contains(userType)) {
            while (!currency.contains(userType)) {

                userType = scanner.nextLine();
            }
        }
                //Implementacja monety
                System.out.println("Podaj wartość monety:");
                int userCoin = scanner.nextInt();
                double d;
                if (userType.equals("zloty")) {
                    while (!possZ.contains(userCoin)) {
                        System.out.println("Podaj prawidłową monetę: ");
                        userCoin = scanner.nextInt();
                    }
                    System.out.println("Zatwierdzono!");
                    d = userCoin;
                    coins.add(new Coin(d, userType));
                    System.out.println("Suma wprowadzonych monet: " +coins.stream().mapToDouble(Coin::getValue).sum());
                } else if (userType.equals("grosz")) {
                    while (!possG.contains(userCoin)) {
                        System.out.println("Podaj prawidłową monetę: ");
                        userCoin = scanner.nextInt();
                    }
                    System.out.println("Zatwierdzono!");
                    d = userCoin;
                    coins.add(new Coin(d/100, userType));
                    System.out.println("Suma wprowadzonych monet: " +coins.stream().mapToDouble(Coin::getValue).sum());

                }
            }
        }
        if (coins.stream().mapToDouble(Coin::getValue).sum()>=selectedDrink.getPrice()) {
            System.out.println("Wprowadzona liczba monet jest wystarczająca! ");
            rest=coins.stream().mapToDouble(Coin::getValue).sum()-selectedDrink.getPrice();
            rest2=coins.stream().mapToDouble(Coin::getValue).sum();
            coins.clear();
        }
    }

    public Drink getSelectedDrink() {
        return selectedDrink;
    }

    public void setSelectedDrink(Drink selectedDrink) {
        this.selectedDrink = selectedDrink;
    }

    //Prosi o potwierdzenie transakcji lub jej anulowanie
    public boolean isBought() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Czy zatwierdzić transakcję? ('Tak' lub 'Nie')");
        String decision = scan.nextLine();
        if(decision.equals("Tak")){
            System.out.println("Dziękujemy za zakup! Oto Twoja reszta: " + rest);
            return true;

        }
        else {
            System.out.println("Szkoda! Oto zwrot monet: " + rest2);
            return false;
        }
    }

    public void startNewTransaction(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Czy chcesz rozpocząć nową transakcję?");
        String str = scan.nextLine();
        while(!str.equals("Tak") && !str.equals("Nie")){
            System.out.println("Czy chcesz rozpocząć nową transakcję?");
            System.out.println("Podaj swój wybór('Tak' lub 'Nie')");
            str = scan.nextLine();
        }
        if(str.equals("Tak")){

        }
        else {
            System.out.println("Koniec programu!");
            System.exit(1);
        }
    }

    public void setChosedDrink(String chosedDrink) {
        this.chosedDrink = chosedDrink;
    }

    public String getAdminMenuID() {
        return adminMenuID;
    }

    public String getChosedDrink() {
        return chosedDrink;
    }
}



