package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupportPOST {
    private SupprtModel supportObj;
    private TokenService.TokenInfo tokenInfo;


    public SupportPOST(SupprtModel supportObj,TokenService.TokenInfo TokenInfo) {
        this.supportObj = supportObj;
        tokenInfo= TokenInfo;
        //this.response = response;
    }

    public ResponsModel Run() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }else {

            supportObj.setRequesterID(Integer.parseInt(tokenInfo.getUserId()));

            String query = this.supportObj.getQuery();

            try {
                Date Today= new Date();
                String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, supportObj.getDescription());
                preparedStatement.setString(2, Date);
                preparedStatement.setInt(3, supportObj.getRequesterID());
                preparedStatement.setString(4, supportObj.getSubject());

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
