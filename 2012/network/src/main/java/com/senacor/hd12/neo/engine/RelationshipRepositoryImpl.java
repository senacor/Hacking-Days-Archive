package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 15.06.12
 * Time: 21:40
 */
public class RelationshipRepositoryImpl implements RelationshipRepositoryExtension {

    @Autowired
    private RelationshipRepository baseRepository;

    @Override
    @Transactional
    public boolean deleteRelationships(String usernameSource, String usernameDestination) {
        Collection<Relationship> relationships = baseRepository.findRelationships(usernameSource, usernameDestination);
        boolean somethingFound = relationships.isEmpty();
        baseRepository.delete(relationships);
        return !somethingFound;
    }
}
