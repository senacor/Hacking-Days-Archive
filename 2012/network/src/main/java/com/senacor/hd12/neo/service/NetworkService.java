package com.senacor.hd12.neo.service;

import com.senacor.hd12.neo.engine.OrganisationRepository;
import com.senacor.hd12.neo.engine.PersonRepository;
import com.senacor.hd12.neo.model.Organisation;
import com.senacor.hd12.neo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 16.06.12
 * Time: 10:54
 */
@Service
@Transactional
public class NetworkService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    public Organisation setOrganisation(String username, String organisationName) {
        Person p = personRepository.getPersonByUsername(username);
        if (null == p) {
            return null;
        }
        Organisation o = organisationRepository.getOrganisationByName(organisationName);
        if (o == null) {
            o = new Organisation(organisationName);
        }
        p.setOrganisation(o);
        personRepository.save(p);

        return o;
    }
}
