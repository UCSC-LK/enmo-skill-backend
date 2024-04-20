package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BillingInformationModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BillingInformationPOST {
    private BillingInformationModel billObject;
    private TokenService.TokenInfo tokenInfo;
    public BillingInformationPOST(BillingInformationModel billingInformationModel, TokenService.TokenInfo tokenInfo) {
        this.billObject = billingInformationModel;
        this.tokenInfo= tokenInfo;
    }

    public ResponsModel Run() throws SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }else {
            String query = "INSERT INTO enmo_database.billingInfor (userID,fname,lname,phoneNo,email,address,city,country) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tokenInfo.getUserId());
            preparedStatement.setString(2, billObject.getFname());
            preparedStatement.setString(3, billObject.getLname());
            preparedStatement.setString(4, billObject.getpNumber());
            preparedStatement.setString(5, billObject.getEmail());
            preparedStatement.setString(6, billObject.getAddress());
            preparedStatement.setString(7, billObject.getCity());
            preparedStatement.setString(8, billObject.getCountry());

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
        }
    }
}
