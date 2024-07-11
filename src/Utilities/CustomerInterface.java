package Utilities;

import java.sql.SQLException;

@FunctionalInterface
public interface CustomerInterface {

    int customerCount (int customer) throws SQLException;
}
