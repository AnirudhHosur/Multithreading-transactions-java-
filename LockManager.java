import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockManager {

    private Connection connection;
    private List<String> statements;
    private static final Lock lock = new ReentrantLock();

    public LockManager(List<String> statements, Connection connection) {
        this.statements = statements;
        this.connection = connection;
    }

    // Thanks to Cave of Programming to help me grasp the concept https://www.youtube.com/watch?v=fjMTaVykOpc
    void acquireLock() {
        synchronized (this) {
            while(true) {
                if (lock.tryLock()) {
                    lock.lock();
                    System.out.println("Lock acquired" + Thread.currentThread().getName());
                    break;
                } else {
                    try {
                        this.wait(1000);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    void operation() throws SQLException {
        System.out.println("Executing dB level operations" + Thread.currentThread().getName());
        Statement statement = connection.createStatement();
        for (String s : statements) {
            if (s.contains("SELECT")) {
                statement.executeQuery(s);
            } else if (s.contains("UPDATE")) {
                statement.executeUpdate(s);
            } else {
                System.out.println("Weird transaction.");
            }
        }
        connection.commit();
    }

    void releaseLock() throws IllegalMonitorStateException{
        synchronized (this) {
            System.out.println("Lock released " + Thread.currentThread().getName());
            lock.unlock();
            notifyAll();
        }
    }
}
