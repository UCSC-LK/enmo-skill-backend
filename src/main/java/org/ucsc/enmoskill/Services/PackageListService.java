package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Package;
import org.ucsc.enmoskill.model.PackageBlockModel;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PackageListService {


    public List<PackageBlockModel> getPackages(){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();
                query = "SELECT" +
                        "    p.package_id," +
                        "    p.title," +
                        "    p.description," +
                        "    p.category," +
                        "    p.cover_url," +
                        "    p.clicks," +
                        "    p.orders," +
                        "    p.cancellations," +
                        "    p.status," +
                        "    p.designer_userID," +
                        "    pp.price AS starterPrice," +
                        "    pp.delivery_duration," +
//                        "    AVG(r.stars) AS reviews," +
                        "    p.avg_ratings," +
                        "    pr.display_name AS designerUserName, " +
                        "    u.url AS DesignerImg,"+
                        "    GROUP_CONCAT(DISTINCT lm.language_id) AS languageIds "+
                        "FROM" +
                        "    package p " +
                        "JOIN" +
                        "    package_pricing pp ON p.package_id = pp.package_id " +
                        "JOIN" +
                        "    designer pr ON p.designer_userID = pr.userID " +
                        "JOIN"+
                        "   language_mapping lm ON p.designer_userID = lm.userID "+
                        "JOIN "+
                        "   users u ON p.designer_userID = u.userID "+
                        "WHERE" +
                        "   pp.type='bronze' AND p.status='active' " +
                        "GROUP BY" +
                        "   p.package_id, p.clicks " +
                        "ORDER BY" +
                        "   p.clicks DESC;";
                preparedStatement = con.prepareStatement(query);




            resultSet = preparedStatement.executeQuery();

            List<PackageBlockModel> packages = new ArrayList<>();

            while (resultSet.next()){
                PackageBlockModel block = new PackageBlockModel();
                block.setPackageId(resultSet.getInt("package_id"));
                block.setTitle(resultSet.getString("title"));
                block.setDescription(resultSet.getString("description"));
                block.setCategory(resultSet.getInt("category"));
                block.setCoverUrl(resultSet.getString("cover_url"));
                block.setClicks(resultSet.getInt("clicks"));
                block.setOrders(resultSet.getInt("orders"));
                block.setCancellations(resultSet.getString("cancellations"));
                block.setStatus(resultSet.getString("status"));
                block.setDesignerUserId(resultSet.getInt("designer_userID"));
                block.setStarterPrice(resultSet.getInt("starterPrice"));
                block.setDeliveryDuration(resultSet.getInt("delivery_duration"));
                block.setReviews(resultSet.getFloat("avg_ratings"));
                block.setDesignerUserName(resultSet.getString("designerUserName"));
                block.setDesignerProfileImg(resultSet.getString("DesignerImg"));

                // Fetching language IDs
                String languageIdsStr = resultSet.getString("languageIds");
                if (languageIdsStr != null) {
                    String[] languageIdsArray = languageIdsStr.split(","); // Splitting language IDs by comma
                    List<Integer> languageIdsList = new ArrayList<>();
                    for (String languageIdStr : languageIdsArray) {
                        languageIdsList.add(Integer.parseInt(languageIdStr.trim())); // Converting string IDs to integers
                    }
                    block.setLanguageId(languageIdsList); // Set the language IDs to the PackageBlockModel object
                }
                packages.add(block);
            }

            return packages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
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


    public int countPackages(){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            query = "SELECT COUNT(package_id) FROM package WHERE status='active';";
            preparedStatement = con.prepareStatement(query);
            result = preparedStatement.executeQuery();

            // Move the cursor to the first row
            result.next();

            // Retrieve the count value from the ResultSet
            return result.getInt(1);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the database connections in a finally block
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions during closing connections if needed
            }
        }
    }

    public List<PackageBlockModel> filterPackages(int category, int price, int delTime, int language, float reviews, List<PackageBlockModel> packageList) {
        return packageList.stream()
                .filter(filterByCategory(category))
                .filter(filterByPrice(price))
                .filter(filterByDeliveryTime(delTime))
                .filter(filterByReviews(reviews))
                .filter(filterByLanguage(language))
                .collect(Collectors.toList());
    }

    private Predicate<PackageBlockModel> filterByCategory(int category) {
        return aBlock -> category == 0 || aBlock.getCategory() == category;
    }

    private Predicate<PackageBlockModel> filterByPrice(int price) {
        return aBlock -> price == 0 || aBlock.getStarterPrice() <= price;
    }

    private Predicate<PackageBlockModel> filterByDeliveryTime(int delTime) {
        return aBlock -> delTime == 0 || aBlock.getDeliveryDuration() <= delTime;
    }

    private Predicate<PackageBlockModel> filterByLanguage(int language) {
        return aBlock -> language == 0 || aBlock.getLanguageId().contains(language);
    }

    private Predicate<PackageBlockModel> filterByReviews(float reviews) {
        return aBlock -> reviews == 0 || aBlock.getReviews() >= reviews;
    }

}
