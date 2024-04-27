package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.OrderDetailsModel;
import org.ucsc.enmoskill.model.ProposalModel;
import org.ucsc.enmoskill.model.ReviewModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetailsService {

    private HttpServletResponse resp;
    public OrderDetailsService(HttpServletResponse resp){
        this.resp = resp;
    }

    public  void GetOrderDetails(Connection connection , int order_id ){
        PreparedStatement preparedStatement = null;
        ResultSet result = null; // Add this line to initialize the result set

        System.out.println("User ID: " + order_id);
        try{
            String query = "SELECT * FROM order_details WHERE order_id = ? ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, order_id); // Set the userID parameter
            result = preparedStatement.executeQuery();
            System.out.println("Run: ");

            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (result.next()) {
                OrderDetailsModel orderDetails = new OrderDetailsModel(result);
                JsonObject jsonObject = gson.toJsonTree(orderDetails).getAsJsonObject();
                jsonArray.add(jsonObject);
            }

            resp.getWriter().write(jsonArray.toString());
            resp.setStatus(HttpServletResponse.SC_OK);
            System.out.println("OrderDetails : " + jsonArray);

        }catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }


    public boolean isDesignerOrderDetailsInsertionSuccessful(OrderDetailsModel orderDetailsModel){

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO order_details (order_id,client_userID,designer_userID,deliver_work,client_message,designer_message) VALUES (?,?,?,?,?,?)";
            assert con != null;
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, orderDetailsModel.getOrderID());
            preparedStatement.setInt(2, orderDetailsModel.getClientId());
            preparedStatement.setInt(3, orderDetailsModel.getDesignerId());
            preparedStatement.setString(4, orderDetailsModel.getDeliver_work());
            preparedStatement.setString(5, orderDetailsModel.getClient_message());
            preparedStatement.setString(6, orderDetailsModel.getDesigner_message());
            preparedStatement.executeUpdate(); // Execute the INSERT operation

            return true;
        }catch (SQLException e) {
            // Handle any exceptions that might occur during the insertion
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isClientOrderDetailsInsertionSuccessful(OrderDetailsModel orderDetailsModel){

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO order_details (order_id,client_userID,designer_userID, client_message) VALUES (?,?,?,?)";
            assert con != null;
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, orderDetailsModel.getOrderID());
            preparedStatement.setInt(2, orderDetailsModel.getClientId());

            // Query to retrieve client_userID from request table based on requestID
            String DesignerUserIDQuery = "SELECT designer_userID FROM orders WHERE order_id = ?";
            PreparedStatement DesignerUserIDStatement = con.prepareStatement(DesignerUserIDQuery);
            DesignerUserIDStatement.setInt(1, orderDetailsModel.getOrderID());
            ResultSet DesignerUserIDResult = DesignerUserIDStatement.executeQuery();

            if (DesignerUserIDResult.next()) {
                int designer_userID = DesignerUserIDResult.getInt("designer_userID");
                preparedStatement.setInt(3, designer_userID);
            } else {
                System.out.println("No NOOO");
                // Handle the case where no client_userID is found for the given requestID
            }

            preparedStatement.setString(4, orderDetailsModel.getClient_message());
            preparedStatement.executeUpdate(); // Execute the INSERT operation

            return true;
        }catch (SQLException e) {
            // Handle any exceptions that might occur during the insertion
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
