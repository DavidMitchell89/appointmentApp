package model;

public class Customer {

    private int customerId;

    private String customerName;

    private String customerAddress;

    private String postalCode;

    private String phone;

    private String country;

    private String division;

    public Customer(int customerId, String customerName, String customerAddress, String postalCode, String phone, String country, String division) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.postalCode = postalCode;
        this.phone = phone;
        this.country = country;
        this.division = division;
    }



    @Override
    public String toString() {
        return String.valueOf(customerId);
    }

    /**Gets Customer ID.
     * @return customerId*/
    public int getCustomerId() {
        return customerId;
    }

    /**Sets Customer ID.
     * @param customerId*/
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**Gets Customer Name.
     * @return customerName*/
    public String getCustomerName() {
        return customerName;
    }

    /**Sets Customer Name.
     * @param customerName*/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**Gets Customer Address.
     * @return customerAddress*/
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**Sets Customer Address.
     * @param customerAddress*/
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**Gets Customer postal code.
     * @return postalCode*/
    public String getPostalCode() {
        return postalCode;
    }

    /**Sets Customer postal code.
     * @param postalCode*/
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**Gets Customer phone number.
     * @return phone*/
    public String getPhone() {
        return phone;
    }

    /**Sets Customer phone number.
     * @param phone*/
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**Gets Customer country.
     * @return country*/
    public String getCountry() {
        return country;
    }

    /**Sets Customer Country.
     * @param country*/
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets Customer Division ID.
     * @return divisionId*/
    public String getDivision() {
        return division;
    }

    /**Sets Customer Division ID.
     * @param division*/
    public void setDivision(String division) {
        this.division = division;
    }
}
