package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Pro_CR;
import org.ucsc.enmoskill.model.ProposalModel;
import org.ucsc.enmoskill.model.ReviewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewService {

    public boolean isReviewInsertionSuccessful(ReviewModel reviewModel){

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO review (package_id,stars,description,order_id,client_id) VALUES (?,?,?,?,?)";
            assert con != null;
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, reviewModel.getPackage_id());
            preparedStatement.setInt(2, reviewModel.getStars());
            preparedStatement.setString(3, reviewModel.getDescription());
            preparedStatement.setInt(4, reviewModel.getOrder_id());
            preparedStatement.setInt(5, reviewModel.getClient_id());
            preparedStatement.executeUpdate(); // Execute the INSERT operation

            return true;
        }catch (SQLException e) {
            // Handle any exceptions that might occur during the insertion
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
