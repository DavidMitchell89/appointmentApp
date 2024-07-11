package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDaoImpl {

    /**Method returns an observable list of all contacts in DB.
     * @return Observable list of all contacts.*/

    public static ObservableList<Contact> getAllContacts() throws SQLException{
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();

        String sqlStatement = "Select * FROM contacts";
        Query.makeQuery(sqlStatement);
        ResultSet result=Query.getResult();
        while (result.next()){
            int contactId = result.getInt("Contact_ID");
            String contactName = result.getString("Contact_Name");
            String email = result.getString("Email");
            Contact contactResult = new Contact(contactId, contactName, email);
            allContacts.add(contactResult);
        }
        return allContacts;
    }

    public static Contact contact (int id) throws SQLException {
        String sqlStatement = "Select * From contacts WHERE Contact_ID = " + id;
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        Contact contact = null;
        while (result.next()) {
            int contactId = result.getInt("Contact_ID");
            String contactName = result.getString("Contact_Name");
            String email = result.getString("Email");
            contact = new Contact(contactId, contactName, email);
        }
        return contact;
    }
}
