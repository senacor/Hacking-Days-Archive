package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Person;
import com.senacor.hd12.neo.model.Relationship;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 15.06.12
 * Time: 14:54
 */
public interface PersonRepositoryExtension {

    /**
     * find all (direct) Relationships
     *
     * @param username username
     * @return Set of Friends/Relationships
     */
    Set<Person> findFriends(String username);

    /**
     * Deletes a user by username
     *
     * @param username username
     * @return if the user was successfully deleted
     */
    boolean deleteUser(String username);

    /**
     * create new Relationship from source Person to destination Person
     *
     * @param usernameSource      username of source Person
     * @param usernameDestination username of destination Person
     * @return Relationship
     */
    Relationship createRelationship(String usernameSource, String usernameDestination);

    /**
     * Finds all users where the lastname ostarts with the specified string
     */
    Iterable<Person> findPersonsWithLastnameStarting(String lastNameFragment);

    /**
     * get a list of suggested Persons which could be interresting
     *
     * @param username username
     * @return suggested usernames
     */
    List<String> suggestUnknowns(String username);
    
    /**
     * Saves the specified person, replacing the existing one if it already existst.
     */
    void saveOrReplacePerson(Person person);
}
