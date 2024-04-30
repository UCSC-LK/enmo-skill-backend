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
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
                String query = "SELECT t.*,o.created_time,o.package_id,p.percentage FROM enmo_database.earnings t JOIN enmo_database.orders o on o.order_id = t.orderID JOIN platform_charge_rates p on t.platform_charge_id = p.charge_category WHERE t.designerID = ? ORDER BY t.status";
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
                        all= Double.parseDouble(result1.getString("allWithdrawal"));
                        lastDate= result1.getString("lastWithdrawaldate");
                        lastAmount=result1.getDouble("lastWithdrawalAmount");

                    }

                    while (result.next()){
                        String date= String.valueOf(result.getDate("created_time"));
                        String comletedDate=result.getString("completedDate");
                        double presentage = result.getDouble("percentage");
                        double price = result.getInt("price");
                        int status = result.getInt("status");

                        if(status==1 || status==2){
                            double activePrice = activePrice(price,presentage);
                            active = active+activePrice;
                        } else if (status==3) {
                            double actualPrice = completPrice(price,presentage);
                            begin = begin+actualPrice;
                        }else if (status==5){
                            double actualPrice = completPrice(price,presentage);
                            available = available+actualPrice;
                        }
                    }
                    ErningsModel erningsModel =  new ErningsModel(available,active,begin,all,lastAmount,lastDate);
                    JsonObject jsonObject = gson.toJsonTree(erningsModel).getAsJsonObject();
                    jsonArray.add(jsonObject);
                }else {

                    while (result.next()){
                        boolean isActive = false;
                        long remainingDays = 0;
                        if(result.getInt("status")==3){
                            isActive =checkAvailable(String.valueOf(result.getDate("completedDate")));
                            if(!isActive){
                                LocalDate initialDate = LocalDate.parse(String.valueOf(result.getDate("completedDate")));
                                LocalDate newDate = initialDate.plusWeeks(2);
                                LocalDate currentDate = LocalDate.now();
                                remainingDays = ChronoUnit.DAYS.between(currentDate, newDate);
                            }
                        }
                        ErningsModel erningsModel = new ErningsModel(result,isActive,remainingDays);
                        JsonObject jsonObject = gson.toJsonTree(erningsModel).getAsJsonObject();
                        jsonArray.add(jsonObject);
                    }
                }

                return new ResponsModel(jsonArray.toString(),HttpServletResponse.SC_OK);

            } else if (tokenInfo.isClient()) {
                String query = "SELECT t.* FROM enmo_database.orders t WHERE client_userID = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, tokenInfo.getUserId());

                ResultSet result = preparedStatement.executeQuery();
                JsonArray jsonArray = new JsonArray();
                Gson gson = new Gson();

                while (result.next()){
                    ErningsModel erningsModel = new ErningsModel(result);
                    JsonObject jsonObject = gson.toJsonTree(erningsModel).getAsJsonObject();
                    jsonArray.add(jsonObject);
                }
                return new ResponsModel(jsonArray.toString(),HttpServletResponse.SC_OK);

            } else{
                return new ResponsModel("Invalid user ID",HttpServletResponse.SC_BAD_REQUEST);
            }

        }
    }

    public double completPrice(double price,double presentage){
//        LocalDate initialDate = LocalDate.parse(date);
//        LocalDate newDate = initialDate.plusWeeks(2);
//
//        LocalDate currentDate = LocalDate.now();
        double actualPrice = 0;
//        if (currentDate.isBefore(newDate)) {
            actualPrice = price*(1-presentage);
//        }
        return actualPrice;
    }

    public double activePrice(double price, double presentage){
        double active = price*(1-presentage);
        return active;
    }

    public boolean checkAvailable(String compleatedDate){
        if(compleatedDate==null){return false;}
        else {
            LocalDate initialDate = LocalDate.parse(compleatedDate);
            LocalDate newDate = initialDate.plusWeeks(2);
            LocalDate currentDate = LocalDate.now();
            if (newDate.isBefore(currentDate)) {
                return  true;
            }else return false;
        }
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
