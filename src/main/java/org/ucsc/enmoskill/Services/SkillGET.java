package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Skills;
import org.ucsc.enmoskill.model.SupprtModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillGET {
    HttpServletResponse res;

    public SkillGET(HttpServletResponse res) {
        this.res = res;
    }

    public void Run() throws IOException, SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            res.getWriter().write("SQL Connection Error");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        String query = "SELECT (skill) FROM enmo_database.skills";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet result = preparedStatement.executeQuery();

        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();

       while (result.next()) {
           Skills skills = new Skills(result);
            JsonObject jsonObject = gson.toJsonTree(skills).getAsJsonObject();
            jsonArray.add(jsonObject);
       }

        res.getWriter().write(jsonArray.toString());
        res.setStatus(HttpServletResponse.SC_OK);
    }

}


