package com.example.uberv.vugraph2;

import com.apigee.sdk.data.client.entities.User;

/** Model class that wrapes necessary information from Apigee.User */
public class VuGraphUser {

    private String firstname;
    private String lastname;
    private String accessToken;
    private String email;
    private String username;
    private long expiresAt=-1;
//    private String role;

    public VuGraphUser() {
    }

    public VuGraphUser(User user) {
        firstname=user.getFirstname();
        lastname=user.getLastname();
        email=user.getEmail();
        username=user.getUsername();
        //role = user.getPicture();
    }

//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }


}
