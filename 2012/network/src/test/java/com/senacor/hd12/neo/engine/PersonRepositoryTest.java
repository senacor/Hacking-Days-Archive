package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Organisation;
import com.senacor.hd12.neo.model.Person;
import com.senacor.hd12.neo.model.Relationship;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.kernel.impl.core.NodeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 15.06.12
 * Time: 13:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/spring-config.xml", "/spring-test-database-config.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Test
    public void testUserList() {
        Person p1 = new Person("Hans", "Wurst", "hwurst");
        personRepository.save(p1);
        Person friend1 = new Person("My", "Friend", "mfriend");
        personRepository.save(friend1);
        Person friend2 = new Person("Your", "Friend", "yfriend");
        personRepository.save(friend2);

        Iterable<Person> friends = personRepository.findAll();
        int i = 0;
        for (Person friend : friends) {
            System.out.println(">>> friend[" + i + "]: " + friend);
            assertNotNull(friend.getUsername());
            i++;
        }

        assertEquals(3, i);

    }


    @Test
    public void testGetPersonByUsername() {
        Person p = new Person("Hans", "Wurst", "hwurst");
        personRepository.save(p);
        assertNotNull(p.getId());

        Person hwurst = personRepository.getPersonByUsername("hwurst");
        System.out.println("gefundene Person: " + hwurst);
        assertNotNull(hwurst.getId());
        assertEquals(p.getId(), hwurst.getId());
    }

    @Test
    public void testSuggestFriends() {
        Person p1 = new Person("Hans", "Wurst", "hwurst");
        personRepository.save(p1);
        Person friend1 = new Person("My", "Friend", "mfriend");
        personRepository.save(friend1);
        Person friend2 = new Person("Your", "Friend", "yfriend");
        personRepository.save(friend2);
        //Relationship r1 = new Relationship(p1, friend1);
        //relationshipRepository.save(r1);
        //Relationship r2 = new Relationship(p1, friend2);
        //relationshipRepository.save(r2);
        p1.addRelationship(friend1);
        p1.addRelationship(friend2);
        personRepository.save(p1);

        Person hwurst = personRepository.getPersonByUsername("hwurst");
        System.out.println("gefundene person mit relationen: " + hwurst);
        assertEquals(2, hwurst.getRelationships().size());
        for (Relationship r : hwurst.getRelationships()) {
            System.out.println("fired.username=" + r.getDestination().getUsername());
        }

        Iterable<Person> friends = personRepository.findPeers("hwurst");
        int i = 0;
        for (Person friend : friends) {
            System.out.println("friend[" + i + "]: " + friend);
            assertNotNull(friend.getUsername());
            i++;
        }
        assertEquals(2, i);

        Iterable<Person> followers = personRepository.findFollowers("mfriend");
        assertTrue(followers.iterator().hasNext());
        assertEquals(followers.iterator().next().getFirstname(), "Hans");
//        assertTrue(followers.iterator().hasNext());

    }

    @Test
    public void testFindFriends() {
        Person p1 = new Person("Hans", "Wurst", "hwurst");
        personRepository.save(p1);
        Person friend1 = new Person("My", "Friend", "mfriend");
        personRepository.save(friend1);
        Person friend2 = new Person("Your", "Friend", "yfriend");
        personRepository.save(friend2);
        //Relationship r1 = new Relationship(p1, friend1);
        //relationshipRepository.save(r1);
        //Relationship r2 = new Relationship(p1, friend2);
        //relationshipRepository.save(r2);
        p1.addRelationship(friend1);
        p1.addRelationship(friend2);
        personRepository.save(p1);

        Person hwurst = personRepository.getPersonByUsername("hwurst");
        System.out.println("gefundene person mit relationen: " + hwurst);
        assertEquals(2, hwurst.getRelationships().size());
        for (Relationship r : hwurst.getRelationships()) {
            System.out.println("fired.username=" + r.getDestination().getUsername());
        }

        Iterable<Person> friends = personRepository.findFriends("hwurst");
        int i = 0;
        for (Person friend : friends) {
            System.out.println("friend[" + i + "]: " + friend);
            assertNotNull(friend.getUsername());
            i++;
        }
        assertEquals(2, i);
    }

    @Test
    public void testFindFriendsExt() {
        Person p1 = new Person("Hans", "Wurst", "hwurst");
        personRepository.save(p1);
        Person friend1 = new Person("My", "Friend", "mfriend");
        personRepository.save(friend1);
        Person friend2 = new Person("Your", "Friend", "yfriend");
        personRepository.save(friend2);
        Person friendOfFriend2 = new Person("FriendOf", "YourFriend", "foyf");
        personRepository.save(friendOfFriend2);

        p1.addRelationship(friend1);
        p1.addRelationship(friend2);
        friend2.addRelationship(friendOfFriend2);

        personRepository.save(p1);
        personRepository.save(friend2);

        Person yfriend = personRepository.getPersonByUsername("yfriend");
        System.out.println("gefundene person mit relationen: " + yfriend);
        assertEquals(1, yfriend.getRelationships().size());
        for (Relationship r : yfriend.getRelationships()) {
            System.out.println("fired.username=" + r.getDestination().getUsername());
        }

        EndResult friends = personRepository.findPeersExt("hwurst");
        int i = 0;
        for (Object row : friends) {
            Map<String, Object> map = (Map<String, Object>) row;
//            System.out.println(map.keySet().toArray());
            System.out.print(((NodeProxy)map.get("poi")).getProperty("username") + " ");
            System.out.println(map.get("friendcount"));
            i++;
        }
        assertEquals(2, i);
    }

    @Test
    public void testCreateRelationship() {
        Person p1 = new Person("Hans", "Wurst", "hwurst");
        personRepository.save(p1);
        Person friend1 = new Person("My", "Friend", "mfriend");
        personRepository.save(friend1);

        Relationship relationship = personRepository.createRelationship("hwurst", "mfriend");
        assertNotNull(relationship);

        Person hwurst = personRepository.findOne(p1.getId());
        System.out.println("found person with relationships: " + hwurst);
        assertEquals(1, hwurst.getRelationships().size());
        assertEquals("mfriend", hwurst.getRelationships().iterator().next().getDestination().getUsername());
    }

    @Test
    public void testSuggestUnknownPersons() {
        Organisation senacor = organisationRepository.save(new Organisation("Senacor"));
        Organisation foo = organisationRepository.save(new Organisation("foo"));

        Person hwurst = personRepository.save(new Person("Hans", "Wurst", "hwurst").setOrganisation(senacor));
        Person friend1 = personRepository.save(new Person("My", "Friend", "mfriend"));
        Person mff1 = personRepository.save(new Person("mff1", "mff1", "mff1"));
        Person mff2 = personRepository.save(new Person("mff2", "mff2", "mff2").setOrganisation(senacor));
        Person mff3 = personRepository.save(new Person("mff3", "mff3", "mff3"));
        Person mff2_1 = personRepository.save(new Person("mff2_1", "mff2_1", "mff2_1").setOrganisation(foo));
        Person mff2_1_1 = personRepository.save(new Person("mff2_1_1", "mff2_1_1", "mff2_1_1").setOrganisation(senacor));
        Person mff2_1_1_1 = personRepository.save(new Person("mff2_1_1_1", "mff2_1_1_1", "mff2_1_1_1"));
        relationshipRepository.save(hwurst.addRelationship(friend1));
        relationshipRepository.save(hwurst.addRelationship(mff3));
        relationshipRepository.save(friend1.addRelationship(mff1));
        relationshipRepository.save(friend1.addRelationship(mff2));
        relationshipRepository.save(friend1.addRelationship(mff3));
        relationshipRepository.save(mff2.addRelationship(mff2_1));
        relationshipRepository.save(mff2_1.addRelationship(mff2_1_1));
        relationshipRepository.save(mff2_1_1.addRelationship(mff2_1_1_1));

//        EndResult<Map<String, Object>> suggestedPersons = personRepository.suggestUnknownPersons("hwurst");
//        List<String> list = toList(map(new Function<Map<String, Object>, String>() {
//            @Override
//            public String map(Map<String, Object> row) {
//                NodeProxy person = (NodeProxy) row.get("friend_of_friend");
//                return (String) person.getProperty("username");
//            }
//        }, suggestedPersons));
//        for (int i = 0; i < list.size(); i++) {
//            Map<String, Object> row = list.get(i);
//            Integer pathDepth = (Integer) row.get("pathDepth");
//            NodeProxy person = (NodeProxy) row.get("friend_of_friend");
//            org.neo4j.graphdb.Relationship organisation = person.getSingleRelationship(new RelationshipType() {
//                @Override
//                public String name() {
//                    return "IS_EMPLOYED";
//                }
//            }, Direction.OUTGOING);
//            Integer orgPathLength = (Integer) row.get("orgPathLength");
//            System.out.println("suggestedPerson[" + i + "].username=" + person.getProperty("username")
//                    + " organisation=" +
//                    (null != organisation ? organisation.getEndNode().getProperty("name") : "<null>") +
//                    " pathDepth=" + pathDepth + " orgPathLength=" + orgPathLength);
//        }
        List<String> res = personRepository.suggestUnknowns("hwurst");
        assertEquals("Anzahl vorgeschlagene Persons", 4, res.size());
        assertThat(res).containsExactly(mff2.getUsername(), mff2_1_1.getUsername(), mff1.getUsername(),
                mff2_1.getUsername());
    }

    @Test
    public void testDelete() {
        Person p = new Person("Just", "Visiting", "jvisiting");
        personRepository.save(p);
        assertNotNull(p.getId());

        Person hwurst = personRepository.getPersonByUsername("jvisiting");
        System.out.println("gefundene Person: " + hwurst);
        assertNotNull(hwurst.getId());
        assertEquals(p.getId(), hwurst.getId());

        assertTrue(personRepository.deleteUser("jvisiting"));
        assertNull(personRepository.getPersonByUsername("jvisiting"));
        assertFalse(personRepository.deleteUser("jvisiting"));
    }

    @Test
    public void tesFindPersonsWithLastnameRegexp() {
        Person p1 = new Person("Hans", "Wurst", "hwurst");
        p1 = personRepository.save(p1);
        Person p2 = new Person("Peter", "Würstchen", "pwuerstchen");
        p2 = personRepository.save(p2);
        Person p3 = new Person("Max", "Mustermann", "max");
        personRepository.save(p3);

        Iterable<Person> result = personRepository.findPersonsWithLastnameStarting("W");

        assertThat(result).containsOnly(p1, p2);
    }

    @Test
    public void topTen() {
        Person p1 = new Person("Hans", "Wurst", "hwurst");
        personRepository.save(p1);
        Person friend1 = new Person("My", "Friend", "mfriend");
        personRepository.save(friend1);
        Person friend2 = new Person("Your", "Friend", "yfriend");
        personRepository.save(friend2);
        Person friendOfFriend2 = new Person("FriendOf", "YourFriend", "foyf");
        personRepository.save(friendOfFriend2);

        p1.addRelationship(friend1);
        p1.addRelationship(friend2);
        friend2.addRelationship(friendOfFriend2);
        
        personRepository.save(p1);
        personRepository.save(friend2);

        Person yfriend = personRepository.getPersonByUsername("yfriend");
        System.out.println("gefundene person mit relationen: " + yfriend);
        assertEquals(1, yfriend.getRelationships().size());
        for (Relationship r : yfriend.getRelationships()) {
            System.out.println("fired.username=" + r.getDestination().getUsername());
        }

        EndResult friends = personRepository.getTopTen();
        int i = 0;
        for (Object row : friends) {
        	Map<String,Object> map = (Map<String,Object>)row;
//            System.out.println(map.keySet().toArray());
        	if (((NodeProxy)map.get("poi")).hasProperty("username"))
        		System.out.print(((NodeProxy)map.get("poi")).getProperty("username") + " ");
        	System.out.println(map.get("friendcount"));
            i++;
        }
        assertEquals(3, i);
    }
}
