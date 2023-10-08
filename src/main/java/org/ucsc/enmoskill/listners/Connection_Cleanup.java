package org.ucsc.enmoskill.listners;


import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Connection_Cleanup implements ServletContextListener {




    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // This method is called when server is shutting down.


        // Stop the abandoned connection cleanup thread
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            System.out.println(e);
        }

        // DatabaseConnectionManager.closeConnection(DatabaseConnectionManager.getConnection());
    }
}