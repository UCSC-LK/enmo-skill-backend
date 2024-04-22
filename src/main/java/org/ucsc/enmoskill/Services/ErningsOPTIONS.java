package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ErningsOPTIONS {
    private TokenService.TokenInfo tokenInfo;
    public ErningsOPTIONS(TokenService.TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public ResponsModel Run(String orderID) throws SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            return new ResponsModel("SQL Connection Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }else{
            String query1 = "SELECT t.created_time,t.status FROM orders t WHERE t.order_id="+orderID;
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            ResultSet resultSet = preparedStatement1.executeQuery();

            if(resultSet.next()){
                String date = resultSet.getString("created_time");
                int status = resultSet.getInt("status");

                LocalDate initialDate = LocalDate.parse(date);
                LocalDate newDate = initialDate.plusWeeks(2);

                LocalDate currentDate = LocalDate.now();
                if (newDate.isBefore(currentDate) && status==3) {

                    String query ="UPDATE enmo_database.orders t SET t.status=4 WHERE t.order_id=? AND t.designer_userID=?";
                    preparedStatement1.setString(1, orderID);
                    preparedStatement1.setString(2, tokenInfo.getUserId());
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    int row = preparedStatement.executeUpdate(query);

                    if(row>0){
                        return new ResponsModel("Money added to acount!", HttpServletResponse.SC_OK);
                    }else{
                        return new ResponsModel("Money added failed!", HttpServletResponse.SC_NOT_IMPLEMENTED);
                    }
                }else{
                    return new ResponsModel("Not Available!", HttpServletResponse.SC_BAD_REQUEST);
                }

            }else{
                return new ResponsModel("Cannot find order!", HttpServletResponse.SC_BAD_REQUEST);
            }


        }
    }
}
