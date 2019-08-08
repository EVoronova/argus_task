package entities;

import java.io.Serializable;

public class AddressEntity implements Serializable {
    private String number;
    private String street;
    private String region;

    public AddressEntity() {
    }

    public AddressEntity(String street, String region, String number) {
        this.street = street;
        this.region = region;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
