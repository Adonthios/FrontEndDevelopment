package nl.hu.frontenddevelopment.Model;

import java.util.List;

public class Actor extends BaseModel {
    public String title,description;
    public List<ActorPerson> personList;

    public Actor(){
        super();
    }

    public Actor(String title, String description, List<ActorPerson> personList) {
        super();
        this.title = title;
        this.description = description;
        this.personList = personList;
    }

    public void setTitle(String title) {this.title =title;}
    public String getTitle(){ return title; }

    public String getDescription(){ return description; }

    public void setDescription(String description) {this.description = description;}

    public List<ActorPerson> getPersonList() {
        return personList;
    }

    public boolean addPerson(ActorPerson person) {
        if(!personList.contains(person)) {
            super.Update();
            return personList.add(person);
        }
        return false;
    }

    public boolean removePerson(ActorPerson person) {
        super.Update();
        return personList.remove(person);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Actor actor = (Actor) o;

        return personList != null ? personList.equals(actor.personList) : actor.personList == null;
    }

    @Override
    public int hashCode() {
        int result;
        result = 31 *(personList != null ? personList.hashCode() : 0);
        return result;
    }

}
