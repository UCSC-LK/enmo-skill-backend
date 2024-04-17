package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentService {

    public int savePaymentDetails(Payment newPayment){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try{
            con = DatabaseConnection.initializeDatabase();

            String query = "INSERT INTO payment(amount, order_id) VALUES(?,?);";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setDouble(1,newPayment.getAmount());
            preparedStatement.setInt(2, newPayment.getOrderId());

            result = preparedStatement.executeUpdate();

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
