package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupportPOST {
    private SupprtModel supportObj;
    private HttpServletResponse response;

    public SupportPOST(SupprtModel supportObj, HttpServletResponse response) {
        this.supportObj = supportObj;
        this.response = response;
    }

    public ResponsModel Run() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }else {
            String query = this.supportObj.getQuery();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
//                    response.getWriter().write("Data inserted successfully!");
//                    response.setStatus(HttpServletResponse.SC_CREATED);
                    return new ResponsModel("Data inserted successfully!",HttpServletResponse.SC_CREATED);
                } else {
//                    response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
//                    response.getWriter().write("Data Inserting Failed!");
                    return new ResponsModel("Data Inserting Failed!",HttpServletResponse.SC_NOT_IMPLEMENTED);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
