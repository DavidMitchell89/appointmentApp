package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int apptId;

    private String title;

    private String desc;

    private String location;

    private String type;

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;

    private int custId;

    private int userId;

    private int contactId;




    public Appointment(int apptId, String title, String desc, String location, String type, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, int custId, int userId, int contactId) {
        this.apptId = apptId;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.custId = custId;
        this.userId = userId;
        this.contactId = contactId;
    }

    @Override
    public String toString() {
        return apptId +
                title +
                desc +
                location +
                type +
                startDate +
                startTime +
                endDate +
                endTime +
                custId +
                userId +
                contactId;

    }

    /**Gets appointment ID.
     * @return apptId*/
    public int getApptId() {
        return apptId;
    }

    /**Sets appointment ID.
     * @param apptId*/
    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    /**Gets appointment title.
     * @return title*/
    public String getTitle() {
        return title;
    }

    /**Sets appointment title.
     * @param title*/
    public void setTitle(String title) {
        this.title = title;
    }

    /**Gets appointment description.
     * @return desc*/
    public String getDesc() {
        return desc;
    }

    /**Sets appointment description.
     * @param desc*/
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**Gets appointment location.
     * @return location*/
    public String getLocation() {
        return location;
    }

    /**Sets appointment location.
     * @param location*/
    public void setLocation(String location) {
        this.location = location;
    }

    /**Gets appointment type.
     * @return type*/
    public String getType() {
        return type;
    }

    /**Sets appointment type.
     * @param type*/
    public void setType(String type) {
        this.type = type;
    }

    /**Gets appointment start date.
     * @return startDate*/
    public LocalDate getStartDate() {
        return startDate;
    }

    /**Sets appointment start date.
     * @param startDate*/
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**Gets appointment start time.
     * @return startTime*/
    public LocalTime getStartTime() {
        return startTime;
    }

    /**Sets appointment start time.
     * @param startTime*/
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**Gets appointment end Date.
     * @return end*/
    public LocalDate getEndDate() {
        return endDate;
    }

    /**Sets appointment end date.
     * @param endDate*/
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**Gets appointment end time.
     * @return endTime*/
    public LocalTime getEndTime() {
        return endTime;
    }

    /**Sets appointment end time.
     * @param endTime*/
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**Gets appointment customer ID.
     * @return custId*/
    public int getCustId() {
        return custId;
    }

    /**Sets appointment Customer ID.
     * @param custId*/
    public void setCustId(int custId) {
        this.custId = custId;
    }

    /**Gets appointment userID.
     * @return userId*/
    public int getUserId() {
        return userId;
    }

    /**Sets appointment user ID.
     * @param userId*/
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**Gets appointment contact ID.
     * @return contactId*/
    public int getContactId() {
        return contactId;
    }

    /**Sets appointment contact ID.
     * @param contactId*/
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
