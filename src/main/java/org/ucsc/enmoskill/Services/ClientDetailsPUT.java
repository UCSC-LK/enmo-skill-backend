package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class ClientDetailsPUT {
    private HttpServletResponse response;
    private User data;

    public ClientDetailsPUT(HttpServletResponse response, User data) {
        this.response = response;
        this.data = data;
    }

    public void Run() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            response.getWriter().write("SQL Connection Error");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }



        try {
            String userQuery = data.getInsertUserDetails();
            String clientQuery = data.getInsertClientDetails();

            PreparedStatement massageStatement = connection.prepareStatement(userQuery);
            int affectedRows = massageStatement.executeUpdate();

            if (affectedRows == 0) {
                response.getWriter().write("Inserting message failed, no rows affected.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new SQLException("Inserting userdata failed, no rows affected.");
            }

            PreparedStatement chatMappingStatement = connection.prepareStatement(clientQuery);
            int ClientdataAffected = chatMappingStatement.executeUpdate();
                    if (ClientdataAffected == 0) {
                        response.getWriter().write("Inserting client data failed, no rows affected.");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        throw new SQLException("Inserting client data failed, no rows affected.");

                    }
                    else {
                        response.getWriter().write("Successfully updated!");
                        response.setStatus(HttpServletResponse.SC_CREATED);
                    }


        } catch (SQLException e) {
            response.getWriter().write(e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }
}
