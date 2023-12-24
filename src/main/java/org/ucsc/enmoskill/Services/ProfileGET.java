package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.DesignerProfileModel;
import org.ucsc.enmoskill.model.ProfileModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileGET {

    private ProfileModel profileModel;
    private HttpServletResponse resp;

    public ProfileGET(ProfileModel profileModel, HttpServletResponse resp) {
        this.profileModel = profileModel;
        this.resp = resp;
    }

    public void Run() throws IOException, SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();

        if (connection == null) {
            resp.getWriter().write("SQL Connection Error");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        if (profileModel.isDesigner()) {

          String query = "SELECT  designer.userid,designer.display_name, designer.description," +
                  "GROUP_CONCAT(DISTINCT skills.skill) AS skills," +
                  "GROUP_CONCAT(DISTINCT languages.language) AS language " +
                  "FROM  designer " +
                  "LEFT JOIN skill_mapping ON designer.userId = skill_mapping.UserID " +
                  "LEFT JOIN skills ON skill_mapping.skill_id = skills.skill_id " +
                  "LEFT JOIN language_mapping ON designer.userId = language_mapping.userId " +
                  "LEFT JOIN languages ON language_mapping.language_id = languages.language_id " +
                  "WHERE designer.userId ="+ profileModel.getUserId()+  " GROUP BY designer.userId";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//                preparedStatement.setInt(1, profileModel.getUserId());
                ResultSet resultSet = preparedStatement.executeQuery();

                JsonObject jsonObject = new JsonObject();

                while (resultSet.next()) {

                    ProfileModel profileModel = new ProfileModel(resultSet);
                    jsonObject = new Gson().toJsonTree(profileModel).getAsJsonObject();

                }

                resp.getWriter().write(jsonObject.toString());
                System.out.println(resp);
            }



        }


    }
}
