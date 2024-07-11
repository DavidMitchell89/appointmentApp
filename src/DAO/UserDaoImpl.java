package DAO;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl {



    public static ObservableList<User> getAllUsers() throws SQLException {
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        String sqlStatement = "Select * from users";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        while (result.next()) {
            int userId = result.getInt("User_ID");
            String userName = result.getString("User_Name");
            String userPassword = result.getString("Password");
            User userResult = new User(userId, userName, userPassword);
            allUsers.add(userResult);
        }
        return allUsers;
    }

    public static User user (int id) throws SQLException {
        String sqlStatement = "Select * from users WHERE User_ID = " + id;
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        User user = null;
        while (result.next()) {
            int userId = result.getInt("User_ID");
            String userName = result.getString("User_Name");
            String userPassword = result.getString("Password");
            user = new User(userId, userName, userPassword);
        }
        return user;
    }

}
