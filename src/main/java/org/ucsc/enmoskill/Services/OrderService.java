package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Order;

import java.sql.*;

public class OrderService {

    public Order setFee(Order order){

        int price = order.getPrice();

        if (price<1000){
            order.setPlatformFeeId(1);
        } else {
            order.setPlatformFeeId(2);
        }

        return order;
    }

    public int createOrder(Order order){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO orders (requirements, status, client_userID, designer_userID, package_id, price, platform_fee_id)"
                    + "VALUES (?,?,?,?,?,?,?);";
            preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,"");
            preparedStatement.setInt(2,0);
            preparedStatement.setInt(3,order.getClientId());
            preparedStatement.setInt(4,order.getDesignerId());
            preparedStatement.setInt(5,order.getPackageId());
            preparedStatement.setInt(6,order.getPrice());
            preparedStatement.setInt(7,order.getPlatformFeeId());


            result = preparedStatement.executeUpdate();

            if (result > 0){
                // Retrieve the auto-generated keys (including the primary key)
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);

                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
