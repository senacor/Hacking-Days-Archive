package com.senacor.hd12.neo.model;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

/**
 * Ralph Winzinger, Senacor
 */
@NodeEntity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(JsonMethod.NONE)
public class Person {
    @GraphId
    private Long id;

    @Indexed
    @JsonProperty
    private String firstname;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "lastnameFulltext")
    @JsonProperty
    private String lastname;

    @Indexed(unique = true)
    @JsonProperty
    private String username;

    @JsonProperty
    private String avatar;
    
    @RelatedToVia
    private Set<Relationship> relationships = new HashSet<Relationship>(0);

    @Fetch
    @RelatedTo(direction = Direction.OUTGOING, type = "IS_EMPLOYED")
    @JsonProperty
    private Organisation organisation;

    public Person() {
    }

    public Person(String firstname, String lastname, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
    }


    public Person(String firstname, String lastname, String username, String avatar) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.avatar = avatar;
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

    @JsonIgnore
    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }

    public Relationship addRelationship(Person p) {
        Relationship newRelationship = new Relationship(this, p, RelationshipType.INTERESTED);
        relationships.add(newRelationship);
        return newRelationship;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public Person setOrganisation(Organisation organisation) {
        this.organisation = organisation;
        return this;
    }

    public String getAvatar() {
		return avatar;
	}
    
    public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        return new EqualsBuilder()
                .append(id, person.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(31, 43)
                .append(id)
                .toHashCode();
    }
}
