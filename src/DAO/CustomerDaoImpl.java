package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CustomerDaoImpl {

    /**
     * Method Retrieves all Customers from Database with Division Objects included in the query.
     *
     * @return ObservableList of all customers.
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException, Exception {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM customers AS c INNER JOIN first_level_divisions AS d ON c.Division_ID = d.Division_ID INNER JOIN countries AS co ON co.Country_ID=d.COUNTRY_ID";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        while (result.next()) {
            int customerId = result.getInt("Customer_ID");
            String customerName = result.getString("Customer_Name");
            String customerAddress = result.getString("Address");
            String postalCode = result.getString("Postal_Code");
            String phone = result.getString("Phone");
            String country = result.getString("Country");
            String division = result.getString("Division");
            Customer customerResult = new Customer(customerId, customerName, customerAddress, postalCode, phone, country, division);
            allCustomers.add(customerResult);
        }
        return allCustomers;

    }

    public static int newCustomer(String name,
                                  String address,
                                  String postal,
                                  String phone,
                                  String division) throws SQLException {
        String sqlStatement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postal);
        ps.setString(4, phone);
        ps.setString(5, division);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static Customer customer (int id) throws SQLException {
        String sqlStatement = "SELECT * FROM customers AS c INNER JOIN first_level_divisions AS d ON c.Division_ID = d.Division_ID INNER JOIN countries AS co ON co.Country_ID=d.COUNTRY_ID WHERE Customer_ID = " + id;
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        Customer customer = null;
        while (result.next()) {
            int customerId = result.getInt("Customer_ID");
            String customerName = result.getString("Customer_Name");
            String customerAddress = result.getString("Address");
            String postalCode = result.getString("Postal_Code");
            String phone = result.getString("Phone");
            String country = result.getString("Country");
            String division = result.getString("Division");
            customer = new Customer(customerId, customerName, customerAddress, postalCode, phone, country, division);
        }
        return customer;
    }

    public static int deleteCustomer(int customerId) throws SQLException {
        String sqlStatement = "DELETE FROM customers WHERE Customer_ID = " + customerId;
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
