package com.firebase.woflfish.goodbox;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 1/8/2017.
 */

public class DonationActivities extends Activities {
    Map<String,String> categories;
    Map<String,String> adress;
    Boolean offLocation;
    Boolean fromHome;

    Map<String,Boolean> idDonations;

    public void setOnlyAdress(String index, String adress){
        this.adress.put(index,adress);
    }
    public Map<String, String> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, String> categories) {
        this.categories.putAll(categories);
    }
    public void setDonation(String iddonation){
        this.idDonations.put(iddonation,true);
    }
    public Map<String, String> getAdress() {
        return adress;
    }

    public void setAdress(Map<String, String> adress) {
        this.adress.putAll(adress);
    }

    public boolean isOffLocation() {
        return offLocation;
    }

    public void setOffLocation(boolean offLocation) {
        this.offLocation = offLocation;
    }

    public boolean isFromHome() {
        return fromHome;
    }

    public void setFromHome(boolean fromHome) {
        this.fromHome = fromHome;
    }

    public Map<String, Boolean> getIdDonations() {
        return idDonations;
    }

    public void setIdDonations(Map<String, Boolean> idDonations) {
        this.idDonations.putAll(idDonations);
    }

    public DonationActivities(String idCharity, String name, String startDate, String finishDate, String description, Map<String, String> urlImages, String createDate, Map<String, String> categories, Map<String, String> adress, Boolean offLocation, Boolean fromHome, Map<String, Boolean> idDonations) {
        super(idCharity, name, startDate, finishDate, description, urlImages, createDate);
        categories = new HashMap<String,String>();
        adress = new HashMap<String,String>();
        this.categories.putAll(categories);
        this.adress.putAll(adress);
        this.offLocation = offLocation;
        this.fromHome = fromHome;
        this.idDonations.putAll(idDonations);
    }

    public DonationActivities() {
        categories = new HashMap<String,String>();
        adress = new HashMap<String,String>();
        idDonations = new HashMap<String,Boolean>();
    }
}
