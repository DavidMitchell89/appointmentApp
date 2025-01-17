package DAO;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static DAO.JDBC.connection;

public abstract class Query {

    private static String query;

    private static Statement stmt;

    private static ResultSet result;

    public static void makeQuery(String q){
        query = q;
        try {
            stmt=connection.createStatement();
            if(query.toLowerCase().startsWith("select"))
                result=stmt.executeQuery(q);
             if(query.toLowerCase().startsWith("delete")||query.toLowerCase().startsWith("insert")||query.toLowerCase().startsWith("update"))
                stmt.executeUpdate(q);
        }
        catch (Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
    }

    public static ResultSet getResult() {
        return result;
    }
}
