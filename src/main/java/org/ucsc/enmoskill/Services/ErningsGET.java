package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ErningsModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ErningsGET {
    private TokenService.TokenInfo tokenInfo;
    private  double available,begin,active;

    public ErningsGET(TokenService.TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public ResponsModel Run(String pval) throws IOException, SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();


        if (connection == null) {

//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } else {
            if(tokenInfo.isDesigner()){
                String query = "SELECT t.* FROM enmo_database.orders t WHERE designer_userID = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, tokenInfo.getUserId());

                ResultSet result = preparedStatement.executeQuery();
                JsonArray jsonArray = new JsonArray();
                Gson gson = new Gson();

                if(pval != null){
                    String query1 = "SELECT t.* FROM enmo_database.withdrawal t WHERE userID = ? ";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                    preparedStatement1.setString(1, tokenInfo.getUserId());
                    ResultSet result1 = preparedStatement1.executeQuery();
                    double all=0;
                    double lastAmount=0;
                    String lastDate=null;
                    if (!result1.next()) {
                        all = 0;
                    } else {
                        all= Double.parseDouble(result1.getString("all"));
                        lastDate= result1.getString("date");
                        lastAmount=result1.getDouble("lastWthdrawaiAmount");

                    }

                    while (result.next()){
                        String date= String.valueOf(result.getDate("created_time"));
                        double price = result.getInt("price");
                        int status = result.getInt("status");

                        if(status==1 || status==2){
                            double activePrice = activePrice(price);
                            active = active+activePrice;
                        } else if (status==3) {
                            double actualPrice = completPrice(price,date);
                            begin = begin+actualPrice;
                        }else if (status==4){
                            double actualPrice = completPrice(price,date);
                            available = available+actualPrice;
                        }
                    }
                    ErningsModel erningsModel =  new ErningsModel(available,active,begin,all,lastAmount,lastDate);
                    JsonObject jsonObject = gson.toJsonTree(erningsModel).getAsJsonObject();
                    jsonArray.add(jsonObject);
                }else {
                    while (result.next()){
                        ErningsModel erningsModel = new ErningsModel(result);
                        JsonObject jsonObject = gson.toJsonTree(erningsModel).getAsJsonObject();
                        jsonArray.add(jsonObject);
                    }
                }

                return new ResponsModel(jsonArray.toString(),HttpServletResponse.SC_OK);

            }else{
                return new ResponsModel("Invalid user ID",HttpServletResponse.SC_BAD_REQUEST);
            }

        }
    }

    public double completPrice(double price,String date){
        LocalDate initialDate = LocalDate.parse(date);
        LocalDate newDate = initialDate.plusWeeks(2);

        LocalDate currentDate = LocalDate.now();
        double actualPrice = 0;
        if (newDate.isBefore(currentDate)) {
            actualPrice = price*0.9;
        }
        return actualPrice;
    }

    public double activePrice(double price){
        double active = price*0.9;
        return active;
    }

//    public double available(double price,int status,String date){
//        LocalDate initialDate = LocalDate.parse(date);
//        LocalDate newDate = initialDate.plusWeeks(2);
//
//        LocalDate currentDate = LocalDate.now();
//        double availablePrice = 0;
//        if (newDate.isBefore(currentDate)) {
//            availablePrice = price*0.9;
//        }
//        return availablePrice;
//    }

}
