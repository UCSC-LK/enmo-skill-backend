package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Order;

import java.sql.*;

public class OrderService {

    public double findFee(int orderId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        double result = 0;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            String query = "SELECT c.percentage FROM orders o "
                    +"JOIN client_charges c "
                    +"ON c.charge_category = o.platform_fee_id "
                    +"WHERE order_id=?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,orderId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                result = resultSet.getDouble("percentage");
//                System.out.println(resultSet.getDouble("percentage"));

            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public double calTotalPayAmount(Order order){

        double platformFee = findFee(order.getOrderId());

        double totalFee = order.getPrice() + order.getPrice()*platformFee;

        return totalFee;
    }

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
            preparedStatement.setDouble(7,order.getPlatformFeeId());


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

    public Order getOrderDetails(int orderId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT * FROM orders WHERE order_id = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,orderId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet!=null){

                Order order = new Order();

                while (resultSet.next()){
                    order.setOrderId(resultSet.getInt("order_id"));
                    order.setCreatedTime(resultSet.getTimestamp("created_time"));
                    order.setRequirements(resultSet.getString("requirements"));
                    order.setStatus(resultSet.getInt("status"));
                    order.setClientId(resultSet.getInt("client_userID"));
                    order.setDesignerId(resultSet.getInt("designer_userID"));
                    order.setPackageId(resultSet.getInt("package_id"));
                    order.setPrice(resultSet.getInt("price"));
                    order.setPlatformFeeId(resultSet.getInt("platform_fee_id"));
                }



                return order;

            }
            return  null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int updateOrder(Order order){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result;

        try {
            con = DatabaseConnection.initializeDatabase();

            String query = "UPDATE orders SET requirements=?, created_time=?, status=?, client_userID=?, designer_userID=?, package_id=?, price=?, platform_fee_id=? WHERE order_id=?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, order.getRequirements());
            preparedStatement.setTimestamp(2, order.getCreatedTime());
            preparedStatement.setInt(3, order.getStatus());
            preparedStatement.setInt(4, order.getClientId());
            preparedStatement.setInt(5, order.getDesignerId());
            preparedStatement.setInt(6, order.getPackageId());
            preparedStatement.setInt(7, order.getPrice());
            preparedStatement.setInt(8, order.getPlatformFeeId());
            preparedStatement.setInt(9, order.getOrderId());


            result = preparedStatement.executeUpdate();

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
