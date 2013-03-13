package com.senacor.hd12.neo.engine;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 15.06.12
 * Time: 21:38
 */
public interface RelationshipRepositoryExtension {

    /**
     * Delete all Relationships from Source Person to destination Person
     *
     * @param usernameSource      source username
     * @param usernameDestination destination username
     * @return <code>true</code> if a Relationship was deleted.
     */
    boolean deleteRelationships(String usernameSource, String usernameDestination);
}
