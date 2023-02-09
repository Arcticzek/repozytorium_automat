import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction  {
    private String name;
    private double price;
    private String time;
    Date data=new Date();


    public Transaction(){

        SimpleDateFormat sDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time=sDF.format(data);
    }
    public Transaction(String name, double price){
        SimpleDateFormat sDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.name=name;
        this.price=price;
        this.time=sDF.format(data);

    }

    @Override
    public String toString() {
        return "Transaction{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", time='" + time + '\'' +
                '}';
    }


    //Dodaje do bazy nową transakcję o parametrach: nazwa, cena, czas w jakim została zatwierdzona transakcja
    public void addTransactionToFile(Transaction d){
    Connection connection = null;
    Statement statement = null;

        try {
        connection =
                DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/automat-baza", "root", "");
        if (connection==null){
            System.out.println("Brak połączenia z BZ!");
        }
        else {

            statement = connection.createStatement();
            String sql = "INSERT INTO transactions (drinkname, drinkprice, date) VALUES\n" +
                    "('" + d.name + "','" + d.price + "','" + d.time + "')";
            statement.executeUpdate(sql);
        }



    } catch (
    SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {

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

}
