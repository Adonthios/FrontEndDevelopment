package nl.hu.frontenddevelopment.Model;

import android.media.Image;

/**
 * Created by Schultzie on 15-2-2017.
 */

public class Person extends BaseModel {
    public String name;
    public String email;
    public String function;
    public String phonenumber;
    public String sidenote;
    public Image profilePhoto;

    public Person(String name, String email) {
        super();
        this.name = name;
        this.email = email;
    }

    public Person(String name, String email, String function, String phonenumber, String sidenote, Image profilePhoto) {
        this.name = name;
        this.email = email;
        this.function = function;
        this.phonenumber = phonenumber;
        this.sidenote = sidenote;
        this.profilePhoto = profilePhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSidenote() {
        return sidenote;
    }

    public void setSidenote(String sidenote) {
        this.sidenote = sidenote;
    }

    public Image getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Image profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
