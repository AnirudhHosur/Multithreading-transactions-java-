import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ArrayList<String> transaction1 = new ArrayList<>();
        transaction1.add("SELECT * FROM research");
        transaction1.add("UPDATE research set research = 'Chem' where researchId = 1");
        transaction1.add("SELECT * FROM research");

        ArrayList<String> transaction2 = new ArrayList<>();
        transaction2.add("SELECT * FROM research");
        transaction2.add("UPDATE research set research = 'Bio' where researchId = 1");
        transaction2.add("SELECT * FROM research");

        ArrayList<ArrayList<String>> transaction = new ArrayList<>();
        transaction.add(transaction1);
        transaction.add(transaction2);

        for (List<String> t : transaction) {
            Thread execute = new Transaction(t);
            execute.start();
        }
    }
}