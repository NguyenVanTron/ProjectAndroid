package com.firebase.woflfish.goodbox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by USER on 1/8/2017.
 */

public class CharityOrganization extends User {
    String adress;
    String phoneNumber;
    String description;
    String urlPhoto;

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    Map<String,String> urlImages;
    Map<String,Boolean> activity;

    public  void setUrlImgae(String url, String index){
        this.urlImages.put(index,url);
    }
    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String,String> getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(Map<String,String> urlImages) {
        this.urlImages.putAll(urlImages);
    }
    public void setUrlImage(String urlImage,String note) {
        this.urlImages.put(note,urlImage);
    }
    public Map<String,Boolean> getActivity() {
        return activity;
    }

    public void setActivity(Map<String,Boolean> activity) {
        this.activity.putAll(activity);
    }
    public CharityOrganization(String fullName, String email, String adress, String phoneNumber, String description, Map<String,String> urlImages, Map<String,Boolean> activity) {
        super(fullName, email);
        this.urlImages = new HashMap<String,String>();
        this.activity = new HashMap<String,Boolean>();
        this.adress = adress;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.urlImages.putAll(urlImages);
        this.activity.putAll(activity);
    }

    public CharityOrganization() {
        this.urlImages = new HashMap<String,String>();
        this.activity = new HashMap<String,Boolean>();
    }
}
