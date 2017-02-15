package nl.hu.frontenddevelopment.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lars on 2/15/2017.
 */

public class Project extends BaseModel {
    public String name;
    public String description;
    public List<Actor> actorList;

    public Project(String name, String description) {
        super();
        this.name = name;
        this.description = description;
        this.actorList = new ArrayList<Actor>();
    }

    public Project(String name, String description, ArrayList<Actor> actorList) {
        super();
        this.name = name;
        this.description = description;
        this.actorList = actorList;
    }
}
