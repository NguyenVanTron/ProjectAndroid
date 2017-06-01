package com.firebase.woflfish.goodbox;

import java.util.Map;

/**
 * Created by USER on 1/13/2017.
 */

public class PickedUpFromHome extends Donations {
    String name;
    String phoneNumber;
    String adress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public PickedUpFromHome(String idActivity, String idDonor, Map<String, String> categories, Map<String, String> urlImages, String description, int status, String donationDate, String name, String phoneNumber, String adress) {
        super(idActivity, idDonor, categories, urlImages, description, status, donationDate);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.adress = adress;
    }

    public PickedUpFromHome() {

    }
}
