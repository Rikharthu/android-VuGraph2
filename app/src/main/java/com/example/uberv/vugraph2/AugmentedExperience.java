package com.example.uberv.vugraph2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AugmentedExperience implements Parcelable {

    private String heading;
    private String subheading;
    private String description;
    private String minimalAccessRole;
    private String imageName;
    private List<String> features;
    private int vuforiaRating;
    private String playerPackage;


    public AugmentedExperience() {
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMinimalAccessRole() {
        return minimalAccessRole;
    }

    public void setMinimalAccessRole(String minimalAccessRole) {
        this.minimalAccessRole = minimalAccessRole;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public int getVuforiaRating() {
        return vuforiaRating;
    }

    public void setVuforiaRating(int vuforiaRating) {
        this.vuforiaRating = vuforiaRating;
    }

    public String getPlayerPackage() {
        return playerPackage;
    }

    public void setPlayerPackage(String playerPackage) {
        this.playerPackage = playerPackage;
    }

    protected AugmentedExperience(Parcel in) {
        heading = in.readString();
        subheading = in.readString();
        description = in.readString();
        minimalAccessRole = in.readString();
        imageName = in.readString();
        if (in.readByte() == 0x01) {
            features = new ArrayList<String>();
            in.readList(features, String.class.getClassLoader());
        } else {
            features = null;
        }
        vuforiaRating = in.readInt();
        playerPackage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(heading);
        dest.writeString(subheading);
        dest.writeString(description);
        dest.writeString(minimalAccessRole);
        dest.writeString(imageName);
        if (features == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(features);
        }
        dest.writeInt(vuforiaRating);
        dest.writeString(playerPackage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AugmentedExperience> CREATOR = new Parcelable.Creator<AugmentedExperience>() {
        @Override
        public AugmentedExperience createFromParcel(Parcel in) {
            return new AugmentedExperience(in);
        }

        @Override
        public AugmentedExperience[] newArray(int size) {
            return new AugmentedExperience[size];
        }
    };
}
