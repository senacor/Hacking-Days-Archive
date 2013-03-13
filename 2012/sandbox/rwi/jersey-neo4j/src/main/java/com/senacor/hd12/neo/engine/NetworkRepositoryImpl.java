package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Person;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static com.senacor.hd12.neo.model.RelationshipTypes.KNOWS;


/**
 * Ralph Winzinger, Senacor
 */
@Repository
public class NetworkRepositoryImpl implements NetworkRepository {
    @Autowired
    private PersonRepository personRepository;

    private Person kl = null;
    private Person mt = null;
    private Person mp = null;

    @Override
    public void initializeCompany() {
        kl = personRepository.save(new Person("Katharina", "Landes"));
        mt = personRepository.save(new Person("Matthias", "Tomann"));
        mp = personRepository.save(new Person("Marcus", "Purzer"));

        kl.isIntroducedTo(mt);
        kl.isIntroducedTo(mp);
    }

    @Override
    public Person employ(String firstname, String lastname) {
        return employ(new Person(firstname, lastname));
    }

    @Override
    public Person employ(Person person) {
        Person newEmployee = personRepository.save(person);
        newEmployee.isIntroducedTo(kl);

        return newEmployee;
    }

    public Iterable<Person> findColleagues(Person person) {
        TraversalDescription traversal = Traversal.description().relationships(withName(KNOWS), Direction.BOTH);
        return personRepository.findAllByTraversal(person, traversal);
    }
}
