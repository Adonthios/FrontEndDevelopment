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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (email != null ? !email.equals(person.email) : person.email != null) return false;
        if (function != null ? !function.equals(person.function) : person.function != null)
            return false;
        if (phonenumber != null ? !phonenumber.equals(person.phonenumber) : person.phonenumber != null)
            return false;
        if (sidenote != null ? !sidenote.equals(person.sidenote) : person.sidenote != null)
            return false;
        return profilePhoto != null ? profilePhoto.equals(person.profilePhoto) : person.profilePhoto == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (function != null ? function.hashCode() : 0);
        result = 31 * result + (phonenumber != null ? phonenumber.hashCode() : 0);
        result = 31 * result + (sidenote != null ? sidenote.hashCode() : 0);
        result = 31 * result + (profilePhoto != null ? profilePhoto.hashCode() : 0);
        return result;
    }
}
