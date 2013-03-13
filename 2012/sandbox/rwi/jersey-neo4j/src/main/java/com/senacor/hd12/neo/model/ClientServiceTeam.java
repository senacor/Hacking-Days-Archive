package com.senacor.hd12.neo.model;

import org.neo4j.graphdb.Direction;
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
public class ClientServiceTeam {
    @GraphId
    private Long id;
    @Indexed
    private String customer;

    @RelatedTo(elementClass = Person.class, type = "MEMBER", direction = Direction.BOTH)
    Set<Person> members;

    public ClientServiceTeam() {
    }

    public ClientServiceTeam(String customer) {
        this.customer = customer;
    }

    public void affilate(Person person) {
        members.add(person);
    }

    public Long getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public Set<Person> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "("+id+") "+"CST<"+customer+">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientServiceTeam)) return false;

        ClientServiceTeam person = (ClientServiceTeam) o;

        if (id != null ? !id.equals(person.id) : person.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
