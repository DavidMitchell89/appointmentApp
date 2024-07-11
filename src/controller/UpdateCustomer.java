package controller;

import DAO.CountryDaoImpl;
import DAO.CustomerDaoImpl;
import DAO.DivisionDaoImpl;
import DAO.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**Implements the Update Customer controller class.
 */
public class UpdateCustomer implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Label addressLbl;

    @FXML
    private TextField addressTxt;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<Country> countryCmb;

    @FXML
    private Label countryLbl;

    @FXML
    private Label idLbl;

    @FXML
    private TextField idTxt;

    @FXML
    private Label nameLbl;

    @FXML
    private TextField nameTxt;

    @FXML
    private Label phoneLbl;

    @FXML
    private TextField phoneTxt;

    @FXML
    private Label postalLbl;

    @FXML
    private TextField postalTxt;

    @FXML
    private ComboBox<Division> provinceCmb;

    @FXML
    private Label provinceLbl;

    @FXML
    private Button saveBtn;

    @FXML
    private Label updateCustomerLbl;

    /**Method pulls the country id for the selected country to be used for populating the division combo box.
     *
     * @param country
     * @return returns the country id for the selected country.
     */
    private int getCountryId(String country) throws SQLException {
        String sqlStatement = "SELECT Country_ID FROM countries WHERE Country = '" + country + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        int countryId = 0;
        while (result.next()) {
            countryId = result.getInt("Country_ID");
        }
        return countryId;
    }

    /**Method pulls the division id for the selected division.
     *
     * @param division
     * @return divisionId
     */
    private int getDivisionId(String division) throws SQLException {
        String sqlStatement = "SELECT Division_ID FROM first_level_divisions WHERE Division = '" + division + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        int divisionId = 0;
        while (result.next()) {
            divisionId = result.getInt("Division_ID");
        }
        return divisionId;
    }

    /** Populates the Division combobox based on the country selected by the user.
     */
    @FXML
    void countrySel(ActionEvent event) throws Exception {
        int countryId = getCountryId(String.valueOf(countryCmb.getSelectionModel().getSelectedItem()));
        provinceCmb.setItems(DivisionDaoImpl.filteredDivision(countryId));
    }

    /**method to return to main screen.
     * throws up alert verifying that any changes made will be lost.*/
    @FXML
    void cancelClk(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**Method Saves the inputs updated customer information to the Database.
     * returns user to main screen after saving.
     */
    @FXML
    void saveClk(ActionEvent event) throws SQLException {
        int divisionId = getDivisionId(String.valueOf(provinceCmb.getSelectionModel().getSelectedItem()));
        int rowsAffected = CustomerDaoImpl.newCustomer(nameTxt.getText(), addressTxt.getText(), postalTxt.getText(), phoneTxt.getText(), String.valueOf(divisionId));
        if (rowsAffected == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("please enter valid data in all fields.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            }));
        }
        else if (rowsAffected > 0) {
            try {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**Initializes Update Customer Screen.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            countryCmb.setItems(CountryDaoImpl.getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**method populates Update customer screen with information from customer selected on the main screen.
     *
     * @param customer
     * @throws Exception
     */
    public void receiveCustomer(Customer customer) throws Exception {
        countryCmb.setItems(CountryDaoImpl.getAllCountries());
        idTxt.setText(String.valueOf(customer.getCustomerId()));
        nameTxt.setText(customer.getCustomerName());
        addressTxt.setText(customer.getCustomerAddress());
        postalTxt.setText(customer.getPostalCode());
        phoneTxt.setText(customer.getPhone());
        countryCmb.setValue(CountryDaoImpl.country(customer.getCountry()));
        int countryId = getCountryId(String.valueOf(countryCmb.getSelectionModel().getSelectedItem()));
        provinceCmb.setItems(DivisionDaoImpl.filteredDivision(countryId));
        provinceCmb.setValue(DivisionDaoImpl.division1(customer.getDivision()));
    }

}