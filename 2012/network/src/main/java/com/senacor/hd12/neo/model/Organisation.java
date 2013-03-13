package com.senacor.hd12.neo.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 15.06.12
 * Time: 23:16
 */
@NodeEntity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(JsonMethod.NONE)
public class Organisation {

    @GraphId
    private Long id;

    @JsonProperty
    @Indexed(unique = true)
    private String name;

    @JsonProperty
    @Indexed
    private String ort;

    public Organisation() {
    }

    public Organisation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrt() {
        return ort;
    }

    public Organisation setOrt(String ort) {
        this.ort = ort;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
