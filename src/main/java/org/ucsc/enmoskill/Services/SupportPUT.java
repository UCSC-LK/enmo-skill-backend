package org.ucsc.enmoskill.Services;

import com.google.gson.stream.JsonToken;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SupportPUT {
    private SupprtModel supportObj;


    public SupportPUT(SupprtModel supportObj) {
        this.supportObj = supportObj;
       // this.response = response;
    }

    public ResponsModel Run() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);


        }else {
            String query1 = this.supportObj.setHistoryData();
            String query2 = this.supportObj.getUpdatedQuery();

            try {
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                int rowsAffected1 = preparedStatement1.executeUpdate();
                int rowsAffected2=0;

                if(rowsAffected1>0){
                    PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                    rowsAffected2 = preparedStatement2.executeUpdate();
                }


                if (rowsAffected2 > 0) {

//                    response.getWriter().write("Data Updated successfully!");
//                    response.setStatus(HttpServletResponse.SC_CREATED);
                    return new ResponsModel("Data Updated successfully!",HttpServletResponse.SC_CREATED);

                } else {

                    String deleteQuery = "DELETE FROM enmo_database.ticket_history WHERE ticket_id = " + supportObj.getRef_no();
                    Statement deleteStatement = connection.createStatement();
                    deleteStatement.executeUpdate(deleteQuery);

//                    response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
//                    response.getWriter().write("Data Updating Failed!");
                    return new ResponsModel("Data Updating Failed!",HttpServletResponse.SC_NOT_IMPLEMENTED);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

