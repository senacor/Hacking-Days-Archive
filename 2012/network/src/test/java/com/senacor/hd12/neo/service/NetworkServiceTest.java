package com.senacor.hd12.neo.service;

import com.senacor.hd12.neo.engine.OrganisationRepository;
import com.senacor.hd12.neo.engine.PersonRepository;
import com.senacor.hd12.neo.model.Organisation;
import com.senacor.hd12.neo.model.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 16.06.12
 * Time: 11:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/spring-config.xml", "/spring-test-database-config.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class NetworkServiceTest {

    @Autowired
    private NetworkService networkService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Test
    public void testSetOrganisation(){
        Person p = new Person("Hans", "Wurst", "hwurst");
        personRepository.save(p);

        // Create new Organisation
        Organisation organisation = networkService.setOrganisation("hwurst", "Senacor");
        assertNotNull(organisation.getId());
        assertEquals("Senacor", organisation.getName());
        assertNull(organisation.getOrt());

        Person hwurst = personRepository.getPersonByUsername("hwurst");
        System.out.println(hwurst);
        assertNotNull(hwurst.getOrganisation());
        assertEquals(organisation.getId(), hwurst.getOrganisation().getId());
        assertEquals(organisation.getName(), hwurst.getOrganisation().getName());
    }
}
