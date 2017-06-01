package com.firebase.woflfish.goodbox;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 1/13/2017.
 */

public class AtDropOffLocation extends Donations {
    Map<String,String> adress;

    public Map<String, String> getAdress() {
        return adress;
    }

    public void setAdress(Map<String, String> adress) {
        this.adress.putAll(adress);
    }

    public AtDropOffLocation(String idActivity, String idDonor, Map<String, String> categories, Map<String, String> urlImages, String description, int status, String donationDate, Map<String, String> adress) {
        super(idActivity, idDonor, categories, urlImages, description, status, donationDate);
        this.adress = new HashMap<String,String>();
        this.adress.putAll(adress);
    }

    public AtDropOffLocation() {
    this.adress = new HashMap<String,String>();
    }
}
