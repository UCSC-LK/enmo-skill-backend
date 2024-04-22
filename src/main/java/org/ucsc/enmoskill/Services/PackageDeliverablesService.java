package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.DeliverablesModel;
import org.ucsc.enmoskill.model.PackageDeliverables;
import org.ucsc.enmoskill.model.PackagePricing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PackageDeliverablesService {

    String query;

    public int insertPackageDeliverables(DeliverablesModel deliverablesModel){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        // 14
        try{
            con = DatabaseConnection.initializeDatabase();

            query = "INSERT INTO price_package_deliverables"+
                    "(price_package_id, category, del_1, del_2, del_3, del_4, del_5) "+
                    "VALUES(?,?,?,?,?,?,?)";
            preparedStatement = con.prepareStatement(query);
            ;
            preparedStatement.setInt(1, deliverablesModel.getPricePackageId());
            preparedStatement.setInt(2, deliverablesModel.getCategoryId());
            preparedStatement.setInt(3, deliverablesModel.getDel_1());
            preparedStatement.setInt(4, deliverablesModel.getDel_2());
            preparedStatement.setInt(5, deliverablesModel.getDel_3());
            preparedStatement.setInt(6, deliverablesModel.getDel_4());
            preparedStatement.setInt(7, deliverablesModel.getDel_5());

            result = preparedStatement.executeUpdate();

            return result;



        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions during closing connections if needed
            }
        }
    }

    public DeliverablesModel getPackageDeliverables(int pricePackageId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            query = "SELECT * FROM price_package_deliverables WHERE price_package_id=?;";
//            query = "SELECT * FROM package_deliverables WHERE price_package_id=?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,pricePackageId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet != null){
//                PackageDeliverables newDeliverables = new PackageDeliverables();
                DeliverablesModel newDeliverables = new DeliverablesModel();

                while (resultSet.next()) {

                    newDeliverables.setDeliverablesId(resultSet.getInt("deliverables_id"));
                    newDeliverables.setPricePackageId(resultSet.getInt("price_package_id"));
                    newDeliverables.setCategoryId(resultSet.getInt("category_id"));
                    newDeliverables.setDel_1(resultSet.getInt("del_1"));
                    newDeliverables.setDel_2(resultSet.getInt("del_2"));
                    newDeliverables.setDel_3(resultSet.getInt("del_3"));
                    newDeliverables.setDel_4(resultSet.getInt("del_4"));
                    newDeliverables.setDel_5(resultSet.getInt("del_5"));

                }
                return newDeliverables;
            } else
                return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions during closing connections if needed
            }
        }
    }

    public int updatePackageDeliverables(DeliverablesModel deliverables){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "UPDATE price_package_deliverables SET "+
                    "price_package_id = ?," +
                    "category = ?, "+
                    "del_1 = ?, "+
                    "del_2 = ?, "+
                    "del_3 = ?, "+
                    "del_4 = ?, "+
                    "del_5 = ? "+
                    "WHERE deliverables_id = ?;";

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,deliverables.getPricePackageId());
            preparedStatement.setInt(2,deliverables.getCategoryId());
            preparedStatement.setInt(3,deliverables.getDel_1());
            preparedStatement.setInt(4,deliverables.getDel_2());
            preparedStatement.setInt(5,deliverables.getDel_3());
            preparedStatement.setInt(6,deliverables.getDel_4());
            preparedStatement.setInt(7,deliverables.getDel_5());
            preparedStatement.setInt(8,deliverables.getDeliverablesId());

            result = preparedStatement.executeUpdate();

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions during closing connections if needed
            }
        }
    }
}
