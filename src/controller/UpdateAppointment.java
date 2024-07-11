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

/**Implements the Update Appointment controller.
 */
public class UpdateAppointment implements Initializable {

    Stage stage;
    Parent scene;

    ObservableList<Appointment> customerAppointments;

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
    private Label descLbl;

    @FXML
    private TextField descTxt;

    @FXML
    private Label endDateLbl;

    @FXML
    private DatePicker endDatePkr;

    @FXML
    private Label endTimeLbl;

    @FXML
    private TextField endTimeTxt;

    @FXML
    private Label locationLbl;

    @FXML
    private TextField locationTxt;

    @FXML
    private Button saveBtn;

    @FXML
    private Label startDateLbl;

    @FXML
    private DatePicker startDatePkr;

    @FXML
    private Label startTimeLbl;

    @FXML
    private TextField startTimeTxt;

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

    /**Returns the user to the main screen.
     * displays an alert that unsaved changes will be lost.
     */
    @FXML
    void cancelClk(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("Any unsaved data will be lost. Continue?");

        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                alert.close();
                try {
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
    }

    /**Saves Updated appointment information to the database.
     * Before saving checks database for overlapping appointments for the selected customer.
     * Checks database to ensure appointment is within business hours.
     * returns user to main screen after saving.
     */
    @FXML
    void saveClk(ActionEvent event) throws SQLException, IOException {
            LocalDate startDate = startDatePkr.getValue();
            LocalTime startTime = LocalTime.parse(startTimeTxt.getText());
            LocalDateTime start = LocalDateTime.of(startDate, startTime);

            LocalDate endDate = endDatePkr.getValue();
            LocalTime endTime = LocalTime.parse(endTimeTxt.getText());
            LocalDateTime end = LocalDateTime.of(endDate, endTime);

        if (businessHoursCheck(start, end) && customerOverlapCheck(start, end, custCmb.getSelectionModel().getSelectedItem().getCustomerId())) {
            int rowsAffected = AppointmentDaoImpl.updateAppointment(Integer.parseInt(apptTxt.getText()),
                    titleTxt.getText(),
                    descTxt.getText(),
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

    /**Initializes the Update Appointment screen.
     * Populates the necesarry combo boxes.
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

    /** pulls information from appointment selected on Main screen to populate fields in update appointment screen.
     */
    public void receiveAppt(Appointment appointment) throws Exception {
        apptTxt.setText(String.valueOf(appointment.getApptId()));
        custCmb.setValue(CustomerDaoImpl.customer(appointment.getCustId()));
        userCmb.setValue(UserDaoImpl.user(appointment.getUserId()));
        titleTxt.setText(appointment.getTitle());
        descTxt.setText(appointment.getDesc());
        locationTxt.setText(appointment.getLocation());
        contactCmb.setValue(ContactDaoImpl.contact(appointment.getContactId()));
        typeCmb.setValue(appointment.getType());
        startDatePkr.setValue(LocalDate.parse(String.valueOf(appointment.getStartDate())));
        startTimeTxt.setText(String.valueOf(appointment.getStartTime()));
        endDatePkr.setValue(LocalDate.parse(String.valueOf(appointment.getEndDate())));
        endTimeTxt.setText(String.valueOf(appointment.getEndTime()));
    }

}
