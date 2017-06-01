package com.firebase.woflfish.goodbox;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 1/8/2017.
 */

public class Activities {
    String idCharity;
    String name;
    String startDate;
    String finishDate;
    String description;
    Map<String,String> urlImages;
    String createDate;

    public String getIdCharity() {
        return idCharity;
    }

    public void setIdCharity(String idCharity) {
        this.idCharity = idCharity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(Map<String, String> urlImages) {

        this.urlImages.putAll(urlImages);
    }

    public void setUrlImage(String note, String urlImage) {
        this.urlImages.put(note,urlImage);
    }
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Activities(String idCharity, String name, String startDate, String finishDate, String description, Map<String, String> urlImages, String createDate) {
        urlImages = new HashMap<String,String>();
        this.idCharity = idCharity;
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.description = description;
        this.urlImages.putAll(urlImages);
        this.createDate = createDate;
    }

    public Activities() {
        urlImages = new HashMap<String,String>();
    }
}
