package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupportAgentModel;
import org.ucsc.enmoskill.model.SupprtModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupportAgentGET {
    private TokenService.TokenInfo tokenInfo;

    public SupportAgentGET(TokenService.TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

        public ResponsModel Run() throws IOException, SQLException {
            Connection connection = DatabaseConnection.initializeDatabase();
            if(connection==null){
//                resp.getWriter().write("SQL Connection Error");
//                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return new ResponsModel("SQL Connection Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            }else {

                JsonArray jsonArray = new JsonArray();
                Gson gson = new Gson();


                if (tokenInfo.isAgent() || tokenInfo.isAdmin()){

                    String query = "SELECT c.*,u.username FROM enmo_database.contact_support_agent c JOIN users u ON c.userid = u.userID ORDER BY username";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    while (result.next()){
                        SupportAgentModel supportAgentModel = new SupportAgentModel(result);
                        JsonObject jsonObject = gson.toJsonTree(supportAgentModel).getAsJsonObject();
                        jsonArray.add(jsonObject);
                    }

                }
                return new ResponsModel(jsonArray.toString(),HttpServletResponse.SC_OK);
            }

        }


}
