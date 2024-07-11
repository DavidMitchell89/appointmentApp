package controller;

import DAO.AppointmentDaoImpl;
import DAO.CustomerDaoImpl;
import DAO.JDBC;
import Utilities.CustomerInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;

import static java.time.Month.*;

/**Implements the mainscreen for the application.*/
public class MainScreen implements Initializable {

    Stage stage;
    Parent scene;

    static ObservableList<Appointment> allAppointments;

    static ObservableList<Appointment> currentWeekAppointments;

    static ObservableList<Appointment> currentMonthAppointments;

    static ObservableList<Customer> customers;

    static ObservableList<Month> months = FXCollections.observableArrayList(JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER);

    static ObservableList<String> types = AppointmentDaoImpl.types;

    static ObservableList<Appointment> customerAppointments;

    /**Parent tabs for all tables.
     * */
    @FXML
    private Tab customersTab;

    @FXML
    private Tab apptsTab;

    /**Customer FXML elements. */

        @FXML
        private TableColumn<?, ?> custAddCol;

        @FXML
        private TableColumn<?, ?> custCountryCol;

        @FXML
        private TableColumn<?, ?> custIdCol;

        @FXML
        private TableColumn<?, ?> custNameCol;

        @FXML
        private TableColumn<?, ?> custPhoneCol;

        @FXML
        private TableColumn<?, ?> custPostCol;

        @FXML
        private TableColumn<?, ?> custStCol;

        @FXML
        private TableView<Customer> customerTbl;

    /**Customer Tab Buttons*/

        @FXML
        private Button custDltBtn;

        @FXML
        private Button custExitBtn;

        @FXML
        private Button newCustBtn;

        @FXML
        private Button viewApptBtn;

        @FXML
        private Button updateCustBtn;

        /**Allows updating of customers selected from displayed table.
         * pulls information from customer table and passes it to the update customer screen
         * if no selection is made an alert is shown asking to make a selection.*/
        @FXML
        void updateCustClk(ActionEvent event) throws Exception {
            if (customerTbl.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Please select a Customer to edit.");

                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        alert.close();
                    }
                }));
            }

            else {

                FXMLLoader custLoader = new FXMLLoader();
                custLoader.setLocation(getClass().getResource("/view/UpdateCustomer.fxml"));
                custLoader.load();

                UpdateCustomer custUpdtController = custLoader.getController();
                custUpdtController.receiveCustomer(customerTbl.getSelectionModel().getSelectedItem());

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = custLoader.getRoot();
                stage.setTitle("Update Customer");
                stage.setScene(new Scene(scene));
            }
        }

        /**Allows deleting of customer records.
         * displays an alert asking for confirmation before proceeding with deletion.
         * before deleting customer, method searches for any appointments for that customer to be deleted before the customer can be deleted.
         * after deleting both the relevant appointments and the customer a custom alert is shown.*/
        @FXML
        void custDltClk(ActionEvent event) throws SQLException {
            if (customerTbl.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Please make a selection to be deleted.");
                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        alert.close();
                    }
                }));
            } else if (!customerTbl.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText("");
                alert.setContentText("Are you sure you want to delete this customer. all associated appointments will also be deleted?");
                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        int customerId = customerTbl.getSelectionModel().getSelectedItem().getCustomerId();
                        int rowsAffected;
                        try {
                            rowsAffected = AppointmentDaoImpl.deleteAppointmentByCustomerId(customerId);
                            rowsAffected = CustomerDaoImpl.deleteCustomer(customerId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        alert.close();
                        if (rowsAffected > 0) {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Delete Complete");
                            alert.setContentText("Customer Successfully deleted.");
                            alert.showAndWait().ifPresent((response1 -> {
                                if (response1 == ButtonType.OK) {
                                    alert.close();
                                }
                            }));
                        }
                        try {
                            customers = CustomerDaoImpl.getAllCustomers();
                            customerTbl.setItems(customers);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
            }
        }

        /**Redirects to the new customer screen for creation of a new customer.
         * */
        @FXML
        void newCustClk(ActionEvent event) throws IOException {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/NewCustomer.fxml"));
            stage.setTitle("Create Customer");
            stage.setScene(new Scene(scene));
        }



    /**All appointment FXML elements. */

        @FXML
        private Tab allApptTab;

        @FXML
        private TableColumn<?, ?> allApptContactCol;

        @FXML
        private TableColumn<?, ?> allApptCustCol;

        @FXML
        private TableColumn<?, ?> allApptDescCol;

        @FXML
        private TableColumn<?, ?> allApptEndDateCol;

        @FXML
        private TableColumn<?, ?> allApptEndTimeCol;

        @FXML
        private TableColumn<?, ?> allApptIdCol;

        @FXML
        private TableColumn<?, ?> allApptLocCol;

        @FXML
        private TableColumn<Appointment, LocalDateTime> allApptStartDateCol;

        @FXML
        private TableColumn<?, ?> allApptStartTimeCol;

        @FXML
        private TableView<Appointment> allApptTbl;

        @FXML
        private TableColumn<?, ?> allApptTitleCol;

        @FXML
        private TableColumn<?, ?> allApptTypeCol;

        @FXML
        private TableColumn<?, ?> allApptUserCol;

        @FXML
        private Button apptCountGenBut;

        @FXML
        private Label apptCountLbl;



    /**Current month appointment FXML elements. */

        @FXML
        private Tab currentMonthApptTab;

        @FXML
        private TableView<Appointment> currentMonthApptTbl;

        @FXML
        private Tab currentWeekApptTab;

        @FXML
        private TableView<Appointment> currentWeekApptTbl;

        @FXML
        private TableColumn<?, ?> monthApptContactCol;

        @FXML
        private TableColumn<?, ?> monthApptCustCol;

        @FXML
        private TableColumn<?, ?> monthApptDescCol;

        @FXML
        private TableColumn<?, ?> monthApptEndCol;

        @FXML
        private TableColumn<?, ?> monthApptEndTimeCol;

        @FXML
        private TableColumn<?, ?> monthApptIdCol;

        @FXML
        private TableColumn<?, ?> monthApptLocCol;

        @FXML
        private TableColumn<?, ?> monthApptStartCol;

        @FXML
        private TableColumn<?, ?> monthApptStartTimeCol;

        @FXML
        private TableColumn<?, ?> monthApptTitleCol;

        @FXML
        private TableColumn<?, ?> monthApptTypeCol;
        @FXML
        private TableColumn<?, ?> monthApptUserCol;


        /**Current week appointment FXML elements. */
        @FXML
        private TableColumn<?, ?> WeekApptContactCol;

        @FXML
        private TableColumn<?, ?> WeekApptCustCol;

        @FXML
        private TableColumn<?, ?> WeekApptDescCol;

        @FXML
        private TableColumn<?, ?> WeekApptEndCol;

        @FXML
        private TableColumn<?, ?> weekApptEndTimeCol;

        @FXML
        private TableColumn<?, ?> weekApptStartTimeCol;

        @FXML
        private TableColumn<?, ?> WeekApptIdCol;

        @FXML
        private TableColumn<?, ?> WeekApptLocCol;

        @FXML
        private TableColumn<?, ?> WeekApptStartCol;

        @FXML
        private TableColumn<?, ?> WeekApptTitleCol;

        @FXML
        private TableColumn<?, ?> WeekApptTypeCol;

        @FXML
        private TableColumn<?, ?> WeekApptUserCol;


        /**Customer Appointment elements*/

        @FXML
        private TableView<Appointment> custApptTbl;

        @FXML
        private TableColumn<?, ?> custApptContCol;

    @FXML
    private TableColumn<?, ?> custApptCustCol;

    @FXML
    private TableColumn<?, ?> custApptDescCol;

    @FXML
    private TableColumn<?, ?> custApptEndCol;

    @FXML
    private TableColumn<?, ?> custApptEndTimeCol;

    @FXML
    private TableColumn<?, ?> custApptStartTimeCol;

    @FXML
    private TableColumn<?, ?> custApptIdCol;

    @FXML
    private TableColumn<?, ?> custApptLocCol;

    @FXML
    private TableColumn<?, ?> custApptStartCol;

    @FXML
    private TableColumn<?, ?> custApptTitleCol;

    @FXML
    private TableColumn<?, ?> custApptTypeCol;

    @FXML
    private TableColumn<?, ?> custApptUserCol;


        /**Appointment Function elements*/

        @FXML
        private ComboBox<Customer> custCmb;

        @FXML
        private Button contactApptGenBtn;

        @FXML
        private ComboBox<Contact> contactCmb;

        @FXML
        private ComboBox<Month> monthCmb;

        @FXML
        private ComboBox<String> typeCmb;

        @FXML
        private Button newAppt;

        @FXML
        private Button updateAppt;


        /**redirects to the new appointment screen to allow creating new appointments.
         * */
        @FXML
        void newApptClk(ActionEvent event) throws IOException {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/NewAppointment.fxml"));
            stage.setTitle("New Appointment");
            stage.setScene(new Scene(scene));
        }

        /**Allows for updating appointments.
         * Pulls information from one of the appointment tables passing the information to the update appointment screen.
         * if no selection is made an alert is shown asking for a selection.*/
        @FXML
        void updateApptClk(ActionEvent event) throws Exception {
           if (allApptTbl.getSelectionModel().isEmpty() &&
                    currentMonthApptTbl.getSelectionModel().isEmpty() &&
                    currentWeekApptTbl.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Please select an appointment to edit.");

                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        alert.close();
                    }
                }));
            }

            if (!allApptTbl.getSelectionModel().isEmpty()){

                 FXMLLoader apptLoader = new FXMLLoader();
                 apptLoader.setLocation(getClass().getResource("/view/UpdateAppointment.fxml"));
                 apptLoader.load();

                 UpdateAppointment appUpdtController = apptLoader.getController();
                 appUpdtController.receiveAppt(allApptTbl.getSelectionModel().getSelectedItem());

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = apptLoader.getRoot();
                stage.setTitle("Update Appointment");
                stage.setScene(new Scene(scene));
            }

            if (!currentMonthApptTbl.getSelectionModel().isEmpty()){

                FXMLLoader apptLoader = new FXMLLoader();
                apptLoader.setLocation(getClass().getResource("/view/UpdateAppointment.fxml"));
                apptLoader.load();

                UpdateAppointment appUpdtController = apptLoader.getController();
                appUpdtController.receiveAppt(currentMonthApptTbl.getSelectionModel().getSelectedItem());

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = apptLoader.getRoot();
                stage.setTitle("Update Appointment");
                stage.setScene(new Scene(scene));
            }

            if (!currentWeekApptTbl.getSelectionModel().isEmpty()){

                FXMLLoader apptLoader = new FXMLLoader();
                apptLoader.setLocation(getClass().getResource("/view/UpdateAppointment.fxml"));
                apptLoader.load();

                UpdateAppointment appUpdtController = apptLoader.getController();
                appUpdtController.receiveAppt(currentWeekApptTbl.getSelectionModel().getSelectedItem());

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = apptLoader.getRoot();
                stage.setTitle("Update Appointment");
                stage.setScene(new Scene(scene));
            }

        }

        @FXML
        private Button apptDltBtn;

        /**allows for the deletion of an appointment.
         * displays an alert if no selectin is made.
         * after asking for confirmation the appointment is deleted from both the application and the database.*/
        @FXML
        void apptDltClk(ActionEvent event) throws IOException, SQLException {
            if (allApptTbl.getSelectionModel().isEmpty() && currentWeekApptTbl.getSelectionModel().isEmpty() && currentMonthApptTbl.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Please make a selection to be deleted.");
                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        alert.close();
                    }
                }));
            }
            else if (!allApptTbl.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText("");
                alert.setContentText("Are you sure you want to delete this appointment.");
                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        int rowsAffected = 0;
                            try {
                                rowsAffected = AppointmentDaoImpl.deleteAppointment(allApptTbl.getSelectionModel().getSelectedItem().getApptId());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        alert.close();
                        if (rowsAffected > 0) {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Delete Complete");
                            alert.setContentText("Appointment Successfully deleted.");
                            alert.showAndWait().ifPresent((response1 -> {
                                if (response1 == ButtonType.OK) {
                                    alert.close();
                                }
                            }));
                        }
                        try {
                            allAppointments=AppointmentDaoImpl.getAllAppointments();
                            allApptTbl.setItems(allAppointments);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
            }
            else if (!currentMonthApptTbl.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText("");
                alert.setContentText("Are you sure you want to delete this appointment.");
                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        int rowsAffected = 0;
                        try {
                            rowsAffected = AppointmentDaoImpl.deleteAppointment(currentMonthApptTbl.getSelectionModel().getSelectedItem().getApptId());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        alert.close();
                        if (rowsAffected > 0) {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Delete Complete");
                            alert.setContentText("Appointment Successfully deleted.");
                            alert.showAndWait().ifPresent((response1 -> {
                                if (response1 == ButtonType.OK) {
                                    alert.close();
                                }
                            }));
                        }
                        try {
                            currentMonthAppointments = AppointmentDaoImpl.getCurrentMonthAppointments();
                            currentMonthApptTbl.setItems(currentMonthAppointments);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
            }
            else if (!currentWeekApptTbl.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText("");
                alert.setContentText("Are you sure you want to delete this appointment.");
                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        int rowsAffected = 0;
                        try {
                            rowsAffected = AppointmentDaoImpl.deleteAppointment(currentWeekApptTbl.getSelectionModel().getSelectedItem().getApptId());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        alert.close();
                        if (rowsAffected > 0) {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Delete Complete");
                            alert.setContentText("Appointment Successfully deleted.");
                            alert.showAndWait().ifPresent((response1 -> {
                                if (response1 == ButtonType.OK) {
                                    alert.close();
                                }
                            }));
                        }
                        try {
                            currentWeekAppointments = AppointmentDaoImpl.getCurrentWeekAppointments();
                            currentWeekApptTbl.setItems(currentWeekAppointments);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
            }
        }


        /**generates a total count of appointments by month and date selected in the relevant combo baxes.
         * takes the selections from both the month combobox and the appointment type combo box.
         * @return returns a count of appointments meeting the criteria.*/
        @FXML
        void apptCountGenClk(ActionEvent event) throws SQLException {
            int month = monthCmb.getSelectionModel().getSelectedItem().getValue();
            int monthEnd = Month.of(month).length(LocalDate.now().isLeapYear());
            LocalDate ldMonthStart = LocalDate.of(LocalDate.now().getYear(),
                    month,
                    1);
            LocalDate ldMonthEnd = LocalDate.of(LocalDate.now().getYear(),
                    month,
                    monthEnd);
            String type = typeCmb.getSelectionModel().getSelectedItem();
            int apptCount = AppointmentDaoImpl.appointmentCount(ldMonthStart, ldMonthEnd, type);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Count");
                alert.setHeaderText("");
                alert.setContentText("there are " + apptCount + " " + type + " appointments in " + monthCmb.getSelectionModel().getSelectedItem() + ".");
                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        alert.close();
                    }
                }));

            }



        @FXML
        void apptMonthSelectCmb(ActionEvent event) {

        }

        @FXML
        void apptTypeSelectCmb(ActionEvent event) {

        }

        /**Switches screen to contact appointment screen. */
        @FXML
        void contactApptGenClk(ActionEvent event) throws SQLException{
            try {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/ContactAppointments.fxml"));
                stage.setScene(new Scene(scene));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @FXML
        void contactSelectCmb(ActionEvent event) {

        }

        /**disconnects from the database and closes the application fully*/
        @FXML
        void onExitClk(ActionEvent event) {
            JDBC.closeConnection();
            Platform.exit();
        }
    /**returns a count of appointments for a given customer.
     * Lambda expression used to implement the count of customer appointments.
     * */
    @FXML
    void custSel(ActionEvent event) throws SQLException {
            CustomerInterface customerCount = customer -> {
                AppointmentDaoImpl.customerAppointments(custCmb.getSelectionModel().getSelectedItem().getCustomerId());
                return customer;
            };

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("no appointments");
            alert.setHeaderText("");
            alert.setContentText("there are " + customerCount.customerCount(custCmb.getSelectionModel().getSelectedItem().getCustomerId()) + " appointments for the selected customer.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            }));
        }


        /**Initializes and sets information for all the combo boxes and tables present on the main screen.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) throws RuntimeException {

        /**Initializes Customers Table view.
         * */
        try {
            customers = CustomerDaoImpl.getAllCustomers();

            customerTbl.setItems(customers);
            custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            custAddCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            custPostCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            custStCol.setCellValueFactory(new PropertyValueFactory<>("division"));
            custCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));

            custCmb.setItems(customers);
            monthCmb.setItems(months);
            typeCmb.setItems(types);


        /**initializes Table view for all appointments.
         * */
            allAppointments = AppointmentDaoImpl.getAllAppointments();

            allApptTbl.setItems(allAppointments);
            allApptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            allApptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            allApptDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
            allApptLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            allApptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
            allApptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            allApptStartDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            allApptStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            allApptEndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            allApptEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            allApptCustCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
            allApptUserCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

            currentWeekAppointments = AppointmentDaoImpl.getCurrentWeekAppointments();

            currentWeekApptTbl.setItems(currentWeekAppointments);
            WeekApptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            WeekApptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            WeekApptDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
            WeekApptLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            WeekApptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
            WeekApptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            WeekApptStartCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            weekApptStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            WeekApptEndCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            weekApptEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            WeekApptCustCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
            WeekApptUserCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

            currentMonthAppointments = AppointmentDaoImpl.getCurrentMonthAppointments();

            currentMonthApptTbl.setItems(currentMonthAppointments);
            monthApptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            monthApptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            monthApptDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
            monthApptLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            monthApptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
            monthApptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            monthApptStartCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            monthApptStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            monthApptEndCol.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
            monthApptEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            monthApptCustCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
            monthApptUserCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);


        }
   }
}
