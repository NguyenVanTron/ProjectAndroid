package com.firebase.woflfish.goodbox;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 1/13/2017.
 */

public class Donations {
    String idActivity;
    String idDonor;
    Map<String,String> categories;
    Map<String,String> urlImages;
    String description;
    int status;

    public void setUrlImage(String index, String url){
        this.urlImages.put(index,url);
    }
    public String getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(String idActivity) {
        this.idActivity = idActivity;
    }

    public String getIdDonor() {
        return idDonor;
    }

    public void setIdDonor(String idDonor) {
        this.idDonor = idDonor;
    }

    public Map<String, String> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, String> categories) {
        this.categories.putAll(categories);
    }

    public Map<String, String> getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(Map<String, String> urlImages) {
        this.urlImages.putAll(urlImages);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }

    String donationDate;

    public Donations(String idActivity, String idDonor, Map<String, String> categories, Map<String, String> urlImages, String description, int status, String donationDate) {
        this.categories = new HashMap<String,String>();
        this.urlImages = new HashMap<String,String>();
        this.idActivity = idActivity;
        this.idDonor = idDonor;
        this.categories.putAll(categories);
        this.urlImages.putAll(urlImages);
        this.description = description;
        this.status = status;
        this.donationDate = donationDate;
    }

    public Donations() {
        this.categories = new HashMap<String,String>();
        this.urlImages = new HashMap<String,String>();
    }
}
