package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ProfileModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ProfilePOST {
    private ProfileModel profileModel;
    private TokenService.TokenInfo tokenInfo;
//    HttpServletResponse res;

    public ProfilePOST(ProfileModel profileModel,TokenService.TokenInfo tokenInfo) {
        this.profileModel = profileModel;
        this.tokenInfo = tokenInfo;
//        this.res = res;
    }

    public ResponsModel Run() throws IOException, SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
//            res.getWriter().write("SQL Connection Error");
//            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        profileModel.setUserId(Integer.parseInt(tokenInfo.getUserId()));

        //update user role---------------------------------------------------------------------------
//        String queryRoleLevelUp = profileModel.getQueryLevelUp();
//        PreparedStatement preparedStatement = connection.prepareStatement(queryRoleLevelUp);
//        int rows = preparedStatement.executeUpdate();
//        preparedStatement.close();

        //set designer table details-----------------------------------------------------------------------------
        String query1 = profileModel.getQuery1();

        PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
        int rows1 = preparedStatement1.executeUpdate();
        preparedStatement1.close();

        //set skills--------------------------------------------------------------------------------------------
        String query2 = profileModel.getQuery2();
        boolean skillFlag = false;

        System.out.println(profileModel.getUserId());
        for(int i = 0; i< profileModel.getSkills().size(); i++){
            if(skillFlag){
                query2 = query2+",";
            }
            query2 = query2+"("+ profileModel.getUserId()+","+ profileModel.getSkills().get(i)+")";
            skillFlag = true;
        }

        PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
        int rows2 = preparedStatement2.executeUpdate();
//        preparedStatement2.close();


        //get language details-----------------------------------------------------------------------------------
        PreparedStatement preparedStatementLanguage = connection.prepareStatement("SELECT t.* FROM enmo_database.languages t");
        ResultSet resultSet = preparedStatementLanguage.executeQuery();

        Map<String, Integer> languageIdMap = new HashMap<>();

        while (resultSet.next()) {
            // Retrieve values for each column
            int column1Value = resultSet.getInt("language_id");
            String column2Value = resultSet.getString("language");
            languageIdMap.put(column2Value, column1Value);
            // Add more lines for additional columns as needed

            // Print values
            System.out.println("Column1: " + column1Value + ", Column2: " + column2Value);
            // Add more lines for additional columns as needed
        }
        resultSet.close();
//        preparedStatementLanguage.close();

        //set language details------------------------------------------------------------------------------------------
        String query3 = profileModel.getQuery3();

        boolean languageFlag = false;
        for (String language : profileModel.getLanguage()) {
            // Get the ID from the map
            Integer id = languageIdMap.get(language);

            // Check if the language is present in the map
            if (id != null) {
                System.out.println("Language: " + language + ", ID: " + id);
                if(languageFlag){
                    query3 = query3+",";
                }
                query3 = query3+"("+ profileModel.getUserId()+","+id+")";
                languageFlag = true;

            } else {
                System.out.println("Language not found: " + language);
            }
        }

        int rows3 = 0;
        if(languageFlag){
            PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
            System.out.println(query3);
            rows3 = preparedStatement3.executeUpdate();
//            preparedStatement3.close();
        }

        if (rows1 > 0 && rows2 > 0 && rows3 > 0) {
//            res.getWriter().write("Data inserted successfully!");
//            res.setStatus(HttpServletResponse.SC_CREATED);
            return new ResponsModel("Data inserted successfully!",HttpServletResponse.SC_CREATED);
        } else {
//            res.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
//            res.getWriter().write("Data Inserting Failed!");
            return new ResponsModel("Data Inserting Failed!",HttpServletResponse.SC_NOT_IMPLEMENTED);
        }

    }
}
