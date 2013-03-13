package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Person;
import org.springframework.transaction.annotation.Transactional;

/**
 * Ralph Winzinger, Senacor
 */
// @Transactional
public interface NetworkRepository {
    public void initializeCompany();

    public Person employ(String firstname, String lastname);

    public Person employ(Person person);

    public Iterable<Person> findColleagues(Person person);
}
