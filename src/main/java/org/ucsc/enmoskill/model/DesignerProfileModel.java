package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DesignerProfileModel {
String userName,email,name,description;
List<String> languagesList,skillsList;

    public DesignerProfileModel(String userName, String email, String name, String description, List<String> languagesList, List<String> skillsList) {
        this.userName = userName;
        this.email = email;
        this.name = name;
        this.description = description;
        this.languagesList = languagesList;
        this.skillsList = skillsList;
    }

    public DesignerProfileModel(ResultSet resultSet) throws SQLException {
        userName = resultSet.getString("username");
        email = resultSet.getString("email");
        name = resultSet.getString("name");
        description = resultSet.getString("description");

        // Retrieve languages and skills as Lists
        languagesList = getListFromResultSet(resultSet, "languages");
        skillsList = getListFromResultSet(resultSet, "skills");
    }

    private static List<String> getListFromResultSet(ResultSet resultSet, String columnName) throws SQLException, SQLException {
        String concatenatedString = resultSet.getString(columnName);
        String[] items = concatenatedString.split(",");
        List<String> itemList = new ArrayList<>();
        for (String item : items) {
            itemList.add(item.trim());
        }
        return itemList;
    }
}
