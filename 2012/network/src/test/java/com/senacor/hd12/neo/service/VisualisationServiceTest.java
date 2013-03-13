package com.senacor.hd12.neo.service;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.util.HashMap;
import java.util.Map;

import com.senacor.hd12.neo.engine.PersonRepository;
import com.senacor.hd12.neo.model.Person;
import com.senacor.hd12.neo.model.visualisation.Length;
import com.senacor.hd12.neo.model.visualisation.Node;
import com.senacor.hd12.neo.model.visualisation.Visualisation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/spring-config.xml", "/spring-test-database-config.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class VisualisationServiceTest {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private VisualisationService visualisationService;

    @Before
    public void prepareData() {
        Person p1 = new Person("Hans", "Wurst", "hwurst");
        personRepository.save(p1);
        Person p2 = new Person("Oleg", "Galimov", "ogalimov");
        personRepository.save(p2);
        Person p3 = new Person("Andreas", "Keefer", "akeefer");
        personRepository.save(p3);
        p1.addRelationship(p2);
        p2.addRelationship(p3);
        personRepository.save(p1);
        personRepository.save(p2);
    }

    @Test
    public void testDepth0() {
        Visualisation visualisation = visualisationService.getVisualisation("hwurst", 0);
        assertThat(visualisation.getEdges()).isEmpty();
        assertThat(visualisation.getNodes()).includes(entry("hwurst", new Node("red", "dot", "hwurst")));
    }

    @Test
    public void testDepth1() {
        Visualisation visualisation = visualisationService.getVisualisation("hwurst", 1);
        Map<String, Length> edgesHwurst = new HashMap<String, Length>();
        edgesHwurst.put("ogalimov", new Length(3));
        assertThat(visualisation.getEdges()).includes(
                entry("hwurst", edgesHwurst)
        );
        assertThat(visualisation.getNodes()).includes(
                entry("hwurst", new Node("red", "dot", "hwurst")),
                entry("ogalimov", new Node("blue", "dot", "ogalimov"))
        );
    }

    @Test
    public void testDepth2() {
        Visualisation visualisation = visualisationService.getVisualisation("hwurst", 2);
        Map<String, Length> edgesHwurst = new HashMap<String, Length>();
        edgesHwurst.put("ogalimov", new Length(3));
        Map<String, Length> edgesOgalimov = new HashMap<String, Length>();
        edgesOgalimov.put("akeefer", new Length(3));
        assertThat(visualisation.getEdges()).includes(
                entry("hwurst", edgesHwurst),
                entry("ogalimov", edgesOgalimov)
        );
        assertThat(visualisation.getNodes()).includes(
                entry("hwurst", new Node("red", "dot", "hwurst")),
                entry("ogalimov", new Node("blue", "dot", "ogalimov")),
                entry("akeefer", new Node("green", "dot", "akeefer"))
        );
    }
}
