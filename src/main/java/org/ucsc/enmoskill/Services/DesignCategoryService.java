package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.DesignCategoryModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DesignCategoryService {

    String query;

    public DesignCategoryModel getCategory(int categoryId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            query = "SELECT * FROM design_categories WHERE category_id = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, categoryId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet != null){
                DesignCategoryModel designCategoryModel = new DesignCategoryModel();

                while (resultSet.next()){
                    designCategoryModel.setCategoryId(resultSet.getInt("category_id"));
                    designCategoryModel.setCategory(resultSet.getString("category"));
                    designCategoryModel.setDel_1(resultSet.getString("del_1"));
                    designCategoryModel.setDel_2(resultSet.getString("del_2"));
                    designCategoryModel.setDel_3(resultSet.getString("del_3"));
                    designCategoryModel.setDel_4(resultSet.getString("del_4"));
                    designCategoryModel.setDel_5(resultSet.getString("del_5"));

                }

                return designCategoryModel;
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
