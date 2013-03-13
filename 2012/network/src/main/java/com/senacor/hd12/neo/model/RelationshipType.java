package com.senacor.hd12.neo.model;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 15.06.12
 * Time: 21:18
 */
public enum RelationshipType implements org.neo4j.graphdb.RelationshipType {
    FRIEND,
    COWORKER,
    INTERESTED
}
