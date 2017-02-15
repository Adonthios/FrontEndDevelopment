package nl.hu.frontenddevelopment.Controller;

import java.util.List;

import nl.hu.frontenddevelopment.Model.Person;

/**
 * Created by Schultzie on 15-2-2017.
 */

public class PersonService {
    public List<Person> personList;

    public List<Person> getPersonList() {

        // Haal uit de database en/of contacten
        return personList;
    }
}
