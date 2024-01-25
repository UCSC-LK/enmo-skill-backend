package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuyerRequestDELETE {
    private String requestID;
    public BuyerRequestDELETE(String requestID, HttpServletResponse response) throws SQLException, IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        String quary ="DELETE FROM enmo_database.jobs WHERE requestID = "+requestID;
        PreparedStatement preparedStatement = connection.prepareStatement(quary);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {

            response.getWriter().write("Row is deleted successfully.");
            response.setStatus(HttpServletResponse.SC_OK);
        } else {

            response.getWriter().write("No row found .");
        }
    }


}
