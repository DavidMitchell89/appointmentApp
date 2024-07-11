/*pop-up when login for appointments coming up in next 15 minutes or no appointments
automatically changes language based on system settings, only french and english are necessary.
*/

package controller;

import DAO.AppointmentDaoImpl;
import DAO.JDBC;
import DAO.Query;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

/**Implements the login in screen and associated Methods.
 * */
public class LoginScreen implements Initializable {

    Stage stage;
    Parent scene;

    FileWriter loginActivity = new FileWriter("login_activity.txt", true);


    @FXML
    private Label titleLbl;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label idLbl;

    @FXML
    private TextField idTxt;

    @FXML
    private Label locationLbl;

    @FXML
    private Label localeLbl;

    @FXML
    private Button loginBtn;

    @FXML
    private Label passwordLbl;

    @FXML
    private TextField passwordTxt;

    public LoginScreen() throws IOException {
    }

    /**Initializes the login screen.
     * sets all text to associated language based on system locale.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLbl.setText(language("titleLabel"));
        locationLbl.setText(language("locationLabel"));
        localeLbl.setText(Locale.getDefault().toString());
        idLbl.setText(language("idLabel"));
        passwordLbl.setText(language("passwordLabel"));
        loginBtn.setText(language("loginBtn"));
        cancelBtn.setText(language("cancelBtn"));
    }

    /**Method to pull strings from language resource bundles.
     * @param key
     * @return string in system default language.
     * */
    private String language (String key) {
        ResourceBundle languageBundle = ResourceBundle.getBundle("main/ResourceBundle", Locale.getDefault());
        String translate = languageBundle.getString(key);
    return translate;
    }


    /**Checks username and password information entered against database.
     * if no information is input an alert is shown asking for inputs.
     * if username and password is input, it checks information against what is in the database.
     *       if information is incorrect a relevant alert is displayed and login fails.
     *       if information is correct the database is checked again for appointments within the next fifteen minutes local time.
     *            if there is no appointments found, an alert will be shown and screen is redirected to the main screen
     *            if there is appointments found, the appointments will be listed in an alert, and screen is redirected to the main screen.
 *            an activity log file is kept for all login attempts.
     *            all relevant text and alert ore displayed in system default language.
     *            @param event userName
     *            @param event password
     *            */
    public void loginClk(ActionEvent event) throws Exception {
              // test username password first JDBC.openConnection();
        JDBC.openConnection();
        if (idTxt.getText().isEmpty() || passwordTxt.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText(language("invalidPassword"));

            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            }));
            loginActivity(false);
            JDBC.closeConnection();
        }

        else {
            String userName = idTxt.getText();
            String userPassword = passwordTxt.getText();
            if (pullUserId(userName, userPassword) > 0) {
                LocalDateTime nowLdt = LocalDateTime.of(LocalDate.now(), LocalTime.now());
                Timestamp nowTs = Timestamp.valueOf(nowLdt);
                ObservableList<Appointment> upcomingAppointments= AppointmentDaoImpl.getUpcomingAppointments(nowTs);
                if (upcomingAppointments.size() < 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("");
                    alert.setContentText(language("noAppointments"));

                    alert.showAndWait().ifPresent((response -> {
                        if (response == ButtonType.OK) {
                            alert.close();
                        }
                        try {
                            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                            stage.setScene(new Scene(scene));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }));
                    loginActivity(true);
                }
                else {
                    for (Appointment appointment : upcomingAppointments) {
                            if (upcomingAppointments.size() >= 1) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("");
                                alert.setContentText(
                                        language("upcomingAppointments") + ": \n"
                                                +language("appointmentID")  + ": " + appointment.getApptId() + "\n"
                                                + language("startDate") + ": " + appointment.getStartDate() + "\n"
                                                + language("time") + ": " + appointment.getStartTime());
                                alert.showAndWait().ifPresent((response -> {
                                    if (response == ButtonType.OK) {
                                        alert.close();
                                    }
                                }));
                            }

                            else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("");
                                alert.setHeaderText("");
                                alert.setContentText(language("noAppointments"));

                                alert.showAndWait().ifPresent((response -> {
                                    if (response == ButtonType.OK) {
                                        alert.close();
                                    }
                                }));
                            }
                    }

                    try {
                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                        stage.setScene(new Scene(scene));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText(language("invalidPassword"));

                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        alert.close();
                    }
                }));
                loginActivity(false);
                JDBC.closeConnection();
            }
        }
    }

    public void cancelClk(ActionEvent actionEvent) {
        //put behind if statement to test if connection open JDBC.closeConnection();
        JDBC.closeConnection();
        Platform.exit();
    }


    /**Gets user ID from Database for password validation. uses both the username and password to pull user ID.
     * @param userPassword
     * @param  userName
     * @return returns true if username and password match an entry in the database
     * */
    private int pullUserId(String userName, String userPassword) throws SQLException {
        JDBC.openConnection();
        int userID = -1;
        String sqlStatement = "SELECT User_ID FROM users WHERE Password = '" + userPassword + "' AND User_Name = '" + userName + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result=Query.getResult();
        while (result.next()){
            userID = result.getInt("User_ID");
        }
        return userID;
    }

    /**Appends all login attempts to the file "login_activity.txt*/
    private void loginActivity(boolean loginAttempt) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        if(!loginAttempt) {
            loginActivity.write("Login attempt failed at: " + now.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.getDefault())) + "\n");
            loginActivity.close();
        }
        else if(loginAttempt == true) {
            loginActivity.write("Login attempt Successful at: " + now.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.getDefault())) + "\n");
            loginActivity.close();
        }
    }

}
