package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

import java.util.Map;

/**
 * Ralph Winzinger, Senacor
 */
public interface PersonRepository extends GraphRepository<Person>, NamedIndexRepository<Person>,
        PersonRepositoryExtension {

    Person getPersonByUsername(String username);

    @Query("start user=node:Person(username = {0}) "
            + "match user-[RELATIONSHIP]->friend "
            + "return friend")
    Iterable<Person> findPeers(String username);

    @Query("start user=node:Person(username = {0}) "
            + "match user<-[RELATIONSHIP]-friend "
            + "return friend")
    Iterable<Person> findFollowers(String username);
    

    @Query("start user=node:Person(username = {0}) "
            + "match user-[:RELATIONSHIP]->friend<-[?:RELATIONSHIP]-friend_of_friend "
            + "return friend AS poi,count(friend_of_friend) AS friendcount ")
    EndResult findPeersExt(String username);

    @Query("START user=node:Person(username = {0}) " +
            "MATCH p=user-[:RELATIONSHIP]->friend-[:RELATIONSHIP*1..3]->friend_of_friend," +
            " orgPath=user-[?:IS_EMPLOYED]->org<-[?:IS_EMPLOYED]-friend_of_friend " +
            "WHERE not(user-[:RELATIONSHIP]->friend_of_friend) " +
            "AND user.id != friend_of_friend.id " + // noch nicht bekannt
            "RETURN friend_of_friend, length(p) AS pathDepth, length(orgPath) AS orgPathLength " +
            "ORDER BY orgPathLength, pathDepth " // first people who are in the same organisation
    )
    EndResult<Map<String, Object>> suggestUnknownPersons(String username);
    
    @Query("start user=node(*) "
            + "match user<-[:RELATIONSHIP]-friend "
            + "return user AS poi,count(friend) AS friendcount " 
            + "order by count(friend) "
            + "limit 10")
    EndResult getTopTen();
}
