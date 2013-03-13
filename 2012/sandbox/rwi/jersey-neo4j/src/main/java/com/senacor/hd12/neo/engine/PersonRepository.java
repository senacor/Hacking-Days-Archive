package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Person;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

/**
 * Ralph Winzinger, Senacor
 */
public interface PersonRepository extends GraphRepository<Person>, NamedIndexRepository<Person> {
}
