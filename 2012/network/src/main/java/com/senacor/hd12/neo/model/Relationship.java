package com.senacor.hd12.neo.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.neo4j.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 15.06.12
 * Time: 13:18
 */
@RelationshipEntity(type = "RELATIONSHIP")
public class Relationship {

    @GraphId
    private Long id;
    @Fetch
    @StartNode
    private Person source;
    @Fetch
    @EndNode
    private Person destination;

    private RelationshipType type;

    public Relationship() {
    }

    public Relationship(Person source, Person destination, RelationshipType type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getSource() {
        return source;
    }

    public void setSource(Person source) {
        this.source = source;
    }

    public Person getDestination() {
        return destination;
    }

    public void setDestination(Person destination) {
        this.destination = destination;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
