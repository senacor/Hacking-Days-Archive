package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Person;
import com.senacor.hd12.neo.model.Relationship;
import org.neo4j.helpers.Function;
import org.neo4j.kernel.impl.core.NodeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.neo4j.helpers.collection.Iterables.map;
import static org.neo4j.helpers.collection.Iterables.toList;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 15.06.12
 * Time: 14:46
 */
public class PersonRepositoryImpl implements PersonRepositoryExtension {
    @Autowired
    private PersonRepository baseRepository;

    @Autowired
    private Neo4jTemplate template;

    @Override
    public Set<Person> findFriends(String username) {
        Person p = baseRepository.getPersonByUsername(username);
        Set<Person> allFriends = new HashSet<Person>();
        if (null != p) {
            for (Relationship r : p.getRelationships()) {
                allFriends.add(r.getDestination());
            }
        }
        return allFriends;
    }

    @Override
    @Transactional
    public boolean deleteUser(String username) {
        Person p = baseRepository.getPersonByUsername(username);
        if (p == null || p.getId() == null)
            return false;
        baseRepository.delete(p);
        return true;
    }

    @Override
    @Transactional
    public Relationship createRelationship(String usernameSource, String usernameDestination) {
        Person source = baseRepository.getPersonByUsername(usernameSource);
        if (null == source) {
            return null;
        }
        Person destination = baseRepository.getPersonByUsername(usernameDestination);
        if (null == destination) {
            return null;
        }
        return source.addRelationship(destination);
    }

    @Override
    @Transactional
    public Iterable<Person> findPersonsWithLastnameStarting(String lastNameFragment) {
        return baseRepository.findAllByQuery("lastnameFulltext", "lastname", lastNameFragment + "*");
    }

    @Override
    public List<String> suggestUnknowns(String username) {
        EndResult<Map<String, Object>> suggestedPersons = baseRepository.suggestUnknownPersons(username);
        List<String> res = toList(map(new Function<Map<String, Object>, String>() {
            @Override
            public String map(Map<String, Object> row) {
                NodeProxy person = (NodeProxy) row.get("friend_of_friend");
                return (String) person.getProperty("username");
            }
        }, suggestedPersons));
        return res;
    }

    @Override
    @Transactional
    public void saveOrReplacePerson(Person person) {
        Person oldPerson = baseRepository.getPersonByUsername(person.getUsername());
        if(oldPerson != null) {
            baseRepository.delete(oldPerson);
        }
        baseRepository.save(person);
    }
}
