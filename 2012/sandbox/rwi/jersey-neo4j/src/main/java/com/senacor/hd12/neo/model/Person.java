package com.senacor.hd12.neo.model;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Ralph Winzinger, Senacor
 */
@NodeEntity
public class Person {
    @GraphId
    private Long id;
    @Indexed
    private String firstname;
    @Indexed
    private String lastname;

    @RelatedTo(elementClass = Person.class, type = "KNOWS", direction = Direction.BOTH)
    Set<Person> knows;

    public Person() {
    }

    public Person(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Transactional
    public void isIntroducedTo(Person person) {
        knows.add(person);
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Set<Person> getKnows() {
        return knows;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("(").append(id).append(") ").append(firstname).append(" ").append(lastname).append(" (knows ");
        for (Person knownPerson : getKnows()) {
            sb.append(knownPerson.getFirstname()).append(" ").append(knownPerson.getLastname()).append(", ");
        }
        sb.append(")");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (id != null ? !id.equals(person.id) : person.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
