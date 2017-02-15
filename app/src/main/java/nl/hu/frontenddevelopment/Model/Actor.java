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
}
