package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.User;
import org.ucsc.enmoskill.model.UserFullModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSer {

    public  int isInsertionSuccessful(User user) {
        // You can implement logic here to check if the insertion was successful
        // For example, you can return true if no exceptions were thrown during insertion
        // and false if an exception occurred.
        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO users (email,username, password  ) VALUES (?, ?, ?)";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            int Rowaffected = preparedStatement.executeUpdate(); // Execute the INSERT operation



            // If the execution reaches this point, the insertion was successful
            return 1;
        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                return 2;
            } else {
                e.printStackTrace();
                return 0; // Other SQL error
            }
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



    public List<UserFullModel> getAllDesigners(){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT" +
                    "    u.userID," +
                    "    d.display_name," +
                    "    u.email," +
                    "    u.contact_no," +
                    "    u.password," +
                    "    u.url," +
                    "    d.description," +
                    "    d.NIC," +
                    "    d.joinedDate," +
                    "    d.fname," +
                    "    d.lname, "+
                    "    u.status,"+
                    "    m.userlevelID "+
                    "FROM" +
                    "    users u " +
                    "LEFT JOIN" +
                    "    designer d ON u.userID = d.userid " +
                    "LEFT JOIN" +
                    "    user_level_mapping m ON d.userId = m.userID " +
                    "GROUP BY u.userID;";
            preparedStatement = con.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

            List<UserFullModel> userList = new ArrayList<>();

            while (resultSet.next()){
                UserFullModel userFull = new UserFullModel();
                User user = new User();

                user.setId(resultSet.getInt("userID"));
                user.setContact_no(resultSet.getString("contact_no"));
                user.setEmail(resultSet.getString("email"));
                user.setDescription(resultSet.getString("description"));
                user.setNIC(resultSet.getString("NIC"));
                user.setUsername(resultSet.getString("display_name"));
                user.setUrl(resultSet.getString("url"));
                user.setUser_role(String.valueOf(resultSet.getInt("userlevelID")));
                user.setPassword(resultSet.getString("password"));

                userFull.setUser(user);
                userFull.setFname(resultSet.getString("fname"));
                userFull.setLname(resultSet.getString("lname"));
                userFull.setStatus(resultSet.getInt("status"));
                userFull.setJoinedDate(resultSet.getString("joinedDate"));

                userList.add(userFull);
            }

            return userList;

        } catch (SQLException e) {
            System.out.println("SQL Error executing query: " + e.getMessage());
            throw new RuntimeException("Failed to fetch user data", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public List<UserFullModel> getAllClients(){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT" +
                    "    u.userID," +
                    "    u.username," +
                    "    u.email," +
                    "    u.contact_no," +
                    "    u.password," +
                    "    u.url," +
                    "    c.description," +
                    "    c.NIC," +
                    "    c.joinedDate," +
                    "    c.country," +
                    "    u.name, "+
                    "    u.status,"+
                    "    m.userlevelID "+
                    "FROM" +
                    "    users u " +
                    "LEFT JOIN" +
                    "    client c ON u.userID = c.userid " +
                    "LEFT JOIN" +
                    "    user_level_mapping m ON c.userid = m.userID " +
                    "GROUP BY u.userID;";
            preparedStatement = con.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

            List<UserFullModel> userList = new ArrayList<>();

            while (resultSet.next()){
                UserFullModel userFull = new UserFullModel();
                User user = new User();

                user.setId(resultSet.getInt("userID"));
                user.setContact_no(resultSet.getString("contact_no"));
                user.setEmail(resultSet.getString("email"));
                user.setDescription(resultSet.getString("description"));
                user.setNIC(resultSet.getString("NIC"));
                user.setUsername(resultSet.getString("username"));
                user.setName(resultSet.getString("name"));
                user.setUrl(resultSet.getString("url"));
                user.setCountry(resultSet.getString("country"));
                user.setUser_role(String.valueOf(resultSet.getInt("userlevelID")));
                user.setPassword(resultSet.getString("password"));
                user.setUsername(resultSet.getString("username"));


                userFull.setUser(user);
                userFull.setStatus(resultSet.getInt("status"));
                userFull.setJoinedDate(resultSet.getString("joinedDate"));

                userList.add(userFull);
            }

            return userList;

        } catch (SQLException e) {
            System.out.println("SQL Error executing query: " + e.getMessage());
            throw new RuntimeException("Failed to fetch dashboard data", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public int countUserRecords(){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT COUNT(userID) FROM users;";
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            // Move the cursor to the first row
            resultSet.next();

            // Retrieve the count value from the ResultSet
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<UserFullModel> filterUsers(List<UserFullModel> list, int role, int status){

        List<UserFullModel> newList = new ArrayList<>();

        for (UserFullModel userModel : list) {
            User user = userModel.getUser();

            if (userModel.getStatus() == status && String.valueOf(role).equals(user.getUser_role())){
                newList.add(userModel);
            }
        }
        return newList;
    }

    public UserFullModel getAdesigner(int userId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT" +
                    "    u.userID," +
                    "    d.display_name," +
                    "    u.email," +
//                    "    u.name," +
                    "    u.contact_no," +
                    "    u.password," +
                    "    u.url," +
                    "    d.description," +
                    "    d.NIC," +
                    "    d.joinedDate," +
                    "    d.fname," +
                    "    d.lname, "+
                    "    u.status,"+
                    "    m.userlevelID "+
                    "FROM" +
                    "    users u " +
                    "LEFT JOIN" +
                    "    designer d ON u.userID = d.userid " +
                    "LEFT JOIN" +
                    "    user_level_mapping m ON d.userId = m.userID " +
                    "WHERE u.userID = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            resultSet  = preparedStatement.executeQuery();


            if (resultSet != null){

                UserFullModel userFull = new UserFullModel();
                User user = new User();

                while (resultSet.next()){


                    user.setId(resultSet.getInt("userID"));
                    user.setContact_no(resultSet.getString("contact_no"));
                    user.setEmail(resultSet.getString("email"));
                    user.setDescription(resultSet.getString("description"));
                    user.setNIC(resultSet.getString("NIC"));
                    user.setUsername(resultSet.getString("display_name"));
                    user.setUrl(resultSet.getString("url"));
                    user.setUser_role(String.valueOf(resultSet.getInt("userlevelID")));
                    user.setPassword(resultSet.getString("password"));
//                    user.setName(resultSet.getString("name"));
                    userFull.setUser(user);
                    userFull.setFname(resultSet.getString("fname"));
                    userFull.setLname(resultSet.getString("lname"));
                    userFull.setStatus(resultSet.getInt("status"));
                    userFull.setJoinedDate(resultSet.getString("joinedDate"));
                }
                return userFull;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public UserFullModel getAclient(int userId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT" +
                    "    u.userID," +
                    "    u.username," +
                    "    u.email," +
                    "    u.password,"+
                    "    u.contact_no," +
                    "    u.url," +
                    "    c.description," +
                    "    c.NIC," +
                    "    c.joinedDate," +
                    "    c.country," +
                    "    u.name, "+
                    "    u.status,"+
                    "    m.userlevelID "+
                    "FROM" +
                    "    users u " +
                    "LEFT JOIN" +
                    "    client c ON u.userID = c.userid " +
                    "LEFT JOIN" +
                    "    user_level_mapping m ON c.userid = m.userID " +
                    "WHERE u.userID = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            resultSet  = preparedStatement.executeQuery();


            if (resultSet != null){

                UserFullModel userFull = new UserFullModel();
                User user = new User();

                while (resultSet.next()){

                    user.setId(resultSet.getInt("userID"));
                    user.setContact_no(resultSet.getString("contact_no"));
                    user.setEmail(resultSet.getString("email"));
                    user.setDescription(resultSet.getString("description"));
                    user.setNIC(resultSet.getString("NIC"));
                    user.setUsername(resultSet.getString("username"));
                    user.setName(resultSet.getString("name"));
                    user.setUrl(resultSet.getString("url"));
                    user.setCountry(resultSet.getString("country"));
                    user.setUser_role(String.valueOf(resultSet.getInt("userlevelID")));
                    user.setPassword(resultSet.getString("password"));

                    userFull.setUser(user);
                    userFull.setStatus(resultSet.getInt("status"));
                    userFull.setJoinedDate(resultSet.getString("joinedDate"));
                }
                return userFull;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int makeCSA(int userId){
        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "UPDATE user_level_mapping SET userlevelID = 4 WHERE userID = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
