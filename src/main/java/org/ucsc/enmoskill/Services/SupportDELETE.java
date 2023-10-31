package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupportDELETE {
    private String requesterID;
    public SupportDELETE(String requesterID, HttpServletResponse response) throws SQLException, IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        String quary = "DELETE FROM enmo_Skill.ticket WHERE ref_no = " + requesterID;
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
