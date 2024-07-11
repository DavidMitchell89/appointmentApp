package model;

public class Country {

    private int countryId;

    private String country;

    public Country(String country, int countryId) {
        this.country = country;
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return country;
    }

    /**Gets Country ID.
     * @return  countryId*/
    public int getCountryId() {
        return countryId;
    }

    /**Sets Country ID.
     * @param countryId*/
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**Gets Country name.
     * @return country*/
    public String getCountry() {
        return country;
    }

    /**Sets Country name.
     * @param country*/
    public void setCountry(String country) {
        this.country = country;
    }
}
