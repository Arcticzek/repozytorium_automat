import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class adminMenu extends VendingMachine{
    private Drink drink;
    private String  adminChoice;
    private VendingMachine v=new VendingMachine();
    private String enterKey;

    public adminMenu(){

        Transaction t = new Transaction();
    }

    //Wybór czynności z poziomu menu administratora
    public void whatAction()  {
        Scanner scan=new Scanner(System.in);
        System.out.println("Witaj w admin menu! Co chcesz zrobić?(Wybierz odpowiedni numer)\n1. Zobacz listę transakcji.\n2. Dodaj nowy napój." +
                "\n3. Usuń istniejący napój.\n4. Zmień cenę napoju.\n Aby wyjść z menu admina naciśnij 'enter'");
        adminChoice = scan.nextLine();

        while((!adminChoice.equals("1") && !adminChoice.equals("2") && !adminChoice.equals("3") && !adminChoice.equals("4")) && !adminChoice.equals("")){
            System.out.println("Wybierz odpowiedni numer\n1. Zobacz listę transakcji.\n2. Dodaj nowy napój.\n3. Usuń istniejący napój.\n4. Zmień cenę napoju.");
            adminChoice=scan.nextLine();
        }
        if(adminChoice.equals("1")){
            showTransactions();
        } else if (adminChoice.equals("2")) {
            addDrink();
        }
        else if(adminChoice.equals("3")){
            deleteDrink();
        }
        else if (adminChoice.equals("4")){
            changeDrinkPrice();
        }
        else {};
    }
    // Dodaje nowy napój do bazy danych
    public void addDrink(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Wprowadź nazwę napoju:");
        String name=scan.nextLine();
        System.out.println("Wprowadź cenę: ");
        String price=scan.nextLine();
        double price2=Double.parseDouble(price);
        while(price2<=0){
            System.out.println("Cena nie może być ujemna! Wprowadź cenę: ");
            price=scan.nextLine();
            price2=Double.parseDouble(price);
        }
        System.out.println("Zostanie dodany napój: " + name + " w cenie: " + price+"zł\n Wpisz 'Tak' aby potwierdzić operację.");
        String czyDodac=scan.nextLine();
        if(czyDodac.equals("Tak")) {

            Connection connection = null;
            Statement statement = null;

            try {
                connection =
                        DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/automat-baza", "root", "");
                if (connection == null) {
                    System.out.println("Brak połączenia z BZ!");
                } else {
                    statement = connection.createStatement();
                    String sql = "INSERT INTO available_drinks (drinkName, drinkPrice) VALUES\n" +
                            "('" + name + "','" + price2 + "')";
                    int rows = statement.executeUpdate(sql);
                    System.out.println("Dodano napój: "+name);
                }
            } catch (
                    SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                try {
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (!czyDodac.equals("Tak")){
            System.out.println("Anulowano");
        }
        System.out.println("Wciśnij 'enter', aby powrócić do menu admina.");
        String enterkey = scan.nextLine();
        if(enterkey.equals("")){
            whatAction();
        }
    }



    // Usuwa wybrany napój z bazy danych
    public void deleteDrink(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj nazwę napoju do usunięcia: ");
        String drinkToDelete=scan.nextLine();
        System.out.println("Czy napewno chcesz usunąć?('Tak')");
        String str = scan.nextLine();
        if(str.equals("Tak")) {
            Connection connection = null;
            try {
                connection =
                        DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/automat-baza", "root", "");
                if (connection == null) {
                    System.out.println("Brak połączenia z BZ!");
                } else {
                    String sql = "DELETE FROM available_drinks WHERE drinkName = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, drinkToDelete);
                    int rows = statement.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Usunięto napój: "+drinkToDelete);
                    } else {
                        System.out.println("Napój o takiej nazwie nie istnieje!");
                    }
                }


            } catch (
                    SQLException e) {
                e.printStackTrace();
            }
        } else if(!str.equals("Tak")){
            System.out.println("Anulowano!");
        }
        System.out.println("Wciśnij 'enter', aby powrócić do menu admina.");
        String enterkey = scan.nextLine();
        if(enterkey.equals("")){
            whatAction();
        }
    }

    //Zmienia cenę napoju w bazie danych
    public void changeDrinkPrice(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj nazwę napoju do zmiany ceny: ");
        String drinkToChange=scan.nextLine();
        System.out.println("Wprowadź nową cenę: ");
        String price = scan.nextLine();
        Double price2=Double.parseDouble(price);
        while(price2<0){
            System.out.println("Cena nie może być ujemna! Wprowadź nową cenę: ");
            price = scan.nextLine();
            price2=Double.parseDouble(price);
        }
        System.out.println("Czy napewno chcesz zmienić cenę?('Tak')");
        String str = scan.nextLine();
        if(str.equals("Tak")) {
            Connection connection = null;
            try {
                connection =
                        DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/automat-baza", "root", "");
                if (connection == null) {
                    System.out.println("Brak połączenia z BZ!");
                } else {
                    String sql = "UPDATE available_drinks SET drinkPrice = ? WHERE drinkName = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setDouble(1, price2);
                    statement.setString(2, drinkToChange);
                    int rows = statement.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Cena napoju "+drinkToChange+" została zmieniona na "+ price2+"zł");
                    } else {
                        System.out.println("Napój o takiej nazwie nie istnieje!");
                    }
                }


            } catch (
                    SQLException e) {
                e.printStackTrace();
            }
        } else if(!str.equals("Tak")){
            System.out.println("Anulowano!");
        }
        System.out.println("Wciśnij 'enter', aby powrócić do menu admina.");
        String enterkey = scan.nextLine();
        if(enterkey.equals("")){
            whatAction();
        }
    }


    //Wyświetla listę transakcji z bazy danych
    public void showTransactions(){
        System.out.println("Oto lista transakcji: ");
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
                String sql = "SELECT * FROM transactions";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String drinkName = resultSet.getString("drinkname");
                    Double drinkPrice = resultSet.getDouble("drinkprice");
                    String date = resultSet.getString("date");
                    System.out.println(drinkName+", "+drinkPrice+"zł "+date);
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
        Scanner scan = new Scanner(System.in);
        System.out.println("Wciśnij 'enter', aby powrócić do menu admina.");
        String enterkey = scan.nextLine();
        if(enterkey.equals("")){
            whatAction();
        }
    }
}
