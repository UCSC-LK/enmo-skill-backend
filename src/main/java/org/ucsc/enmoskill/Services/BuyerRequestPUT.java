package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BuyerRequestModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BuyerRequestPUT {
    private HttpServletResponse response;
    private BuyerRequestModel data;
    public BuyerRequestPUT(HttpServletResponse response, BuyerRequestModel data){
        this.response=response;
        this.data=data;
    }

    public void Run() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            response.getWriter().write("SQL Connection Error");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }


        String query = this.data.getQuery("update");

        try {
            PreparedStatement preparedStatement  = connection.prepareStatement(query);
            preparedStatement.setString(1, this.data.getTitle());
            preparedStatement.setString(2, this.data.getDiscription());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                response.getWriter().write("Data Updated successfully!");
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
                response.getWriter().write("Data Updating Failed!");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

}
