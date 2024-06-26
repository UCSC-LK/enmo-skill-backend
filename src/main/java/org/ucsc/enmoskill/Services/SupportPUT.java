package org.ucsc.enmoskill.Services;

import com.google.gson.stream.JsonToken;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupportPUT {
    private SupprtModel supportObj;
    private TokenService.TokenInfo tokenInfo;


    public SupportPUT(SupprtModel supportObj,TokenService.TokenInfo TokenInfo) {
        this.supportObj = supportObj;
        tokenInfo= TokenInfo;
       // this.response = response;
    }

    public ResponsModel Run() throws IOException, SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);


        }else {
            supportObj.setRequesterID(Integer.parseInt(tokenInfo.getUserId()));
            String query1=null;
            int flag =0;
            if(tokenInfo.isAdmin()){
                query1 = this.supportObj.setReply2();
            } else{
                query1 = this.supportObj.setReply();
            }

            if(tokenInfo.isAgent() || tokenInfo.isAdmin()){flag=1;}


            Date Today= new Date();
            String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);

            PreparedStatement preparedStatement = connection.prepareStatement(query1);
//            preparedStatement.setInt(1, supportObj.getRef_no());

            preparedStatement.setString(1, supportObj.getDescription());
            preparedStatement.setString(2, Date);
            preparedStatement.setString(3, tokenInfo.getUserId());

            int rowsAffected = preparedStatement.executeUpdate();


//            if(rowsAffected1>0){
//
//                Date Today= new Date();
//                String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);
//
//                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
//                preparedStatement2.setString(1, supportObj.getDescription());
//                preparedStatement2.setString(2, supportObj.getSubject());
//                preparedStatement2.setString(3, Date);
//
//                rowsAffected2 = preparedStatement2.executeUpdate();
//            }

            if (rowsAffected>0) {
                if(flag==1){
                    String userId=null;
                    String ticketId=null;

                    String query = "SELECT t.requesterID FROM ticket t WHERE t.ref_no="+supportObj.getRef_no();
                    PreparedStatement preparedStatement1 = connection.prepareStatement(query);

                    ResultSet resultSet = preparedStatement1.executeQuery();
                    while (resultSet.next()){
                        userId = resultSet.getString("requesterID");
                    }

                    ticketId = String.valueOf(supportObj.getRef_no());
                    SendEmail sendEmail = new SendEmail(ticketId,userId);
                    sendEmail.setdata("reply");
                }
//                    response.getWriter().write("Data Updated successfully!");
//                    response.setStatus(HttpServletResponse.SC_CREATED)
                return new ResponsModel("Data Updated successfully!",HttpServletResponse.SC_CREATED);

            } else {

//                String deleteQuery = "DELETE FROM enmo_database.ticket_history WHERE ticketID = " + supportObj.getRef_no();
//                Statement deleteStatement = connection.createStatement();
//                deleteStatement.executeUpdate(deleteQuery);

//                    response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
//                    response.getWriter().write("Data Updating Failed!");
                return new ResponsModel("Data Updating Failed!",HttpServletResponse.SC_NOT_IMPLEMENTED);

            }

        }
    }
}

