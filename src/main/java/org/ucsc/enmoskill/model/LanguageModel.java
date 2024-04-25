package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LanguageModel {
    private String language;
    private int language_id;

    public LanguageModel(String language, int languageID) {
        this.language = language;
        this.language_id = languageID;
    }

    public LanguageModel(ResultSet result) throws SQLException {
        this.language = result.getString("language");
        this.language_id = result.getInt("language_id");
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getLanguageID() {
        return language_id;
    }

    public void setLanguageID(int languageID) {
        this.language_id = languageID;
    }
}
