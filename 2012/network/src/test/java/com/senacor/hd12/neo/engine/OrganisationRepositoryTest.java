package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Organisation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 16.06.12
 * Time: 10:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/spring-config.xml", "/spring-test-database-config.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class OrganisationRepositoryTest {

    @Autowired
    private OrganisationRepository organisationRepository;

    @Test
    public void testFindOrganisationByName() {
        Organisation o = new Organisation("Senacor");
        o = organisationRepository.save(o);
        assertNotNull(o.getId());

        Organisation senacor = organisationRepository.getOrganisationByName("Senacor");
        assertNotNull(senacor);
        assertEquals(o.getId(), senacor.getId());
    }

    @Test
    public void testUniqueName() {
        Organisation senacor1 = organisationRepository.save(new Organisation("Senacor").setOrt("Nürnberg"));
        assertNotNull(senacor1.getId());
        assertEquals("Nürnberg", senacor1.getOrt());

        Organisation senacor2 = organisationRepository.save(new Organisation("Senacor"));
        assertEquals(senacor1.getId(), senacor2.getId());

        Organisation senacorLoaded = organisationRepository.getOrganisationByName("Senacor");
        assertEquals(senacor1.getId(), senacorLoaded.getId());
        assertEquals(null, senacorLoaded.getOrt());
    }

    @Test
    public void testDeleteOrganisation() {
        Organisation senacor1 = organisationRepository.save(new Organisation("Senacor").setOrt("Nürnberg"));
        Long senacor1Id = senacor1.getId();
        assertNotNull(senacor1Id);
        assertEquals("Nürnberg", senacor1.getOrt());

        assertEquals(true, organisationRepository.deleteOrganisation("Senacor"));
        assertEquals(false, organisationRepository.deleteOrganisation("Senacor"));

        assertEquals(null, organisationRepository.getOrganisationByName("Senacor"));
    }
}
