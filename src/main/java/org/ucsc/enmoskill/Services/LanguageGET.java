package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.LanguageModel;
import org.ucsc.enmoskill.model.Skills;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LanguageGET {
    HttpServletResponse res;

    public LanguageGET(HttpServletResponse res) {
        this.res = res;
    }

    public void Run() throws IOException, SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            res.getWriter().write("SQL Connection Error");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        String query = "SELECT * FROM enmo_database.languages ORDER BY language_id";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet result = preparedStatement.executeQuery();

        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();

        while (result.next()) {
            LanguageModel languageModel = new LanguageModel(result);
            JsonObject jsonObject = gson.toJsonTree(languageModel).getAsJsonObject();
            jsonArray.add(jsonObject);
        }

        res.getWriter().write(jsonArray.toString());
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
