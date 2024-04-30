package org.ucsc.enmoskill.model;

import org.ucsc.enmoskill.utils.TokenService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupportAgentModel {
    private int userId;
    private String userName;
    TokenService.TokenInfo tokenInfo;

    public SupportAgentModel(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public SupportAgentModel(TokenService.TokenInfo tokenInfo) {
        this.userId = userId;
        this.userName = userName;
    }

    public SupportAgentModel(ResultSet result) throws SQLException {
        this.userId = result.getInt("userid");
        this.userName = result.getString("username");
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
