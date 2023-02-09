import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        VendingMachine v1=new VendingMachine();
        Transaction t1 = new Transaction();
        adminMenu am1 = new adminMenu();
        while(true) {
            //Wyświetla powitanie
            v1.greetings();
            //Pobiera listę dostępnych napojów z bazy
            v1.getDatabaseDrinks();
            //Wyświetla dostępne napoje
            v1.showDrinks();
            //Użytkownik wybiera napój
            v1.getDrinkFromUser();
            //Jeśli użytkownik wprowadził odpowiedni ciąg znaków to wyświetla się admin panel:
            if (v1.getChosedDrink().equals(v1.getAdminMenuID())) {
                v1.drinks.clear();
                am1.whatAction();
                v1.setChosedDrink("");
            } else if (!v1.getChosedDrink().equals(v1.getAdminMenuID())) {
                //Użytkownik wprowadza monety, a automat zlicza ich ilość
                v1.insertCoin();
                //Użytkownik potwierdza lub anuluje transakcję
                if (v1.isBought()) {
                    v1.drinks.clear();
                    //Jeśli użytkownik potwierdzi transakcję to do bazy jest dodawana transakcja
                    t1.addTransactionToFile(new Transaction(v1.getSelectedDrink().getNname(), v1.getSelectedDrink().getPrice()));
                    v1.startNewTransaction();
                }
                else v1.startNewTransaction();
            }
        }
    }

}