package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.DesignerProfileModel;
import org.ucsc.enmoskill.model.Profile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DesignerProfile {

    private Profile profile;
    private HttpServletResponse resp;

    public DesignerProfile(Profile profile, HttpServletResponse resp) {
        this.profile = profile;
        this.resp = resp;
    }

    public void Run() throws IOException, SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();



        if(connection==null){
            resp.getWriter().write("SQL Connection Error");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
         if(profile.isDesigner()){
             if(profile.getUserId()==null){
                 resp.getWriter().write("User ID is Required!");
                 resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
             }else{
                String query="SELECT u.username,u.email,u.name,d.description,GROUP_CONCAT(DISTINCT l.language) AS languages, GROUP_CONCAT(DISTINCT s.skill) AS skills FROM users u JOIN designer d ON u.userid = d.userid LEFT JOIN language_mapping lm ON d.designerID = lm.designerID LEFT JOIN languages l ON lm.language_id = l.language_id LEFT JOIN skill_mapping sm ON d.designerID = sm.designerID LEFT JOIN skills s ON sm.skill_id = s.skill_id WHERE u.userid = "+ profile.getUserId();
                 PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery();

                 JsonObject jsonObject=null;
                 while(resultSet.next()){
                     DesignerProfileModel designerProfileModel = new DesignerProfileModel(resultSet);
                     jsonObject = new Gson().toJsonTree(designerProfileModel).getAsJsonObject();
                 }
                 resp.getWriter().write(jsonObject.toString());
             }
         }



    }


}
