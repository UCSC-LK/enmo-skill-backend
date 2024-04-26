package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Order;
import org.ucsc.enmoskill.model.ProposalModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class OrderService {

    private HttpServletResponse resp;

    public OrderService(HttpServletResponse resp){
        this.resp = resp;
    }

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
            String query = "INSERT INTO orders (requirements, status, client_userID, designer_userID, package_id, price, platform_fee_id , proposalID , price_package_id , deliveryDuration)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?);";
            preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, order.getRequirements());
            preparedStatement.setInt(2,0);
            preparedStatement.setInt(3,order.getClientId());
            preparedStatement.setInt(4,order.getDesignerId());
            preparedStatement.setInt(5,order.getPackageId());
            preparedStatement.setInt(6,order.getPrice());
            preparedStatement.setDouble(7,order.getPlatformFeeId());
            preparedStatement.setDouble(8,order.getProposalID());
            preparedStatement.setInt(9,order.getPricePackageID());
            preparedStatement.setInt(10, order.getDeliveryDuration());
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

    public int createCustomOrder(Order order){
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

    public void getOrderDetails(int orderId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT * FROM orders WHERE order_id = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,orderId);

            resultSet = preparedStatement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (resultSet.next()) {
                Order order = new Order(resultSet);
                JsonObject jsonObject = gson.toJsonTree(order).getAsJsonObject();
                jsonArray.add(jsonObject);
            }

            resp.getWriter().write(jsonArray.toString());
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }

    }

    public void getAllDesignerOrderDetails(int designer_userID) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Order order;
        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT * FROM orders WHERE designer_userID = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, designer_userID);

            resultSet = preparedStatement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (resultSet.next()) {
                order = new Order(resultSet);
                JsonObject jsonObject = gson.toJsonTree(order).getAsJsonObject();
                jsonArray.add(jsonObject);
            }

            resp.getWriter().write(jsonArray.toString());
            resp.setStatus(HttpServletResponse.SC_OK);
            System.out.println("Orders : " + jsonArray);

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAllClientOrderDetails(int client_userID) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Order order;
        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT * FROM orders WHERE client_userID = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, client_userID);

            resultSet = preparedStatement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (resultSet.next()) {
                order = new Order(resultSet);
                JsonObject jsonObject = gson.toJsonTree(order).getAsJsonObject();
                jsonArray.add(jsonObject);
            }

            resp.getWriter().write(jsonArray.toString());
            resp.setStatus(HttpServletResponse.SC_OK);
            System.out.println("Orders : " + jsonArray);

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

        public int updateOrder (Order order){
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

        public int deleteOrder ( int orderId){
            Connection con = null;
            PreparedStatement preparedStatement = null;
            int result;

            try {
                con = DatabaseConnection.initializeDatabase();
                String query = "DELETE FROM orders WHERE order_id = ?";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, orderId);
                result = preparedStatement.executeUpdate();

                return result;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
    }
