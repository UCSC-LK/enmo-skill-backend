package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.SupprtModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupportPUT {
    private SupprtModel supportObj;
    private HttpServletResponse response;

    public SupportPUT(SupprtModel supportObj, HttpServletResponse response) {
        this.supportObj = supportObj;
        this.response = response;
    }

    public void Run() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            response.getWriter().write("SQL Connection Error");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
        String query = this.supportObj.getUpdatedQuery();

        try {
            PreparedStatement preparedStatement  = connection.prepareStatement(query);
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

