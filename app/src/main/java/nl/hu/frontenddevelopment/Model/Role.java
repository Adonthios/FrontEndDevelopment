package nl.hu.frontenddevelopment.Model;

/**
 * Created by Schultzie on 15-2-2017.
 */

public class Role extends BaseModel {

    // Deze shit uit de database trekken en in een RoleService zetten :D

    public String name;
    public String description;

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
