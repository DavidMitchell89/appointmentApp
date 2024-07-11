package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDaoImpl {


    /**
     * Method returns an observable list of all appointments in DB.
     *
     * @return Observable list of all appointments.
     */
    public static ObservableList<Appointment> getAllAppointments() throws Exception {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        String sqlStatement = "SELECT * FROM appointments AS a INNER JOIN contacts as c ON a.Contact_ID=c.Contact_ID";

        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        while (result.next()) {
            int apptId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String desc = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDate startDate = result.getTimestamp("Start").toLocalDateTime().toLocalDate();
            LocalTime startTime = result.getTimestamp("Start").toLocalDateTime().toLocalTime();
            LocalDate endDate = result.getTimestamp("End").toLocalDateTime().toLocalDate();
            LocalTime endTime = result.getTimestamp("End").toLocalDateTime().toLocalTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contactId = result.getInt("Contact_ID");
            Appointment appointmentResult = new Appointment(apptId, title, desc, location, type, startDate, startTime, endDate, endTime, custId, userId, contactId);
            allAppointments.add(appointmentResult);


        }
        return allAppointments;

    }

    public static ObservableList<Appointment> getCurrentWeekAppointments() throws Exception {
        ObservableList<Appointment> currentWeekAppointments = FXCollections.observableArrayList();
        LocalDate ldToday = LocalDate.now();
        LocalDate ldWeekStart = ldToday.minusDays(ldToday.getDayOfWeek().getValue());
        LocalDate ldWeekEnd = ldWeekStart.plusDays(7);
        Timestamp weekStart = Timestamp.valueOf(ldWeekStart.atStartOfDay());
        Timestamp weekEnd = Timestamp.valueOf(ldWeekEnd.atStartOfDay());
        String sqlStatement = "SELECT * FROM appointments AS a INNER JOIN contacts as c ON a.Contact_ID=c.Contact_ID WHERE Start BETWEEN '" + weekStart +   "' and '" + weekEnd + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        while (result.next()) {
            int apptId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String desc = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDate startDate = result.getTimestamp("Start").toLocalDateTime().toLocalDate();
            LocalTime startTime = result.getTimestamp("Start").toLocalDateTime().toLocalTime();
            LocalDate endDate = result.getTimestamp("End").toLocalDateTime().toLocalDate();
            LocalTime endTime = result.getTimestamp("End").toLocalDateTime().toLocalTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contactId = result.getInt("Contact_ID");
            Appointment appointmentResult = new Appointment(apptId, title, desc, location, type, startDate, startTime, endDate, endTime, custId, userId, contactId);
            currentWeekAppointments.add(appointmentResult);


        }
        return currentWeekAppointments;
    }

    public static ObservableList<Appointment> getCurrentMonthAppointments() throws Exception {
        ObservableList<Appointment> currentMonthAppointments = FXCollections.observableArrayList();
        LocalDate ldToday = LocalDate.now();
        LocalDate monthStart = ldToday.minusDays((LocalDate.now().getDayOfMonth() - 1));
        Timestamp today = Timestamp.valueOf(monthStart.atStartOfDay());
        Timestamp nextMonth = Timestamp.valueOf(monthStart.plusDays(31).atStartOfDay());
        String sqlStatement = "SELECT * FROM appointments AS a INNER JOIN contacts as c ON a.Contact_ID=c.Contact_ID WHERE Start BETWEEN '" + today + "' and '" + nextMonth + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        while (result.next()) {
            int apptId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String desc = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDate startDate = result.getTimestamp("Start").toLocalDateTime().toLocalDate();
            LocalTime startTime = result.getTimestamp("Start").toLocalDateTime().toLocalTime();
            LocalDate endDate = result.getTimestamp("End").toLocalDateTime().toLocalDate();
            LocalTime endTime = result.getTimestamp("End").toLocalDateTime().toLocalTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contactId = result.getInt("Contact_ID");
            Appointment appointmentResult = new Appointment(apptId, title, desc, location, type, startDate, startTime, endDate, endTime, custId, userId, contactId);
            currentMonthAppointments.add(appointmentResult);
        }
        return currentMonthAppointments;
}

        public static ObservableList<Appointment> getUpcomingAppointments(Timestamp start) throws Exception {
            ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();
            LocalDate today = LocalDate.from(LocalDate.now().atStartOfDay());
            LocalTime lTEnd = start.toLocalDateTime().toLocalTime().plusMinutes(15);
            LocalDateTime lDTend = LocalDateTime.of(today, lTEnd);
            Timestamp end = Timestamp.valueOf(lDTend);
            String sqlStatment = "SELECT * FROM appointments AS a INNER JOIN contacts as c ON a.Contact_ID=c.Contact_ID WHERE Start BETWEEN '" + start + "' and '" + end + "'";
            Query.makeQuery(sqlStatment);
            ResultSet result = Query.getResult();
            while (result.next()) {
                int apptId = result.getInt("Appointment_ID");
                String title = result.getString("Title");
                String desc = result.getString("Description");
                String location = result.getString("Location");
                String type = result.getString("Type");
                LocalDate startDate = result.getTimestamp("Start").toLocalDateTime().toLocalDate();
                LocalTime startTime = result.getTimestamp("Start").toLocalDateTime().toLocalTime();
                LocalDate endDate = result.getTimestamp("End").toLocalDateTime().toLocalDate();
                LocalTime endTime = result.getTimestamp("End").toLocalDateTime().toLocalTime();
                int custId = result.getInt("Customer_ID");
                int userId = result.getInt("User_ID");
                int contactId = result.getInt("Contact_ID");
                Appointment appointmentResult = new Appointment(apptId, title, desc, location, type, startDate, startTime, endDate, endTime, custId, userId, contactId);
                upcomingAppointments.add(appointmentResult);
            }
            return upcomingAppointments;
        }



    public static int updateAppointment(int apptId,                                     String title,
                                     String desc,
                                     String location,
                                     String type,
                                     Timestamp start,
                                     Timestamp end,
                                     int custId,
                                     int userId,
                                     int contactId) throws SQLException {
        String sqlStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = " + apptId;
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ps.setString(1, title);
        ps.setString(2, desc);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, custId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int deleteAppointment(int apptId) throws SQLException {
        String sqlStatement = "DELETE FROM appointments WHERE Appointment_ID = " + apptId;
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int deleteAppointmentByCustomerId(int custId) throws SQLException {
        String sqlStatement = "DELETE FROM appointments WHERE Customer_ID = " + custId;
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int newAppointment(String title,
                                        String desc,
                                        String location,
                                        String type,
                                        Timestamp start,
                                        Timestamp end,
                                        int custId,
                                        int userId,
                                        int contactId) throws SQLException {
        String sqlStatement = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
        ps.setString(1, title);
        ps.setString(2, desc);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, custId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int appointmentCount(LocalDate monthStart, LocalDate monthEnd, String type) throws SQLException {
        String monthStartString = String.valueOf(monthStart);
        String monthEndString = String.valueOf(monthEnd);
        String sqlStatement = "SELECT COUNT(*) FROM client_schedule.appointments WHERE Type = '" + type + "' AND Start Between '" + monthStartString + "' AND '"+ monthEndString +"'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        int rowsAffected = 0;
        while (result.next()) {
            rowsAffected = (int) result.getLong(1);
        }
        return rowsAffected;
    }

    public static ObservableList<Appointment> contactAppointments (int selContactId) throws SQLException {
        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM appointments WHERE Contact_ID = '" + selContactId + "'";
        System.out.println(sqlStatement);
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        while (result.next()) {
            int apptId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String desc = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDate startDate = result.getTimestamp("Start").toLocalDateTime().toLocalDate();
            LocalTime startTime = result.getTimestamp("Start").toLocalDateTime().toLocalTime();
            LocalDate endDate = result.getTimestamp("End").toLocalDateTime().toLocalDate();
            LocalTime endTime = result.getTimestamp("End").toLocalDateTime().toLocalTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contactId = result.getInt("Contact_ID");
            Appointment appointmentResult = new Appointment(apptId, title, desc, location, type, startDate, startTime, endDate, endTime, custId, userId, contactId);
            contactAppointments.add(appointmentResult);
        }
        return contactAppointments;
    }

    public static int customerAppointments (int customerId) throws SQLException{
        int customerCount = 0;
        String sqlStatement = "COUNT * FROM appointments WHERE Customer_ID = '" + customerId + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        while (result.next()) {
            customerCount = (int) result.getLong(1);
        }
        return customerCount;
    }

    public static ObservableList<Appointment> overlapAppointments (int customerId, Timestamp start, Timestamp end) throws SQLException {
        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM appointments WHERE Customer_ID = '" + customerId + "' AND (Start Between '" + start + "' AND '" + end + "') OR (End Between '" + start + "' AND '" + end + "')";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        while (result.next()) {
            int apptId = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String desc = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            LocalDate startDate = result.getTimestamp("Start").toLocalDateTime().toLocalDate();
            LocalTime startTime = result.getTimestamp("Start").toLocalDateTime().toLocalTime();
            LocalDate endDate = result.getTimestamp("End").toLocalDateTime().toLocalDate();
            LocalTime endTime = result.getTimestamp("End").toLocalDateTime().toLocalTime();
            int custId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contactId = result.getInt("Contact_ID");
            Appointment appointmentResult = new Appointment(apptId, title, desc, location, type, startDate, startTime, endDate, endTime, custId, userId, contactId);
            customerAppointments.add(appointmentResult);
        }
        return customerAppointments;
    }



    public static ObservableList<String> types = FXCollections.observableArrayList("Planning Session", "De-Briefing");


    }


