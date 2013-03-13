package com.senacor.hd12.neo.service;

import java.util.HashSet;
import java.util.Set;

import com.senacor.hd12.neo.engine.PersonRepository;
import com.senacor.hd12.neo.model.Person;
import com.senacor.hd12.neo.model.Relationship;
import com.senacor.hd12.neo.model.visualisation.Visualisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VisualisationService {
    @Autowired
    private PersonRepository personRepository;

    public Visualisation getVisualisation(String username, int depth) {
        Person ich = personRepository.getPersonByUsername(username);

        Visualisation result = new Visualisation();

        Set<Person> pastLevels = new HashSet<Person>();
        Set<Person> currentLevel =  new HashSet<Person>();
        currentLevel.add(ich);
        Set<Person> nextLevel =  new HashSet<Person>();

        for(int level = 0; level <= depth; level++) {
            for (Person person : currentLevel) {
                if(pastLevels.contains(person)) {
                    continue;
                }

                result.addPerson(person, colorForLevel(level));
                if(level != depth) {
                    Set<Person> peers = new HashSet<Person>();
                    for (Relationship relationship : person.getRelationships()) {
                        if(!pastLevels.contains(relationship.getDestination())) {
                            peers.add(relationship.getDestination());
                        }
                        result.addEdges(person, peers);
                    }

                    nextLevel.addAll(peers);
                }
            }

            pastLevels.addAll(currentLevel);
            nextLevel.removeAll(currentLevel);
            currentLevel = nextLevel;
            nextLevel = new HashSet<Person>();
        }

        return result;
    }

    private String colorForLevel(int level) {
        switch (level) {
            case 0:
                return "red";
            case 1:
                return "blue";
            default:
                return "green";
        }
    }
}
