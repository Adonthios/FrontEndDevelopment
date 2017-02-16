package nl.hu.frontenddevelopment.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Schultzie on 15-2-2017.
 */

public class Actor extends BaseModel {
    public Role role;
    public List<Person> personList;

    public Actor(Role role, List<Person> personList) {
        super();
        this.role = role;
        this.personList = personList;
    }

    public Actor(Role role) {
        super();
        this.role = role;
        this.personList = new ArrayList<Person>();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        super.Update();
        this.role = role;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public boolean addPerson(Person person) {
        if(!personList.contains(person)) {
            super.Update();
            return personList.add(person);
        }
        return false;
    }

    public boolean removePerson(Person person) {
        super.Update();
        return personList.remove(person);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Actor actor = (Actor) o;

        if (role != null ? !role.equals(actor.role) : actor.role != null) return false;
        return personList != null ? personList.equals(actor.personList) : actor.personList == null;
    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (personList != null ? personList.hashCode() : 0);
        return result;
    }
}
