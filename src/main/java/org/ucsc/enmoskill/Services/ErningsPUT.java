package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ErningsModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.module.ResolvedModule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class ErningsPUT {
    private ErningsModel erningsModel;
    private TokenService.TokenInfo tokenInfo;


    public ErningsPUT(ErningsModel erningsModel,TokenService.TokenInfo TokenInfo) {
        this.erningsModel = erningsModel;
        tokenInfo= TokenInfo;
    }

    public ResponsModel Run(double amount) throws SQLException, IOException {

        Connection connection = DatabaseConnection.initializeDatabase();


        Date Today= new Date();
        String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);

        String query = "SELECT t.all FROM enmo_database.withdrawal t WHERE userID = ? ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, tokenInfo.getUserId());
        ResultSet result1 = preparedStatement.executeQuery();
        double all=0;
        String query1=null;
        PreparedStatement preparedStatement1=null;

            double newamount = 0;
            if (!result1.next()) {
                if(isValiedAmaont(amount,0,connection)) {
                    newamount = amount;
                    query1 = "INSERT INTO enmo_database.withdrawal (`all`,date,lastWthdrawaiAmount,userID) VALUES (?, ?, ?, ?)";
                }else {
                    return new ResponsModel("Invalid Amount!", HttpServletResponse.SC_BAD_REQUEST);
                }

            } else {
                if(isValiedAmaont(amount,Double.parseDouble(result1.getString("all")),connection)) {
                    all = Double.parseDouble(result1.getString("all"));
                    newamount = all + amount;

                    query1 = "UPDATE enmo_database.withdrawal t  SET t.all = ?, t.date= ?,t.lastWthdrawaiAmount = ? WHERE userID =  ? ";
                }else {
                    return new ResponsModel("Invalid Amount!", HttpServletResponse.SC_BAD_REQUEST);
                }
            }

            preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setString(1, String.valueOf(newamount));
            preparedStatement1.setString(2, Date);
            preparedStatement1.setString(3, String.valueOf(amount));
            preparedStatement1.setString(4, tokenInfo.getUserId());

            int rowsAffected = preparedStatement1.executeUpdate();

            if (rowsAffected > 0) {
                return new ResponsModel("Withdrawal successfully!", HttpServletResponse.SC_OK);
            } else {
                return new ResponsModel("Withdrawal unsuccessfully!", HttpServletResponse.SC_NOT_IMPLEMENTED);
            }


    }


    public boolean isValiedAmaont(double amount,double all,Connection connection) throws SQLException {
//        ErningsGET erningsGET = new ErningsGET(tokenInfo);
//        String pval ="1";
//        erningsGET.Run(pval);
//        System.out.println(erningsModel.getAvailable());

        String query = "SELECT t.* FROM enmo_database.orders t WHERE designer_userID = ? ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, tokenInfo.getUserId());

        ResultSet result = preparedStatement.executeQuery();
        double available=0;
        while (result.next()){
            String date= String.valueOf(result.getDate("created_time"));
            double price = result.getInt("price");
            int status = result.getInt("status");

            if (status==4){
                double actualPrice = completPrice(price);
                available = available+actualPrice;
            }
        }
        if(amount <= (available-all)){
            return true;
        }else{
            return false;
        }

    }
    public double completPrice(double price){
        double actualPrice = price*0.9;
        return actualPrice;
    }
}
