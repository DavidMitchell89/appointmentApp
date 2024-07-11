package controller;

import DAO.AppointmentDaoImpl;
import DAO.ContactDaoImpl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


/**Implements a class for controlling the contact appointment screen.
 * */
public class ContactAppointments implements Initializable {

    Stage stage;
    Parent scene;

        ObservableList<Appointment> allContactsAppointments;

        ObservableList<Appointment> contactAppointments;

        ObservableList<Contact> contacts;

        @FXML
        private Label ContactLbl;

        @FXML
        private TableColumn<?, ?> apptCustCol;

        @FXML
        private TableColumn<?, ?> apptDescCol;

        @FXML
        private TableColumn<?, ?> apptEndCol;

        @FXML
        private TableColumn<?, ?> apptEndTimeCol;

        @FXML
        private TableColumn<?, ?> apptIdCol;

        @FXML
        private TableColumn<?, ?> apptStartCol;

        @FXML
        private TableColumn<?, ?> apptStartTimeCol;

        @FXML
        private TableColumn<?, ?> apptTitleCol;

        @FXML
        private TableColumn<?, ?> apptTypeCol;

        @FXML
        private Button backBtn;


        /**On Click Returns user to main screen.
         * */
        @FXML
        void backClk (ActionEvent event) {
            try {
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @FXML
        private TableView<Appointment> contactAppointmentTable;

        @FXML
        private ComboBox<Contact> contactCmb;

        /**Populate table with a list of appointments for selected contact.
         */
        @FXML
        void contactSel(ActionEvent event) throws SQLException {
            contactAppointments = AppointmentDaoImpl.contactAppointments(contactCmb.getSelectionModel().getSelectedItem().getContactId());
            contactAppointmentTable.setItems(contactAppointments);
            apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            apptDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
            apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            apptStartCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            apptStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            apptEndCol.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
            apptEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            apptCustCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        }

        /**Initializes the Contact Appointment screen.
         * populatoes the combo box for selecting a contact.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) throws RuntimeException {
        try {
            contacts = ContactDaoImpl.getAllContacts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        contactCmb.setItems(contacts);
    }

    }


