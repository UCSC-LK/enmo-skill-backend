package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupportDELETE {
    private String TicketID;
    private HttpServletResponse response;
    private TokenService.TokenInfo tokenInfo;
    public SupportDELETE(String TicketID,TokenService.TokenInfo TokenInfo, HttpServletResponse response) throws SQLException, IOException {
        Connection connection = DatabaseConnection.initializeDatabase();

        String quary = "DELETE FROM enmo_database.ticket WHERE ref_no = " + TicketID+" AND requesterID = "+tokenInfo.getUserId();

        PreparedStatement preparedStatement = connection.prepareStatement(quary);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {

            response.getWriter().write("Ticket deleted successfully");
            response.setStatus(HttpServletResponse.SC_OK);
        } else {

            response.getWriter().write("Not found ticket.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
