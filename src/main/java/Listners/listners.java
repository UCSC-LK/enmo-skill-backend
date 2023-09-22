package Listners;

import Classes.DatabaseConnectionManager;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class listners implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // This method is called when your web application is started.
        // You can perform initialization tasks here if needed.
        DatabaseConnectionManager.getConnection();
    }



    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // This method is called when your web application is shutting down.
        // Call the cleanup method here to close connections and unregister the JDBC driver.

            // Stop the abandoned connection cleanup thread
            try {
                AbandonedConnectionCleanupThread.checkedShutdown();
            } catch (Exception e) {
                System.out.println(e);// Handle any exception while stopping the thread
            }


//        DatabaseConnectionManager.closeConnection(DatabaseConnectionManager.getConnection());
    }
}
