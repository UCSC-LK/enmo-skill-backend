package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;

public class ValidateUserPOST {
    private HttpServletResponse response;
    private String key;
    public ValidateUserPOST( HttpServletResponse response,String key){
        this.key=key;
        this.response=response;
    }
    public boolean genarate(String UserID){
        String generatedString = RandomStringUtils.randomAlphanumeric(12);
        System.out.println(generatedString);
        return true;
    }
    public boolean Validate() throws SQLException, IOException {
        PreparedStatement preparedStatement;
        String query ="SELECT t.* FROM enmo_database.user_verify t WHERE `key`="+this.key;
        Connection connection = DatabaseConnection.initializeDatabase();
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int UserId = resultSet.getInt("userid");
            long time = resultSet.getLong("time");

            Date Today= new Date();
            long nowtime = Today.getTime();

            System.out.println("now :"+nowtime+"  created :"+time);

            if(nowtime-time>86400000){
                response.getWriter().write("Link Expired");
                response.setStatus(HttpServletResponse.SC_GONE);
                return true;
            }

            preparedStatement = connection.prepareStatement("SELECT t.* FROM enmo_database.users t WHERE userID ="+UserId);
            ResultSet resultSet2 = preparedStatement.executeQuery();
            if (resultSet2.next()) {
                int status = resultSet.getInt("status");
                if(status==0){
                    preparedStatement = connection.prepareStatement("UPDATE enmo_database.users t SET t.status = 1 WHERE t.userID ="+UserId);
                    int RowAffected = preparedStatement.executeUpdate();
                    if (RowAffected>0){
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("Successfully Verified!");
                        return true;
                    }
                }
                else if(status==1) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Email has already been verified");
                    return true;
                }
            }

        }
        return false;
    }

    public boolean resend(String email,String username){
        return true;
    }



}
