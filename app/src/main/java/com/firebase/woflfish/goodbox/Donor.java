package com.firebase.woflfish.goodbox;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 1/8/2017.
 */

public class Donor extends User {
    Map<String,Boolean> donation;

    public Map<String,Boolean> getDonation() {
        return donation;
    }

    public void setDonation(Map<String,Boolean> donation) {
        this.donation.putAll(donation);
    }
    public Donor() {
        this.donation = new HashMap<String,Boolean>();
    }

    public Donor(String fullName, String email, Map<String,Boolean> history) {
        super(fullName, email);
        this.donation = new HashMap<String,Boolean>();
        this.donation.putAll(history);

    }
}
