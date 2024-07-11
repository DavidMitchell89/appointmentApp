package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionDaoImpl {

    /**Method Queries Database and returns all divisions in Database.
     * @return Observable list of all divisions.*/

    public static ObservableList<Division> getAllDivisions() throws SQLException, Exception{
        ObservableList<Division> allDivisions = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM first_level_divisions";
        Query.makeQuery(sqlStatement);
        ResultSet result=Query.getResult();
        while (result.next()){
            int divisionId = result.getInt("Division_ID");
            String division = result.getString("Division");
            int countryId = result.getInt("Country_ID");
            Division divisionResult = new Division(divisionId, division, countryId);
            allDivisions.add(divisionResult);
        }
        return allDivisions;
    }

    public static ObservableList<Division> filteredDivision(int country) throws SQLException, Exception{
        ObservableList<Division> filterDivisions = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * From first_level_divisions WHERE country_ID = " + country;
        Query.makeQuery(sqlStatement);
        ResultSet result=Query.getResult();
        while (result.next()){
            int divisionId = result.getInt("Division_ID");
            String division = result.getString("Division");
            int countryId = result.getInt("Country_ID");
            Division divisionResult = new Division(divisionId, division, countryId);
            filterDivisions.add(divisionResult);
        }
        return filterDivisions;
    }

    public static Division division1 (String divisionName) throws SQLException {
        Division divisionSel = null;
        String sqlStatement = "SELECT * From first_level_divisions WHERE country_ID = '" + divisionName + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        while (result.next()) {
            int divisionId = result.getInt("Division_ID");
            String division = result.getString("Division");
            int countryId = result.getInt("Country_ID");
            divisionSel = new Division(divisionId, division, countryId);
        }
        return divisionSel;
    }



}
