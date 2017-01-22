package com.example.uberv.vugraph2;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.apigee.sdk.data.client.entities.User;
import com.google.gson.annotations.Expose;

/**
 * Model class that wrapes necessary information from Apigee.User
 */
public class VuGraphUser implements Parcelable{

    private String name;
    private String accessToken;
    private String email;
    private String username;
    private String avatarUrl;
    private String role;
    @Expose(serialize = false)
    private transient Bitmap avatar;
    private long expiresAt = -1;

    public VuGraphUser() {
    }

    public VuGraphUser(User user) {
        name = user.getName();
        email = user.getEmail();
        username = user.getUsername();
        String role = user.getStringProperty("role");
        this.role = (role==null)||(role.isEmpty())?"guest":role;
        avatarUrl=user.getStringProperty("avatarUrl");
    }

    public boolean isAuthorized() {
        long a = System.currentTimeMillis();
        return accessToken != null // token exists
                && expiresAt != -1
                && expiresAt > System.currentTimeMillis(); // and it's not expired
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    protected VuGraphUser(Parcel in) {
        name = in.readString();
        accessToken = in.readString();
        email = in.readString();
        username = in.readString();
        avatarUrl = in.readString();
        role = in.readString();
        expiresAt = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(accessToken);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(avatarUrl);
        dest.writeString(role);
        dest.writeLong(expiresAt);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VuGraphUser> CREATOR = new Parcelable.Creator<VuGraphUser>() {
        @Override
        public VuGraphUser createFromParcel(Parcel in) {
            return new VuGraphUser(in);
        }

        @Override
        public VuGraphUser[] newArray(int size) {
            return new VuGraphUser[size];
        }
    };
}
