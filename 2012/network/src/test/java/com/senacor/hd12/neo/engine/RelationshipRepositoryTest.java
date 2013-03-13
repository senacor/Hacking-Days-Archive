package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Person;
import com.senacor.hd12.neo.model.Relationship;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 15.06.12
 * Time: 22:00
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/spring-config.xml", "/spring-test-database-config.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class RelationshipRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Test
    public void testFindRelationships() {
        Person p1 = new Person("Hans", "Wurst", "hwurst");
        personRepository.save(p1);
        Person friend1 = new Person("My", "Friend", "mfriend");
        personRepository.save(friend1);
        Person friend2 = new Person("Your", "Friend", "yfriend");
        personRepository.save(friend2);
        Relationship r1 = p1.addRelationship(friend1);
        Relationship r2 = p1.addRelationship(friend2);
        personRepository.save(p1);

        Collection<Relationship> relations = relationshipRepository.findRelationships("hwurst", "mfriend");
        assertEquals("anzahl Relations", 1, relations.size());
        for (Relationship r : relations) {
            System.out.println("found Relation: " + r);
        }
    }

    @Test
    public void testDeleteRelationships() {
        Person p1 = new Person("Hans", "Wurst", "hwurst");
        personRepository.save(p1);
        Person friend1 = new Person("My", "Friend", "mfriend");
        personRepository.save(friend1);
        Person friend2 = new Person("Your", "Friend", "yfriend");
        personRepository.save(friend2);
        Relationship r1 = p1.addRelationship(friend1);
        Relationship r2 = p1.addRelationship(friend2);
        personRepository.save(p1);

        Collection<Relationship> relations = relationshipRepository.findRelationships("hwurst", "mfriend");
        assertEquals("anzahl Relations", 1, relations.size());

        boolean somethingDeleted = relationshipRepository.deleteRelationships("hwurst", "mfriend");
        assertEquals(true, somethingDeleted);
        somethingDeleted = relationshipRepository.deleteRelationships("hwurst", "mfriend");
        assertEquals(false, somethingDeleted);

        assertEquals(0, relationshipRepository.findRelationships("hwurst", "mfriend").size());
        assertEquals(1, relationshipRepository.findRelationships("hwurst", "yfriend").size());
    }
}
