import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Transaction extends Thread {

    public Transaction(List<String> statements) {
        this.statements = statements;
    }

    private List<String> statements;

    public List<String> getStatements() {
        return statements;
    }

    public void run() {
        Connection connection = null;
        LockManager lockManager = null;
        try {
            DbConnection connector = new DbConnection();
            connection = connector.connect();
            System.out.println("Connection established " + Thread.currentThread().getName());
            System.out.println("Current thread - start " + Thread.currentThread().getName());
            lockManager = new LockManager(statements, connection);
            lockManager.acquireLock();
            lockManager.operation();
            lockManager.releaseLock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockManager.releaseLock();
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
