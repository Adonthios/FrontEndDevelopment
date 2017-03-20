package nl.hu.frontenddevelopment.Model;

import java.util.Date;

public class BaseModel {
    public Date dateCreated;
    public Date dateUpdated;
    public String key;

    public BaseModel() {
        this.dateCreated = new Date();
        this.dateUpdated = this.dateCreated;
    }

    public void Update() {
        this.dateUpdated = new Date();
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }
}