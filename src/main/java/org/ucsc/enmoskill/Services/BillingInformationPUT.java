package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BillingInformationModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BillingInformationPUT {
    private BillingInformationModel billinInforObject;
    private TokenService.TokenInfo tokenInfo;

    public BillingInformationPUT(BillingInformationModel billinInforObject, TokenService.TokenInfo TokenInfo) {
        this.billinInforObject = billinInforObject;
        tokenInfo= TokenInfo;
        // this.response = response;
    }

    public ResponsModel Run() throws IOException, SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if (connection == null) {
//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);


        } else {
            String query = "UPDATE enmo_database.billingInfor " +
                            "SET fname = ?, lname = ?, phoneNo = ?, email = ?, address = ?, city = ?, country = ? " +
                            "WHERE userID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, billinInforObject.getFname());
            preparedStatement.setString(2, billinInforObject.getLname());
            preparedStatement.setString(3, billinInforObject.getpNumber());
            preparedStatement.setString(4, billinInforObject.getEmail());
            preparedStatement.setString(5, billinInforObject.getAddress());
            preparedStatement.setString(6, billinInforObject.getCity());
            preparedStatement.setString(7, billinInforObject.getCountry());
            preparedStatement.setInt(8, Integer.parseInt(tokenInfo.getUserId()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return new ResponsModel("Data update successfully!",HttpServletResponse.SC_CREATED);
            } else {
                return new ResponsModel("Data updste Failed!",HttpServletResponse.SC_NOT_IMPLEMENTED);
            }
        }
    }

}
