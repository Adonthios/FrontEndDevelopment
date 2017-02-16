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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        super.Update();
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        super.Update();
        this.description = description;
    }

    public List<Actor> getActorList() {
        return actorList;
    }

    public void setActorList(List<Actor> actorList) {
        super.Update();
        this.actorList = actorList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (name != null ? !name.equals(project.name) : project.name != null) return false;
        if (description != null ? !description.equals(project.description) : project.description != null)
            return false;
        return actorList != null ? actorList.equals(project.actorList) : project.actorList == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (actorList != null ? actorList.hashCode() : 0);
        return result;
    }
}
