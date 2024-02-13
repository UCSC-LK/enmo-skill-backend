package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Package;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PackageListService {

    public static List<Package> getCustPriceDelLang(int category, int price, int delTimeCode, int language){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE pp.type = 'bronze'" +
                        "  AND pp.price <= ?" +
                        "  AND pp.delivery_duration <= ?"+
                        "  AND lm.language_id = ?;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, price);
                preparedStatement.setInt(2, delTimeCode);
                preparedStatement.setInt(3, language);


            } else {
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE p.category = ? " +
                        "  AND pp.type = 'bronze'" +
                        "  AND pp.price <= ?" +
                        "  AND pp.delivery_duration <= ?"+
                        "  AND lm.language_id = ?;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, price);
                preparedStatement.setInt(3, delTimeCode);
                preparedStatement.setInt(4, language);


            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getCustPriceLang(int category, int price, int language){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE pp.type = 'bronze'" +
                        "  AND pp.price <= ?" +
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, price);
                preparedStatement.setInt(2, language);


            } else {
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE p.category = ? " +
                        "  AND pp.type = 'bronze'" +
                        "  AND pp.price <= ?" +
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, price);
                preparedStatement.setInt(3, language);


            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getCustPriceDel(int category, int price, int delTimeCode){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT * FROM package WHERE package_id IN ("
                        +"SELECT package_id FROM package_pricing WHERE type='bronze' AND price<=? AND delivery_duration<=?) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, price);
                preparedStatement.setInt(2, delTimeCode);

            } else {
                query = "SELECT * FROM package WHERE package_id IN ("
                        +"SELECT package_id FROM package_pricing WHERE category=? AND type='bronze' AND price<=? AND delivery_duration<=?) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, price);
                preparedStatement.setInt(3, delTimeCode);

            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getPkgesHighDelLang(int category, int delTimeCode, int language){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE pp.type = 'bronze'" +
                        "  AND pp.price >= 5000" +
                        "  AND pp.delivery_duration <= ?"+
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, delTimeCode);
                preparedStatement.setInt(2, language);


            } else {
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE p.category = ? " +
                        "  AND pp.type = 'bronze'" +
                        "  AND pp.price >= 5000" +
                        "  AND pp.delivery_duration <= ?"+
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";


                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, delTimeCode);
                preparedStatement.setInt(3, language);


            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getPkgesHighLang(int category, int language){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE pp.type = 'bronze'" +
                        "  AND pp.price >= 5000" +
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, language);

            } else {
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE p.category = ? " +
                        "  AND pp.type = 'bronze'" +
                        "  AND pp.price >= 5000" +
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, language);


            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<Package> getPkgesHighDel(int category, int delTimeCode){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT * FROM package WHERE package_id IN ("
                        +"SELECT package_id FROM package_pricing WHERE type='bronze' AND price>=5000 AND delivery_duration<=?) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, delTimeCode);
            } else {
                query = "SELECT * FROM package WHERE package_id IN ("
                        +"SELECT package_id FROM package_pricing WHERE category=? AND type='bronze' AND price>=5000 AND delivery_duration<=?) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, delTimeCode);

            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getPkgesMidDelLang(int category, int delTimeCode, int language){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE pp.type = 'bronze'" +
                        "  AND pp.price BETWEEN 2000 AND 5000" +
                        "  AND pp.delivery_duration <= ?"+
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, delTimeCode);
                preparedStatement.setInt(2, language);


            } else {
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE p.category = ? " +
                        "  AND pp.type = 'bronze'" +
                        "  AND pp.price BETWEEN 2000 AND 5000" +
                        "  AND pp.delivery_duration <= ?"+
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, delTimeCode);
                preparedStatement.setInt(3, language);


            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getPkgesMidLang(int category, int language){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE pp.type = 'bronze'" +
                        "  AND pp.price BETWEEN 2000 AND 5000" +
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, language);

            } else {
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE p.category = ? " +
                        "  AND pp.type = 'bronze'" +
                        "  AND pp.price BETWEEN 2000 AND 5000" +
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, language);


            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getPkgesMidDel(int category, int delTimeCode){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT * FROM package WHERE package_id IN ("
                        +"SELECT package_id FROM package_pricing WHERE type='bronze' AND (price BETWEEN 2000 AND 5000) AND delivery_duration<=?) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, delTimeCode);
            } else {
                query = "SELECT * FROM package WHERE package_id IN ("
                        +"SELECT package_id FROM package_pricing WHERE category=? AND type='bronze' AND (price BETWEEN 2000 AND 5000) AND delivery_duration<=?) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, delTimeCode);

            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getPkgesLowDelLang(int category, int delTimeCode, int language){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE pp.type = 'bronze'" +
                        "  AND pp.price <= 2000" +
                        "  AND pp.delivery_duration <= ?"+
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, delTimeCode);
                preparedStatement.setInt(2, language);


            } else {
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE p.category = ? " +
                        "  AND pp.type = 'bronze'" +
                        "  AND pp.price <= 2000" +
                        "  AND pp.delivery_duration <= ?"+
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, delTimeCode);
                preparedStatement.setInt(3, language);


            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getPkgesLowLang(int category, int language){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE pp.type = 'bronze'" +
                        "  AND pp.price <= 2000" +
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, language);

            } else {
                query = "SELECT p.* " +
                        "FROM package p " +
                        "JOIN package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN language_mapping lm ON p.designer_userID = lm.userID " +
                        "WHERE p.category = ? " +
                        "  AND pp.type = 'bronze'" +
                        "  AND pp.price <= 2000" +
                        "  AND lm.language_id = ?"+
                        "  ORDER BY clicks DESC;";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, language);


            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getPkgesLowDel(int category, int delTimeCode){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT * FROM package WHERE package_id IN ("
                        +"SELECT package_id FROM package_pricing WHERE type='bronze' AND price<=2000 AND delivery_duration<=?) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, delTimeCode);
            } else {
                query = "SELECT * FROM package WHERE package_id IN ("
                        +"SELECT package_id FROM package_pricing WHERE category=? AND type='bronze' AND price<=2000 AND delivery_duration<=?) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, delTimeCode);

            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getPkgesByLang(int category, int language){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT * FROM package WHERE designer_userID IN ("
                +"SELECT userID FROM language_mapping WHERE language_id = ?)"+
                        "  ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, language);
            } else {
                query = "SELECT * FROM package WHERE category=? AND designer_userID IN ("
                        +"SELECT userID FROM language_mapping WHERE language_id = ?)"+
                        "  ORDER BY clicks DESC;";;
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, language);

            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getPkgesByDuration(int category, int delTimeCode){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try {
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT * FROM package WHERE package_id IN (SELECT package_id FROM package_pricing WHERE delivery_duration<=?) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, delTimeCode);
            } else {
                query = "SELECT * FROM package WHERE category=? AND package_id IN (SELECT package_id FROM package_pricing WHERE delivery_duration<=?) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, delTimeCode);
            }

            resultSet = preparedStatement.executeQuery();
            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getCustomPricePackages(int category, int price){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT * FROM package WHERE package_id IN (SELECT package_id FROM package_pricing WHERE (type = 'bronze' AND price <= ?)) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, price);
            } else {
                query = "SELECT * FROM package WHERE category=? AND package_id IN (SELECT package_id FROM package_pricing WHERE (type = 'bronze' AND price <= ?)) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
                preparedStatement.setInt(2, price);
            }



            resultSet = preparedStatement.executeQuery();

            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Package> getHighPackages(int category){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT * FROM package WHERE package_id IN (SELECT package_id FROM package_pricing WHERE (type = 'bronze' AND price >= 5000))";
                preparedStatement = con.prepareStatement(query);
            } else {
                query = "SELECT * FROM package WHERE category=? AND package_id IN (SELECT package_id FROM package_pricing WHERE (type = 'bronze' AND price >= 5000))";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
            }



            resultSet = preparedStatement.executeQuery();

            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getMidPackages(int category){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;



        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0) {
                query = "SELECT * FROM package WHERE package_id IN (SELECT package_id FROM package_pricing WHERE (type = 'bronze' AND price between 2000 AND 5000)) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
            } else {
                query = "SELECT * FROM package WHERE category=? AND package_id IN (SELECT package_id FROM package_pricing WHERE (type = 'bronze' AND price between 2000 AND 5000)) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
            }



            resultSet = preparedStatement.executeQuery();

            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);
            return packages;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Package> geLowPackages(int category){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT * FROM package WHERE package_id IN (SELECT package_id FROM package_pricing WHERE type='bronze' AND price<=2000) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);

            } else {
                query = "SELECT * FROM package WHERE category=? AND package_id IN (SELECT package_id FROM package_pricing WHERE type='platinum' AND price<=2000) ORDER BY clicks DESC;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1,category);

            }

            resultSet = preparedStatement.executeQuery();

            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getAllPackages(int category){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT package_id, title, description, category, cover_url, clicks, orders, cancellations, status, designer_userID, insertion_time FROM package WHERE status='active' ORDER BY clicks desc;";
                preparedStatement = con.prepareStatement(query);

            } else{
                query = "SELECT package_id, title, description, category, cover_url, clicks, orders, cancellations, status, designer_userID, insertion_time FROM package WHERE status='active' AND category=? ORDER BY clicks desc;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
            }


            resultSet = preparedStatement.executeQuery();

            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;

        } catch (Exception e) {
            // Handle exceptions here
            e.printStackTrace();
            // You might want to throw an exception or return null here, depending on your requirements.
            return null;
        } finally {
            // Close the database connections in a finally block
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions during closing connections if needed
            }
        }
    }

    public static List<Package> unwrap(ResultSet resultSet){
        List<Package> packages = new ArrayList<>();

        try {
            while (resultSet.next()){
                Package newPackage = new Package(resultSet.getInt("package_id"),resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("category"), resultSet.getString("cover_url"), resultSet.getInt("clicks"), resultSet.getInt("orders"), resultSet.getString("cancellations"), resultSet.getString("status"), resultSet.getInt("designer_userID"), resultSet.getTimestamp("insertion_time"));
                packages.add(newPackage);
            }
            return packages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
