package model;

public class Contact {

    private int contactId;

    private String contactName;

    private String email;

    public Contact(int contactId, String contactName, String email) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.valueOf(contactId);
    }

    /**Gets Contact ID.
     * @return contactId*/
    public int getContactId() {
        return contactId;
    }

    /**Sets Contact ID.
     * @param contactId*/
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**Gets Contact Name.
     * @return contactName*/
    public String getContactName() {
        return contactName;
    }

    /**Sets contact Name.
     * @param contactName*/
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**Gets Contact Email.
     * @return email*/
    public String getEmail() {
        return email;
    }

    /**Sets Contact email.
     * @param email*/
    public void setEmail(String email) {
        this.email = email;
    }
}
