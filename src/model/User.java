package model;

public class User {

    private int userId;

    private String userName;

    private String userPassword;

    /**
     * Defines the User class model.
     * */
    public User(int userId, String userName, String password){
        this.userId = userId;
        this.userName = userName;
        this.userPassword = password;
    }

    @Override
    public String toString() {
        return String.valueOf(userId);
    }

    /**Returns User ID.
     * @return userId */
    public int getUserId() {
        return userId;
    }

    /**Sets User ID.
     * @param userId */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**Returns User name.
     * @return userName */
    public String getUserName() {
        return userName;
    }

    /**Sets User name.
     * @param userName */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**Returns User password.
     * @return password */
    public String getUserPassword() {
        return userPassword;
    }

    /**Sets User password.
     * @param userPassword */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
