package org.ucsc.enmoskill.utils;

public class AuthorizationResults {
    private String userID;
    private String jwtUserLevelID;

    public AuthorizationResults(String userID, String jwtUserLevelID) {
        this.userID = userID;
        this.jwtUserLevelID = jwtUserLevelID;
    }

    public String getUserID() {
        return userID;
    }

    public String getJwtUserLevelID() {
        return jwtUserLevelID;
    }
}
