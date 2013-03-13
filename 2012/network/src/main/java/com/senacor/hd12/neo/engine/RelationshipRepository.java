package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Relationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 15.06.12
 * Time: 16:19
 */
public interface RelationshipRepository extends GraphRepository<Relationship>, NamedIndexRepository<Relationship>,
        RelationshipRepositoryExtension {

    @Query("start user=node:Person(username = {0}) "
            + "match user-[rel:RELATIONSHIP]->friend "
            + "where friend.username = {1} "
            + "return rel")
    Collection<Relationship> findRelationships(String usernameSource, String usernameDestination);
}
