package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDaoImpl {

    /**
     * Method Returns an Observable list of all Countries in DB.
     *
     * @return Observable list of all countries.
     */
    public static ObservableList<Country> getAllCountries() throws SQLException, Exception {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM countries";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        while (result.next()) {
            int countryId = result.getInt("Country_ID");
            String country = result.getString("Country");
            Country countryResult = new Country(country, countryId);
            allCountries.add(countryResult);
        }
        return allCountries;
    }

    public static Country country(String countryName) throws SQLException {
        Country country1 = null;
        String sqlStatement = "SELECT * FROM countries WHERE Country = '" + countryName + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        while (result.next()) {
            int countryId = result.getInt("Country_ID");
            String country = result.getString("Country");
            country1 = new Country(country, countryId);
        }
        return country1;
    }
}
