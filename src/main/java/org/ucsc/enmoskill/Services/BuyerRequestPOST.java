package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.Req_BRlist;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.System.out;

public class BuyerRequestPOST {
    private HttpServletResponse response;
    private BuyerRequestModel data;
    private TokenService.TokenInfo tokenInfo;
    public BuyerRequestPOST(HttpServletResponse response, BuyerRequestModel data, TokenService.TokenInfo TokenInfo){
        this.response=response;
        this.data=data;
        tokenInfo= TokenInfo;
    }

    public void Run() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            response.getWriter().write("SQL Connection Error");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        if(!tokenInfo.isClient()){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Token Invalid!");
            return;
        }
        this.data.setUserID(Integer.parseInt(tokenInfo.getUserId()));




        String query = this.data.getQuery("insert");

        try {
            PreparedStatement preparedStatement  = connection.prepareStatement(query);
            preparedStatement.setString(1, this.data.getTitle());
            preparedStatement.setString(2, this.data.getDiscription());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                response.getWriter().write("Data inserted successfully!");
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
                response.getWriter().write("Data Inserting Failed!");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
}
