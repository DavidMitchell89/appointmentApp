package controller;

import DAO.AppointmentDaoImpl;
import DAO.ContactDaoImpl;
import DAO.CustomerDaoImpl;
import DAO.UserDaoImpl;
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
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

/**implements the New Appointment controller class.
 */
public class NewAppointment implements Initializable {

    Stage stage;
    Parent scene;

    ObservableList <Appointment> customerAppointments;


    @FXML
    private Label DescriptionLbl;

    @FXML
    private TextField descriptionTxt;

    @FXML
    private Label apptLbl;

    @FXML
    private TextField apptTxt;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<Contact> contactCmb;

    @FXML
    private Label contactLbl;

    @FXML
    private ComboBox<Customer> custCmb;

    @FXML
    private Label customerLbl;

    @FXML
    private Label endLbl;

    @FXML
    private DatePicker endPkr;

    @FXML
    private TextField endTxt;

    @FXML
    private Label locationLbl;

    @FXML
    private TextField locationTxt;

    @FXML
    private Button saveBtn;

    @FXML
    private Label startLbl;

    @FXML
    private DatePicker startPkr;

    @FXML
    private TextField startTxt;

    @FXML
    private Label titleLbl;

    @FXML
    private TextField titleTxt;

    @FXML
    private ComboBox<String> typeCmb;

    @FXML
    private Label typeLbl;

    @FXML
    private ComboBox<User> userCmb;

    @FXML
    private Label userLbl;

    /**method to return to main screen.
     * throws up alert verifying that any input information will be lost.*/
    @FXML
    void cancelClk(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**Saves Appointment information to Database.
     * Ensures all fields are filled out.
     * checks appointment date and time against Business hours in Easter Standard Time. */
    @FXML
    void saveClk(ActionEvent event) throws IOException, SQLException {
        if (custCmb.getSelectionModel().isEmpty() ||
                userCmb.getSelectionModel().isEmpty() ||
                titleTxt.getText().isEmpty() ||
                descriptionTxt.getText().isEmpty() ||
                locationTxt.getText().isEmpty() ||
                contactCmb.getSelectionModel().isEmpty() ||
                typeCmb.getSelectionModel().isEmpty() ||
                startPkr.getTypeSelector().isEmpty() ||
                startTxt.getText().isEmpty() ||
                endPkr.getTypeSelector().isEmpty() ||
                endTxt.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("please enter valid data in all fields.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            }));
        } else {
            LocalDate startDate = startPkr.getValue();
            LocalTime startTime = LocalTime.parse(startTxt.getText());
            LocalDateTime start = LocalDateTime.of(startDate, startTime);

            LocalDate endDate = startPkr.getValue();
            LocalTime endTime = LocalTime.parse(endTxt.getText());
            LocalDateTime end = LocalDateTime.of(endDate, endTime);

            if (businessHoursCheck(start, end) && customerOverlapCheck(start, end, custCmb.getSelectionModel().getSelectedItem().getCustomerId())) {
                int rowsAffected = AppointmentDaoImpl.newAppointment(titleTxt.getText(),
                        descriptionTxt.getText(),
                        locationTxt.getText(),
                        typeCmb.getSelectionModel().getSelectedItem(),
                        Timestamp.valueOf(start),
                        Timestamp.valueOf(end),
                        custCmb.getSelectionModel().getSelectedItem().getCustomerId(),
                        userCmb.getSelectionModel().getSelectedItem().getUserId(),
                        contactCmb.getSelectionModel().getSelectedItem().getContactId());
                if (rowsAffected > 0) {
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                    stage.setScene(new Scene(scene));

                }
            }
        }
    }

    /**Method to ensure that there are no overlapping appointments for selected customer before saving appointment.
     * searches Database for any appointment times that overlap with input information.
     * displays an alert with appointment information if any are found.
     * @return returns true if no overlaps are found.*/
    private boolean customerOverlapCheck (LocalDateTime start, LocalDateTime end, int customerId) throws SQLException {
        Boolean noOverlap = null;
        customerAppointments = AppointmentDaoImpl.overlapAppointments(customerId, Timestamp.valueOf(start), Timestamp.valueOf(end));
        System.out.println(customerAppointments.size());
        if (!customerAppointments.isEmpty()) {
            noOverlap = false;
            for (Appointment appointment : customerAppointments) {
                if (customerAppointments.size() >= 1) {
                    noOverlap = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Overlapping appointments");
                    alert.setContentText(
                            "Overlapping appointments: \n"
                                    + "appointment ID: " + appointment.getApptId() + "\n"
                                    + "start: " + appointment.getStartTime() + "\n"
                                    + "end: " + appointment.getEndTime());
                    alert.showAndWait().ifPresent((response -> {
                        if (response == ButtonType.OK) {
                            alert.close();
                        }
                    }));
                }
            }
        }
        else {
            noOverlap = true;
        }

    return noOverlap;
    }

    /**Method to ensure appointments fall within business hours.
     * methods converts input times to Eastern standard time and compares against business hours of 8:00 to 10:00 seven days a week.
     * displays an alert if oppointment time is not appropriate
     * @return returns true if appointment is within business hours.
     * */
    private boolean businessHoursCheck (LocalDateTime start, LocalDateTime end) {
        Boolean hoursOk = null;
        ZoneId businessZoneId = ZoneId.of("America/New_York");
        LocalTime businessStartTime = LocalTime.parse("08:00");
        System.out.println(businessStartTime);
        LocalTime businessEndTime = LocalTime.parse("22:00");
        System.out.println(businessEndTime);

        ZonedDateTime apptStart = ZonedDateTime.of(start, ZoneId.systemDefault()).withZoneSameInstant(businessZoneId);
        LocalTime apptStartBsZone = apptStart.toLocalDateTime().toLocalTime();


        ZonedDateTime apptEnd = ZonedDateTime.of(end, ZoneId.systemDefault()).withZoneSameInstant(businessZoneId);
        LocalTime apptEndBsZone = apptEnd.toLocalDateTime().toLocalTime();
       if (apptStartBsZone.isAfter(businessStartTime) ||apptStartBsZone.equals(businessStartTime) && apptStartBsZone.isBefore(businessEndTime) && apptEndBsZone.isAfter(businessStartTime) && apptEndBsZone.isBefore(businessEndTime)) {
           hoursOk = true;
        } else {
           hoursOk = false;
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("");
           alert.setHeaderText("");
           alert.setContentText("Please ensure appointment is schedule during business hours. \n" +
                   "Business hours are Monday through Friday 8am to 10pm Eastern Standard Time.");
           alert.showAndWait().ifPresent((response -> {
               if (response == ButtonType.OK) {
                   alert.close();
               }
           }));
        }
        return hoursOk;
    }

    /**Initializes the New Appointments screen.
     * populates all necessary combo boxes.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            custCmb.setItems(CustomerDaoImpl.getAllCustomers());
            userCmb.setItems(UserDaoImpl.getAllUsers());
            contactCmb.setItems(ContactDaoImpl.getAllContacts());
            typeCmb.setItems(AppointmentDaoImpl.types);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

